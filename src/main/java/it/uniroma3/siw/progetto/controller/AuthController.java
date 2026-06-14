package it.uniroma3.siw.progetto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.UtenteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("utente", new Utente());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("utente") Utente utente, 
                                BindingResult bindingResult, HttpServletRequest request) 
                                throws ServletException {
        if (utente.getEmail() != null && !utente.getEmail().isBlank()
            && this.utenteService.findByEmail(utente.getEmail()).isPresent()) {
                bindingResult.rejectValue("email", "duplicate",
                                        "Email gia in uso");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        String rawPassword = utente.getPassword();
        this.utenteService.save(utente);
        if (request.getUserPrincipal() != null) {
            request.logout();
        }
        request.login(utente.getEmail(), rawPassword);
        return "redirect:/";
    }

}
