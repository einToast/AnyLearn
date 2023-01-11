package de.thb.anylearn.controller;

import de.thb.anylearn.common.SupportFunctions;
import de.thb.anylearn.controller.form.AnyLearnFormModel;
import de.thb.anylearn.controller.form.UserFormModel;
import de.thb.anylearn.service.*;
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
    private AnyLearnGetSevice getSevice;

    private final SupportFunctions supportFunctions = new SupportFunctions();


    @GetMapping("show/{currUserId}/folder={id}/cat={categories}")
    public String getCards(@PathVariable("currUserId") int currUserId, @PathVariable("id") int id, @PathVariable("categories") int[] categories, Model model) {

        model.addAttribute("cards1", getSevice.getFilteredCard(id, categories, currUserId));
        model.addAttribute("folder1", getSevice.getAllFolder());
        model.addAttribute("category1", getSevice.getAllCategory());
        model.addAttribute("folderId", id);
        model.addAttribute("selectedCategories", categories);
        model.addAttribute("currUserId", currUserId);

        return "showCards";
    }

    @PostMapping("show/{currUserId}/folder={id}/cat={categories}")
    public RedirectView postCards(@PathVariable("currUserId") int currUserId, AnyLearnFormModel form) {
        String cat = supportFunctions.arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/" + currUserId + "/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @PostMapping("show/{currUserId}")
    public RedirectView allCardsPost(@PathVariable("currUserId") int currUserId, AnyLearnFormModel form) {
        String cat = supportFunctions.arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/ " + currUserId + "/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @GetMapping("show/{currUserId}")
    public RedirectView allCardsGet(@PathVariable("currUserId") int currUserId) {
        return new RedirectView("/show/" + currUserId + "/folder=" + 0 + "/cat=" + 0);
    }

    @GetMapping("show/{currUserId}/users")
    public String allUser(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("name", "Users");
        model.addAttribute("entities", getSevice.getAllUser());
        return "showEntities";
    }

    @GetMapping("show/{currUserId}/folders")
    public String allFolder(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("name", "Folders");
        model.addAttribute("entities", getSevice.getAllFolder());
        return "showEntities";
    }

    @GetMapping("show/{currUserId}/categories")
    public String allCategories(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("name", "Categories");
        model.addAttribute("entities", getSevice.getAllCategory());
        return "showEntities";
    }

    @PostMapping()
    public RedirectView startPagePost(UserFormModel form) {

        return new RedirectView("/show/" + form.getUserId());
    }

    @GetMapping()
    public String startPage(Model model) {

        model.addAttribute("users", getSevice.getAllUser());

        return "home";
    }
}
