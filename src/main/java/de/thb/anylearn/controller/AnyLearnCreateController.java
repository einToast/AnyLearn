package de.thb.anylearn.controller;

import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.controller.form.EntityFormModel;
import de.thb.anylearn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AnyLearnCreateController {

    @Autowired
    private AnyLearnCreateSevice createSevice;

    @Autowired
    private AnyLearnGetSevice getSevice;

    @GetMapping("create/{currUserId}/card")
    public String createCard(@PathVariable("currUserId") int currUserId, Model model) {
        int[] shared = {currUserId};
        model.addAttribute("folder1", getSevice.getAllFolder());
        model.addAttribute("category1", getSevice.getAllCategory());
        model.addAttribute("allUser", getSevice.getAllUser());
        model.addAttribute("sharedUsersId", shared);
        model.addAttribute("currUserId", currUserId);
        return "createCard";
    }

    @PostMapping("create/{currUserId}/card")
    public RedirectView applyCard(@PathVariable("currUserId") int currUserId, CardFormModel form) {
        createSevice.addCard(form);

        return new RedirectView("/create/" + currUserId + "/finished");
    }

    @GetMapping("create/{currUserId}/finished")
    public String showCreateFinished(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("mode", "erstellt");
        model.addAttribute("currUserId", currUserId);
        return "finishedCard";
    }

    @GetMapping("create/{currUserId}/{name}")
    public String createUser(@PathVariable("currUserId") int currUserId,@PathVariable("name") String name, Model model) {
        model.addAttribute("currUserId", currUserId);
        model.addAttribute("name", name);
        return "createEntity";
    }

    @PostMapping("create/{currUserId}/{name}")
    public RedirectView createUser(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, EntityFormModel form) {
        switch (name){
            case "users" -> createSevice.addUser(form);
            case "folders" -> createSevice.addFolder(form);
            case "categories" -> createSevice.addCategory(form);
        }

        return new RedirectView("/create/" + currUserId + "/" + name + "/finished");
    }

    @GetMapping("create/{currUserId}/{name}/finished")
    public String CreateFinished(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, Model model) {
        switch (name){
            case "users" -> model.addAttribute("name", "den Nutzer");
            case "folders" -> model.addAttribute("name", "den Ordner");
            case "categories" -> model.addAttribute("name", "die Kategorie");
        }
        model.addAttribute("name1", name);
        model.addAttribute("mode", "erstellt");
        model.addAttribute("currUserId", currUserId);
        return "finishedEntity";
    }
}
