package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.StatoEvento;
import it.uniroma3.siw.progetto.service.EventoService;

import it.uniroma3.siw.progetto.service.IscrizioneEventoService;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EventoController {

    private final IscrizioneEventoService iscrizioneEventoService;
    private final EventoService eventoService;

    public EventoController(EventoService eventoService, IscrizioneEventoService iscrizioneEventoService) {
        this.eventoService = eventoService;
        this.iscrizioneEventoService = iscrizioneEventoService;
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
    @PostMapping("/admin/eventi/save")
    public String save(@ModelAttribute Evento evento,@RequestParam List<Long> giochiIds) {
        eventoService.save(evento, giochiIds);
        return "redirect:/admin/eventi";
    }
    @GetMapping("/admin/eventi/{id}/iscrizioni")
    public String listaIscrizioni(@PathVariable Long id,Model model) {
        model.addAttribute("evento",eventoService.getEventoById(id));
        model.addAttribute("iscrizioni",iscrizioneEventoService.getIscrizioniByEvento(id));
        return "admin/eventi/iscrizioni";
    }
    @PostMapping("/admin/eventi/{id}/stato")
    public String cambiaStato(@PathVariable Long id,@RequestParam StatoEvento stato) {
        eventoService.cambiaStato(id, stato);
        return "redirect:/admin/eventi" + id + "/iscrizioni";
    }

    @PostMapping("/admin/iscrizioni/{id}/delete")
    public String deleteIscrizione(@PathVariable Long id,@RequestParam Long eventoId) {
        iscrizioneEventoService.deleteById(id);
        return "redirect:/admin/eventi" + eventoId + "/iscrizioni";
    }
    
    
    
    

}








