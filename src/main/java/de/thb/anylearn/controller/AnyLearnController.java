package de.thb.anylearn.controller;

import de.thb.anylearn.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping("cards")
    public String allCards(Model model) {

        model.addAttribute("cards1", deskService.getAllCard());


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
