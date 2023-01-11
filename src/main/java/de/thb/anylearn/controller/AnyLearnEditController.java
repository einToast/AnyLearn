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

//To-do: Controller aufteilen
@Controller
public class AnyLearnEditController {

    @Autowired
    private AnyLearnDeleteSevice deleteSevice;

    @Autowired
    private AnyLearnGetSevice getSevice;

    @Autowired
    private AnyLearnUpdateSevice updateSevice;

    @GetMapping("edit/{currUserId}/cards/{id}")
    public String editCard(@PathVariable("currUserId") int currUserId, @PathVariable("id") int id, Model model) {

        model.addAttribute("card", getSevice.getCardById(id));
        model.addAttribute("folder1", getSevice.getAllFolder());
        model.addAttribute("category1", getSevice.getAllCategory());
        model.addAttribute("selectedCategories", getSevice.getAllCategoryIdFromCard(id));
        model.addAttribute("allUser", getSevice.getAllUser());
        model.addAttribute("sharedUsersId", getSevice.getAllUserIdFromCard(id));
        model.addAttribute("currUserId", currUserId);
        return "editCard";
    }

    @PostMapping("edit/{currUserId}/cards/{id}")
    public RedirectView editCard(@PathVariable("currUserId") int currUserId, CardFormModel form) {
        updateSevice.updateCard(form);

        return new RedirectView("/edit/" + currUserId + "/cards/finished");
    }

    @GetMapping("edit/{currUserId}/cards/finished")
    public String showEditCardFinished(@PathVariable("currUserId") int currUserId, Model model) {
        model.addAttribute("mode", "bearbeitet");
        model.addAttribute("currUserId", currUserId);
        return "finishedCard";
    }

    @GetMapping("edit/{currUserId}/{name}/{id}")
    public String editEntities(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, @PathVariable("id") int id, Model model) {
        switch (name){
            case "users" -> model.addAttribute("entity", getSevice.getUserById(id));
            case "folders" -> model.addAttribute("entity", getSevice.getFolderById(id));
            case "categories" -> model.addAttribute("entity", getSevice.getCategoryById(id));
        }
        model.addAttribute("currUserId", currUserId);
        model.addAttribute("name", name);
        return "editEntity";
    }

    @PostMapping("edit/{currUserId}/{name}/{id}")
    public RedirectView editEntity(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, @PathVariable("id") int id, EntityFormModel form) {
        switch (name){
            case "users" -> updateSevice.updateUser(form);
            case "folders" -> updateSevice.updateFolder(form);
            case "categories" -> updateSevice.updateCategory(form);
        }
        return new RedirectView("/edit/" + currUserId + "/" + name + "/finished");
    }

    @GetMapping("edit/{currUserId}/{name}/finished")
    public String showEditEntityFinished(@PathVariable("currUserId") int currUserId, @PathVariable("name") String name, Model model) {
        switch (name){
            case "users" -> model.addAttribute("name", "den Nutzer");
            case "folders" -> model.addAttribute("name", "den Ordner");
            case "categories" -> model.addAttribute("name", "die Kategorie");
        }

        model.addAttribute("mode", "bearbeitet");
        model.addAttribute("currUserId", currUserId);
        model.addAttribute("name1", name);
        return "finishedEntity";
    }
}
