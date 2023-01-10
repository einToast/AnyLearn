package de.thb.anylearn.controller;

import de.thb.anylearn.common.SupportFunctions;
import de.thb.anylearn.controller.form.AnyLearnFormModel;
import de.thb.anylearn.controller.form.UserFormModel;
import de.thb.anylearn.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AnyLearnShowController {

    @Autowired
    private DeskService deskService;

    private final SupportFunctions supportFunctions = new SupportFunctions();


    @GetMapping("show/{userId}/folder={id}/cat={categories}")
    public String getCards(@PathVariable("userId") int userId, @PathVariable("id") int id, @PathVariable("categories") int[] categories, Model model) {

        model.addAttribute("cards1", deskService.getFilteredCard(id, categories, userId));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("folderId", id);
        model.addAttribute("selectedCategories", categories);
        model.addAttribute("userId", userId);

        return "showCards";
    }

    @PostMapping("show/{userId}/folder={id}/cat={categories}")
    public RedirectView postCards(@PathVariable("userId") int userId, AnyLearnFormModel form) {
        String cat = supportFunctions.arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/" + userId + "/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @PostMapping("show/{userId}")
    public RedirectView allCardsPost(@PathVariable("userId") int userId, AnyLearnFormModel form) {
        String cat = supportFunctions.arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/ " + userId + "/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @GetMapping("show/{userId}")
    public RedirectView allCardsGet(@PathVariable("userId") int userId) {
        return new RedirectView("/show/" + userId + "/folder=" + 0 + "/cat=" + 0);
    }

    @GetMapping("show/{userId}/users")
    public String allUser(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("name", "Users");
        model.addAttribute("entities", deskService.getAllUser());
        return "showEntities";
    }

    @GetMapping("show/{userId}/folders")
    public String allFolder(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("name", "Folders");
        model.addAttribute("entities", deskService.getAllFolder());
        return "showEntities";
    }

    @GetMapping("show/{userId}/categories")
    public String allCategories(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("name", "Categories");
        model.addAttribute("entities", deskService.getAllCategory());
        return "showEntities";
    }

    @PostMapping()
    public RedirectView startPagePost(UserFormModel form) {

        return new RedirectView("/show/" + form.getUserId());
    }

    @GetMapping()
    public String startPage(Model model) {

        model.addAttribute("users", deskService.getAllUser());

        return "home";
    }
}
