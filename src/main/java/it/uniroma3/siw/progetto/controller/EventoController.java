package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.StatoEvento;
import it.uniroma3.siw.progetto.service.EventoService;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/eventi")
    public String listaEventi(Model model) {
        model.addAttribute("eventi", eventoService.getEventiAperti());
        return "eventi/lista";
    }

    @GetMapping("/eventi/{id}")
    public String dettaglioEvento(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoService.getEventoById(id));
        return "eventi/dettaglio";
    }

    @PostMapping("/eventi/{id}/iscriviti")
    public String iscriviti(@PathVariable Long id, Principal principal) {
        eventoService.iscriviti(id, principal.getName());
        return "redirect:/eventi";
    }

    @PostMapping("/eventi/{id}/annulla")
    public String annullaIscrizione(@PathVariable Long id, Principal principal) {
        eventoService.annullaIscrizione(id, principal.getName());
        return "redirect:/eventi";
    }

    @PostMapping("/admin/eventi/save")
    public String save(@ModelAttribute Evento evento, @RequestParam(required = false) List<Long> giochiIds) {
        eventoService.save(evento, giochiIds);
        return "redirect:/admin/eventi";
    }

    @GetMapping("/admin/eventi")
    public String listaEventiAdmin(Model model) {
        model.addAttribute("eventi", eventoService.getAllEventi());
        return "admin/eventi/lista";
    }

    @GetMapping("/admin/eventi/{id}/iscrizioni")
    public String listaIscrizioni(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoService.getEventoById(id));
        model.addAttribute("iscrizioni", eventoService.getIscrizioniByEvento(id));
        return "admin/eventi/iscrizioni";
    }

    @PostMapping("/admin/eventi/{id}/stato")
    public String cambiaStato(@PathVariable Long id, @RequestParam StatoEvento stato) {
        eventoService.cambiaStato(id, stato);
        return "redirect:/admin/eventi/" + id + "/iscrizioni";
    }

    @PostMapping("/admin/iscrizioni/{id}/delete")
    public String deleteIscrizione(@PathVariable Long id, @RequestParam Long eventoId) {
        eventoService.deleteIscrizione(id);
        return "redirect:/admin/eventi/" + eventoId + "/iscrizioni";
    }
}








