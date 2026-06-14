package it.uniroma3.siw.progetto.controller;


import it.uniroma3.siw.progetto.service.EventoService;
import it.uniroma3.siw.progetto.service.GiocoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final EventoService eventoService;
    private final GiocoService giocoService;

    public HomeController(EventoService eventoService, GiocoService giocoService) {
        this.eventoService = eventoService;
        this.giocoService = giocoService;
    }

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("eventoSettimana", eventoService.getEventoDellaSettimana().orElse(null));
        model.addAttribute("numeroGiochi", giocoService.count());
        model.addAttribute("numeroEventi", eventoService.countEventiAperti());

        return "index";
    }
}
