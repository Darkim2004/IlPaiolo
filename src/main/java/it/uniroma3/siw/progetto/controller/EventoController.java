package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/eventi")
    public String listaEventi(Model model){
        model.addAttribute("eventi", eventoService.getEventiAperti());
        return "eventi/lista";
    }

    @GetMapping("/eventi/{id}")
    public String dettaglioEvento(@PathVariable("id") Long id, Model model){
        model.addAttribute("evento", eventoService.getEventoById(id));
        return "eventi/dettaglio";
    }

}








