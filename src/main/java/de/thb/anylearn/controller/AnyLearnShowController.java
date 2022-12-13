package de.thb.anylearn.controller;

import de.thb.anylearn.common.SupportFunctions;
import de.thb.anylearn.controller.form.AnyLearnFormModel;
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

    /**
     * http://localhost:8080/
     *
     * @param model does something
     * @return String for html
     */
    @PostMapping("show/folder={id}/cat={categories}")
    public RedirectView postCards(Model model, AnyLearnFormModel form) {
        String cat = supportFunctions.arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/folder=" + form.getFolderId() + "/cat=" + cat);
    }

    @PostMapping("show")
    public RedirectView allCardsPost(Model model, AnyLearnFormModel form) {
        String cat = supportFunctions.arrayToUrlString(form.getCategoryId());

        return new RedirectView("/show/folder=" + form.getFolderId() + "/cat=" + cat);
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
}
