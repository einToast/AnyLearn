package de.thb.anylearn.controller;

import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.controller.form.EntityFormModel;
import de.thb.anylearn.service.DeskService;
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
    private DeskService deskService;

    @GetMapping("create/{userId}/card")
    public String createCard(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("owner", deskService.getAllUser());
        model.addAttribute("userId", userId);
        return "createCard";
    }

    @PostMapping("create/{userId}/card")
    public RedirectView applyCard(@PathVariable("userId") int userId, CardFormModel form) {
        deskService.addCard(form);

        return new RedirectView("/create/" + userId + "/finished");
    }

    @GetMapping("create/{userId}/finished")
    public String showCreateFinished(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("mode", "erstellt");
        model.addAttribute("userId", userId);
        return "cardFinished";
    }

    @GetMapping("create/{userId}/{name}")
    public String createUser(@PathVariable("userId") int userId,@PathVariable("name") String name, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("name", name);
        return "createEntity";
    }

    @PostMapping("create/{userId}/{name}")
    public RedirectView createUser(@PathVariable("userId") int userId, @PathVariable("name") String name, EntityFormModel form) {
        switch (name){
            case "users" -> deskService.addUser(form);
            case "folders" -> deskService.addFolder(form);
            case "categories" -> deskService.addCategory(form);
        }

        return new RedirectView("/create/" + userId + "/" + name + "/finished");
    }

    @GetMapping("create/{userId}/{name}/finished")
    public String CreateFinished(@PathVariable("userId") int userId, @PathVariable("name") String name, Model model) {
        switch (name){
            case "users" -> model.addAttribute("name", "den Nutzer");
            case "folders" -> model.addAttribute("name", "den Ordner");
            case "categories" -> model.addAttribute("name", "die Kategorie");
        }
        model.addAttribute("name1", name);
        model.addAttribute("mode", "erstellt");
        model.addAttribute("userId", userId);
        return "entityFinished";
    }
}
