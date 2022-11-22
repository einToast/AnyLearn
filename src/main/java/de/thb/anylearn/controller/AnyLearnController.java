package de.thb.anylearn.controller;

import de.thb.anylearn.controller.form.AnyLearnFormModel;
import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

//To-do: Controller aufteilen
@Controller
public class AnyLearnController {

    @Autowired
    private DeskService deskService;


    @GetMapping("learn/finished")
    public String showFinished(Model model) {

        return "finished";
    }

    @GetMapping("learn/folder={folderId}/cat={categories}/card={cardId}/{difficulty}")
    public RedirectView reschedule(@PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, @PathVariable("difficulty") int difficulty, Model model) {

        deskService.rescheduleCard(cardId, difficulty);

        String cat = arrayToUrlString(categories);
        return new RedirectView("/learn/folder=" + folderId + "/cat=" + cat);
    }

    @GetMapping("learn/folder={folderId}/cat={categories}/card={cardId}/answer")
    public String showAnswer(@PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {

        model.addAttribute("card", deskService.getCardById(cardId));
        model.addAttribute("folderId", folderId);
        model.addAttribute("selectedCategories", categories);

        return "learn_answer";
    }

    @PostMapping("learn/folder={folderId}/cat={categories}/card={cardId}")
    public RedirectView getAnswer(@PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {
        String cat = arrayToUrlString(categories);
        return new RedirectView("/learn/folder=" + folderId + "/cat=" + cat + "/card=" + cardId + "/answer");
    }

    @GetMapping("edit/finished")
    public String showEditFinished(Model model) {

        return "editFinished";
    }

    @GetMapping("learn/folder={folderId}/cat={categories}/card={cardId}")
    public String getCards(@PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, @PathVariable("cardId") int cardId, Model model) {

        model.addAttribute("card", deskService.getCardById(cardId));

        return "learn";
    }

    /**
     * http://localhost:8080/learn
     *
     * @param model does something
     * @return String for html
     */
    @GetMapping("learn/folder={folderId}/cat={categories}")
    public RedirectView nextCard(@PathVariable("folderId") int folderId, @PathVariable("categories") int[] categories, Model model) {
        int cardId = deskService.getNextCardIdToLearn(folderId, categories);
        if (cardId == 0) {
            return new RedirectView("/learn/finished");
        } else {
            String cat = arrayToUrlString(categories);

            return new RedirectView("/learn/folder=" + folderId + "/cat=" + cat + "/card=" + cardId);
        }
    }
    @GetMapping("learn")
    public RedirectView nextCard(Model model) {
        return new RedirectView("/learn/folder=0/cat=0");

    }


    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @GetMapping("show/folder={id}/cat={categories}")
    public String getCards(@PathVariable("id") int id, @PathVariable("categories") int[] categories, Model model, AnyLearnFormModel form) {

        model.addAttribute("cards1", deskService.getFilteredCard(id, categories));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("folderId", id);
        model.addAttribute("selectedCategories", categories);

        return "cards";
    }

    @GetMapping("edit/{id}")
    public String editCards(@PathVariable("id") int id, Model model) {

        model.addAttribute("card", deskService.getCardById(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("selectedCategories", deskService.getAllCategoryIdFromCard(id));
        return "edit";
    }


    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @PostMapping("show/folder={id}/cat={categories}")
    public RedirectView postCards(Model model, AnyLearnFormModel form) {
        String cat = arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @PostMapping("show")
    public RedirectView allCardsPost(Model model, AnyLearnFormModel form) {
        String cat = arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @PostMapping("edit/{id}")
    public RedirectView changeCard(Model model, CardFormModel form){
        deskService.updateCard(form);

        return new RedirectView("/edit/finished");
    }

    @GetMapping("show")
    public RedirectView allCardsGet(Model model, AnyLearnFormModel form) {
        // TODO: Redirect auf / ausführen der Funktion getCards für Kontinuität -> Erledigt 22.11. (CD)
        return new RedirectView("/show/folder=" + 0 + "/cat=" + 0);
    }

    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @GetMapping()
    public String startPage(Model model) {

        return "home";
    }

    /**
     * Function to convert a int-Array into a string for url (e.g. categories)
     *
     * @param arr (int-array which should converted to String for URL
     * @return return_string
     */
    public String arrayToUrlString(int[] arr) {
        String return_string = "";
        if (arr != null) {
            for (int a : arr) {
                if (!return_string.equals(""))
                    return_string += ",";
                return_string = return_string + a;
            }
        } else {
            return_string = "0";
        }
        return return_string;
    }


}
