package es.anusky.rating_books.users.infrastructure.controller;

import es.anusky.rating_books.users.application.activation.ActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthActivateController {

    private final ActivationService activationService;

    @GetMapping("/activate")
    public String activate(@RequestParam String token, Model model) {
        activationService.activate(token);
        return "activation-success";
    }
}
