package de.thb.anylearn.controller;

import de.thb.anylearn.common.SupportFunctions;
import de.thb.anylearn.service.*;
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
    private AnyLearnGetSevice getSevice;

    @Autowired
    private AnyLearnUpdateSevice updateSevice;

    private final SupportFunctions supportFunctions = new SupportFunctions();

    @GetMapping("learn/{currUserId}/finished")
    public String showFinished(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("currUserId", currUserId);
        return "learnFinished";
    }

    @GetMapping("learn/{currUserId}/folder={folderId}/cat={categories}/card={cardId}/{difficulty}")
    public RedirectView reschedule(@PathVariable("currUserId") int currUserId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, @PathVariable("difficulty") int difficulty) {

        updateSevice.updateCardTime(cardId, difficulty, currUserId);

        String cat = supportFunctions.arrayToUrlString(categories);
        return new RedirectView("/learn/" + currUserId + "/folder=" + folderId + "/cat=" + cat);
    }

    @GetMapping("learn/{currUserId}/folder={folderId}/cat={categories}/card={cardId}/answer")
    public String showAnswer(@PathVariable("currUserId") int currUserId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {

        model.addAttribute("card", getSevice.getCardById(cardId));
        model.addAttribute("folderId", folderId);
        model.addAttribute("selectedCategories", categories);
        model.addAttribute("currUserId", currUserId);

        return "learnAnswer";
    }

    @PostMapping("learn/{userId}/folder={folderId}/cat={categories}/card={cardId}")
    public RedirectView getAnswer(@PathVariable("userId") int userId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId) {
        String cat = supportFunctions.arrayToUrlString(categories);
        return new RedirectView("/learn/" + userId + "/folder=" + folderId + "/cat=" + cat + "/card=" + cardId + "/answer");
    }

    @GetMapping("learn/{currUserId}/folder={folderId}/cat={categories}/card={cardId}")
    public String getCards(@PathVariable("currUserId") int currUserId, @PathVariable("cardId") int cardId, Model model) {

        model.addAttribute("card", getSevice.getCardById(cardId));
        model.addAttribute("currUserId", currUserId);
        return "learn";
    }

    @GetMapping("learn/{currUserId}/folder={folderId}/cat={categories}")
    public RedirectView nextCard(@PathVariable("currUserId") int currUserId, @PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories) {
        int cardId = getSevice.getNextCardIdToLearn(folderId, categories, currUserId);
        if (cardId == 0) {
            return new RedirectView("/learn/" + currUserId + "/finished");
        } else {
            String cat = supportFunctions.arrayToUrlString(categories);

            return new RedirectView("/learn/" + currUserId + "/folder=" + folderId + "/cat=" + cat + "/card=" + cardId);
        }
    }

    @GetMapping("learn/{currUserId}")
    public RedirectView nextCard(@PathVariable("currUserId") int currUserId) {
        return new RedirectView("/learn/" + currUserId + "/folder=0/cat=0");

    }
}
