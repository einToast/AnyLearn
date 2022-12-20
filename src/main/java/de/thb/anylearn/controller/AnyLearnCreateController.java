package de.thb.anylearn.controller;

import de.thb.anylearn.controller.form.CardFormModel;
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

    @GetMapping("create/{id}/card")
    public String createCard(@PathVariable("id") int userId, Model model) {
        model.addAttribute("folder1", deskService.getAllFolder());
        model.addAttribute("category1", deskService.getAllCategory());
        model.addAttribute("owner", deskService.getAllUser());
        model.addAttribute("userId", userId);
        return "createCard";
    }

    @PostMapping("create/{id}/card")
    public RedirectView applyCard(@PathVariable("id") int userId, CardFormModel form) {
        deskService.addCard(form);

        return new RedirectView("/create/" + userId + "/finished");
    }

    @GetMapping("create/{userId}/finished")
    public String showCreateFinished(@PathVariable("userId") int userId, Model model) {
        model.addAttribute("mode", "erstellt");
        model.addAttribute("userId", userId);
        return "cardFinished";
    }
}
