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
public class AnyLearnEditController {

    @Autowired
    private DeskService deskService;

    @GetMapping("edit/finished")
    public String showEditFinished(Model model) {

        return "editFinished";
    }

    @GetMapping("edit/{id}")
    public String editCards(@PathVariable("id") int id, Model model) {

        model.addAttribute("card", deskService.getCardById(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("selectedCategories", deskService.getAllCategoryIdFromCard(id));
        model.addAttribute("owner", deskService.getAllUser());
        return "edit";
    }


    @PostMapping("edit/{id}")
    public RedirectView changeCard(Model model, CardFormModel form){
        deskService.updateCard(form);

        return new RedirectView("/edit/finished");
    }



}
