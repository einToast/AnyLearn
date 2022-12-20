package de.thb.anylearn.controller;

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
public class AnyLearnEditController {

    @Autowired
    private DeskService deskService;

    @GetMapping("edit/{userId}/{id}")
    public String editCard(@PathVariable("userId") int userId, @PathVariable("id") int id, Model model) {

        model.addAttribute("card", deskService.getCardById(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("selectedCategories", deskService.getAllCategoryIdFromCard(id));
        model.addAttribute("owner", deskService.getAllUser());
        model.addAttribute("userId", userId);
        return "editCard";
    }

    @PostMapping("edit/{userId}/{id}")
    public RedirectView editCard(@PathVariable("userId") int userId, CardFormModel form) {
        deskService.updateCard(form);

        return new RedirectView("/edit/" + userId + "/finished");
    }

    @GetMapping("delete/{userId}/{id}")
    public String deleteCard(@PathVariable("userId") int userId, @PathVariable("id") int id, Model model) {

        model.addAttribute("card", deskService.getCardById(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("selectedCategories", deskService.getAllCategoryIdFromCard(id));
        model.addAttribute("owner", deskService.getAllUser());
        model.addAttribute("userId", userId);
        return "deleteCard";
    }

    @PostMapping("delete/{userId}/{id}")
    public RedirectView deleteCard(@PathVariable("userId") int userId, @PathVariable("id") int id) {
        deskService.deleteCard(id);
        return new RedirectView("/delete/" + userId + "/finished");
    }

    @GetMapping("delete/{userId}/finished")
    public String showDeleteFinished(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("mode", "gelöscht");
        model.addAttribute("userId", userId);
        return "cardFinished";
    }

    @GetMapping("edit/{userId}/finished")
    public String showEditFinished(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("mode", "bearbeitet");
        model.addAttribute("userId", userId);
        return "cardFinished";
    }
}
