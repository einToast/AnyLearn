package de.thb.anylearn.controller;

import de.thb.anylearn.controller.form.AnyLearnFormModel;
import de.thb.anylearn.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AnyLearnController {

    @Autowired
    private DeskService deskService;

    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @GetMapping("folder/{id}")
    public String allCards(@PathVariable("id") int id, Model model, AnyLearnFormModel form) {

        model.addAttribute("cards1", deskService.getFilteredCard(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("formula", id);

        return "cards";
    }



    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @PostMapping("folder/{id}")
    public RedirectView filteredCardsByFolder(Model model, AnyLearnFormModel form) {

        return new RedirectView("/folder/"+form.getFolderId());
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

}
