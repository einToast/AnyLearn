package de.thb.anylearn.controller;

import de.thb.anylearn.controller.form.CardFormModel;
import de.thb.anylearn.controller.form.EntityFormModel;
import de.thb.anylearn.service.AnyLearnDeleteSevice;
import de.thb.anylearn.service.AnyLearnGetSevice;
import de.thb.anylearn.service.AnyLearnUpdateSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

//To-do: Controller aufteilen
@Controller
public class AnyLearnDeleteController {

    @Autowired
    private AnyLearnDeleteSevice deleteSevice;

    @Autowired
    private AnyLearnGetSevice getSevice;

    @Autowired
    private AnyLearnUpdateSevice updateSevice;

    @GetMapping("delete/{currUserId}/cards/{id}")
    public String deleteCard(@PathVariable("currUserId") int currUserId, @PathVariable("id") int id, Model model) {

        model.addAttribute("card", getSevice.getCardById(id));
        model.addAttribute("folder1", getSevice.getAllFolder());
        model.addAttribute("category1", getSevice.getAllCategory());
        model.addAttribute("selectedCategories", getSevice.getAllCategoryIdFromCard(id));
        model.addAttribute("allUser", getSevice.getAllUser());
        model.addAttribute("sharedUsersId", getSevice.getAllUserIdFromCard(id));
        model.addAttribute("currUserId", currUserId);
        return "deleteCard";
    }

    @PostMapping("delete/{currUserId}/cards/{id}")
    public RedirectView deleteCard(@PathVariable("currUserId") int currUserId, @PathVariable("id") int id) {
        deleteSevice.deleteCard(id);
        return new RedirectView("/delete/" + currUserId + "/cards/finished");
    }

    @GetMapping("delete/{currUserId}/cards/finished")
    public String showDeleteCardFinished(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("mode", "gelöscht");
        model.addAttribute("currUserId", currUserId);
        return "finishedCard";
    }

    @GetMapping("delete/{currUserId}/{name}/{id}")
    public String deleteEntity(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, @PathVariable("id") int id, Model model) {
        switch (name){
            case "users" -> model.addAttribute("entity", getSevice.getUserById(id));
            case "folders" -> model.addAttribute("entity", getSevice.getFolderById(id));
            case "categories" -> model.addAttribute("entity", getSevice.getCategoryById(id));
        }
        model.addAttribute("currUserId", currUserId);
        model.addAttribute("name", name);
        return "deleteEntity";
    }

    @PostMapping("delete/{currUserId}/{name}/{id}")
    public RedirectView deleteEntity(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, @PathVariable("id") int id) {
        boolean success = true;
        switch (name){
            case "users" -> deleteSevice.deleteUser(id);
            case "folders" -> success = deleteSevice.deleteFolder(id);
            case "categories" -> deleteSevice.deleteCategory(id);
        }
        if (success) {
            return new RedirectView("/delete/" + currUserId + "/" + name + "/finished");
        } else {
            return new RedirectView("/delete/" + currUserId + "/" + name + "/failed");
        }
    }

    @GetMapping("delete/{currUserId}/{name}/finished")
    public String showDeleteEntityFinished(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, Model model) {
        switch (name){
            case "users" -> model.addAttribute("name", "den Nutzer");
            case "folders" -> model.addAttribute("name", "den Ordner");
            case "categories" -> model.addAttribute("name", "die Kategorie");
        }

        model.addAttribute("mode", "gelöscht");
        model.addAttribute("currUserId", currUserId);
        model.addAttribute("name1", name);
        return "finishedEntity";
    }

    @GetMapping("delete/{currUserId}/{name}/failed")
    public String showDeleteEntityFailed(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, Model model) {
        switch (name){
            case "folders" -> model.addAttribute("name", "den Ordner");
        }

        model.addAttribute("currUserId", currUserId);
        model.addAttribute("name1", name);

        return "failedEntity";
    }

}
