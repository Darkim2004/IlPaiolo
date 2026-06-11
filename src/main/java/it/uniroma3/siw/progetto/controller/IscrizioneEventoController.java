package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.IscrizioneEvento;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.IscrizioneEventoRepository;
import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;

import it.uniroma3.siw.progetto.service.IscrizioneEventoService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class IscrizioneEventoController {
    private final IscrizioneEventoRepository iscrizioneEventoRepository;
    private final IscrizioneEventoService iscrizioneEventoService;

    public IscrizioneEventoController(IscrizioneEventoService iscrizioneEventoService, IscrizioneEventoRepository iscrizioneEventoRepository) {
        this.iscrizioneEventoService = iscrizioneEventoService;
        this.iscrizioneEventoRepository = iscrizioneEventoRepository;
    }
    @PostMapping("/eventi/{id}/iscriviti")
    public String save(@PathVariable Long id,Principal principal) {
        iscrizioneEventoService.save(id,principal.getName());
        return "redirect:/eventi"; //eventualmente qui posso mettere +id ma dovrei parlare con chi gestisce eventoController -->
        // --> dovrebbe creare un metodo GET evento /eventi/{id} ma vediamo
    }
    
    @PostMapping("/eventi/{id}/delete")
    public String delete(@PathVariable Long id,Principal principal) {
        iscrizioneEventoService.delete(id, principal.getName());
        return "redirect:/eventi";
    }
    
}
