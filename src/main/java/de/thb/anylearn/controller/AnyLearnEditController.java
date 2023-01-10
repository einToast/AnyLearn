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

//To-do: Controller aufteilen
@Controller
public class AnyLearnEditController {

    @Autowired
    private DeskService deskService;

    @GetMapping("edit/{userId}/cards/{id}")
    public String editCard(@PathVariable("userId") int userId, @PathVariable("id") int id, Model model) {

        model.addAttribute("card", deskService.getCardById(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("selectedCategories", deskService.getAllCategoryIdFromCard(id));
        model.addAttribute("owner", deskService.getAllUser());
        model.addAttribute("userId", userId);
        return "editCard";
    }

    @PostMapping("edit/{userId}/cards/{id}")
    public RedirectView editCard(@PathVariable("userId") int userId, CardFormModel form) {
        deskService.updateCard(form);

        return new RedirectView("/edit/" + userId + "/cards/finished");
    }

    @GetMapping("delete/{userId}/cards/{id}")
    public String deleteCard(@PathVariable("userId") int userId, @PathVariable("id") int id, Model model) {

        model.addAttribute("card", deskService.getCardById(id));
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("selectedCategories", deskService.getAllCategoryIdFromCard(id));
        model.addAttribute("owner", deskService.getAllUser());
        model.addAttribute("userId", userId);
        return "deleteCard";
    }

    @PostMapping("delete/{userId}/cards/{id}")
    public RedirectView deleteCard(@PathVariable("userId") int userId, @PathVariable("id") int id) {
        deskService.deleteCard(id);
        return new RedirectView("/delete/" + userId + "/cards/finished");
    }

    @GetMapping("delete/{userId}/cards/finished")
    public String showDeleteFinished(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("mode", "gelöscht");
        model.addAttribute("userId", userId);
        return "cardFinished";
    }

    @GetMapping("edit/{userId}/cards/finished")
    public String showEditFinished(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("mode", "bearbeitet");
        model.addAttribute("userId", userId);
        return "cardFinished";
    }

    @GetMapping("edit/{userId}/{name}/{id}")
    public String editEntities(@PathVariable("userId") int userId, @PathVariable("name") String name, @PathVariable("id") int id, Model model) {
        switch (name){
            case "users" -> model.addAttribute("entity", deskService.getUserById(id));
            case "folders" -> model.addAttribute("entity", deskService.getFolderById(id));
            case "categories" -> model.addAttribute("entity", deskService.getCategoryById(id));
        }
        model.addAttribute("userId", userId);
        model.addAttribute("name", name);
        return "editEntity";
    }

    @PostMapping("edit/{userId}/{name}/{id}")
    public RedirectView editEntities(@PathVariable("userId") int userId, @PathVariable("name") String name, @PathVariable("id") int id, EntityFormModel form) {
        switch (name){
            case "users" -> deskService.updateUser(form);
            case "folders" -> deskService.updateFolder(form);
            case "categories" -> deskService.updateCategory(form);
        }
        return new RedirectView("/edit/" + userId + "/" + name + "/finished");
    }

    @GetMapping("edit/{userId}/{name}/finished")
    public String showEditFinished(@PathVariable("userId") int userId, @PathVariable("name") String name, Model model) {
        switch (name){
            case "users" -> model.addAttribute("name", "den Nutzer");
            case "folders" -> model.addAttribute("name", "den Ordner");
            case "categories" -> model.addAttribute("name", "die Kategorie");
        }

        model.addAttribute("mode", "bearbeitet");
        model.addAttribute("userId", userId);
        return "entityFinished";
    }

}
