package it.uniroma3.siw.progetto.controller;


import it.uniroma3.siw.progetto.service.EventoService;
import it.uniroma3.siw.progetto.service.GiocoService;
import it.uniroma3.siw.progetto.service.UtenteService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventoService eventoService;
    private final GiocoService giocoService;
    private final UtenteService utenteService;

    public HomeController(EventoService eventoService, GiocoService giocoService, UtenteService utenteService) {
        this.eventoService = eventoService;
        this.giocoService = giocoService;
        this.utenteService = utenteService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal){
        model.addAttribute("eventoSettimana", eventoService.getEventoDellaSettimana().orElse(null));
        model.addAttribute("numeroGiochi", giocoService.count());
        model.addAttribute("numeroEventi", eventoService.countEventiAperti());

        if (principal != null) {
            String email = principal.getName();
            String nomeUtente = utenteService.findByEmail(email)
                    .map(utente -> utente.getNome() != null && !utente.getNome().isBlank() ? utente.getNome() : email)
                    .orElse(email);
            model.addAttribute("nomeUtente", nomeUtente);
        }

        return "index";
    }
}
