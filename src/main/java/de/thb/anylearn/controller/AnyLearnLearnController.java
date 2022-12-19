package de.thb.anylearn.controller;

import de.thb.anylearn.common.SupportFunctions;
import de.thb.anylearn.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AnyLearnLearnController {

    @Autowired
    private DeskService deskService;

    private final SupportFunctions supportFunctions = new SupportFunctions();

    @GetMapping("learn/finished")
    public String showFinished(Model model) {
        return "finished";
    }

    @GetMapping("learn/{userId}/folder={folderId}/cat={categories}/card={cardId}/{difficulty}")
    public RedirectView reschedule(@PathVariable("userId") int userId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, @PathVariable("difficulty") int difficulty, Model model) {

        deskService.rescheduleCard(cardId, difficulty);

        String cat = supportFunctions.arrayToUrlString(categories);
        return new RedirectView("/learn/" + userId + "/folder=" + folderId + "/cat=" + cat);
    }

    @GetMapping("learn/{userId}/folder={folderId}/cat={categories}/card={cardId}/answer")
    public String showAnswer(@PathVariable("userId") int userId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {

        model.addAttribute("card", deskService.getCardById(cardId));
        model.addAttribute("folderId", folderId);
        model.addAttribute("selectedCategories", categories);
        model.addAttribute("currUserId", userId);

        return "learn_answer";
    }

    @PostMapping("learn/{userId}/folder={folderId}/cat={categories}/card={cardId}")
    public RedirectView getAnswer(@PathVariable("userId") int userId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {
        String cat = supportFunctions.arrayToUrlString(categories);
        return new RedirectView("/learn/" + userId + "folder=" + folderId + "/cat=" + cat + "/card=" + cardId + "/answer");
    }

    @GetMapping("learn/{userId}/folder={folderId}/cat={categories}/card={cardId}")
    public String getCards(@PathVariable("userId") int userId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {

        model.addAttribute("card", deskService.getCardById(cardId));

        return "learn";
    }

    /**
     * http://localhost:8080/learn
     *
     * @param model does something
     * @return String for html
     */
    @GetMapping("learn/{userId}/folder={folderId}/cat={categories}")
    public RedirectView nextCard(@PathVariable("userId") int userId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, Model model) {
        int cardId = deskService.getNextCardIdToLearn(folderId, categories, userId);
        if (cardId == 0) {
            return new RedirectView("/learn/finished");
        } else {
            String cat = supportFunctions.arrayToUrlString(categories);

            return new RedirectView("/learn/" + userId +"/folder=" + folderId + "/cat=" + cat + "/card=" + cardId);
        }
    }
    @GetMapping("learn/{userId}")
    public RedirectView nextCard(@PathVariable("userId") int userId, Model model) {
        return new RedirectView("/learn/" + userId + "/folder=0/cat=0");

    }
}
