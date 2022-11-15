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

import java.util.Arrays;

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
    @GetMapping("show/folder={id}/cat={categories}")
    public String getCards(@PathVariable("id") int id, @PathVariable("categories") int[] categories, Model model, AnyLearnFormModel form) {

        model.addAttribute("cards1", deskService.getFilteredCard(id, categories));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("formula", id);

        return "cards";
    }



    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @PostMapping("show/folder={id}/cat={categories}")
    public RedirectView postCards(Model model, AnyLearnFormModel form) {
        String cat = "";
        int[] cat_arr = form.getCategoryId();
        if (cat_arr != null) {
            for (int a : cat_arr) {
                if (!cat.equals(""))
                    cat += ",";
                cat = cat + a;
            }
        } else {
            cat = "0";
        }
        return new RedirectView("/show/folder="+form.getFolderId() + "/cat=" + cat);
    }

    @PostMapping("show")
    public RedirectView allCardsPost(Model model, AnyLearnFormModel form) {
        String cat = "";
        int[] cat_arr = form.getCategoryId();
        if (cat_arr != null) {
            for (int a : cat_arr) {
                if (!cat.equals(""))
                    cat += ",";
                cat = cat + a;
            }
        } else {
            cat = "0";
        }

        return new RedirectView("/show/folder="+form.getFolderId() + "/cat=" + cat);
    }
    @GetMapping("show")
    public String allCardsGet(Model model, AnyLearnFormModel form) {
        // TODO: Redirect auf / ausführen der Funktion getCards für Kontinuität
        model.addAttribute("cards1", deskService.getAllCard());
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("formula", 0);
        return "cards";
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
