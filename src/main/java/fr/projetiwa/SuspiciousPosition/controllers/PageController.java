package fr.projetiwa.SuspiciousPosition.controllers;

import fr.projetiwa.SuspiciousPosition.models.SuspiciousPosition;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/person/register")
    public String mainPage (Model model) {
        model.addAttribute("person", new SuspiciousPosition());
        return "register";
    }
}
