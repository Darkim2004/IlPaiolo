package it.uniroma3.siw.progetto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.progetto.model.Gioco;
import it.uniroma3.siw.progetto.service.GiocoService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class GiocoController {
    
    private final GiocoService giocoService;

    public GiocoController(GiocoService giocoService) {
        this.giocoService = giocoService;
    }

    @GetMapping("/giochi")
    public String listaGiochi(Model model) {
        model.addAttribute("giochi", giocoService.findAll());
        return "giochi/lista";
    }

    @GetMapping("/giochi/{id}")
    public String dettaglioGioco(@PathVariable("id") Long id, Model model) {
        Gioco gioco = giocoService.findById(id);
        if (gioco == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gioco non trovato");
        }

        model.addAttribute("gioco", gioco);
        return "giochi/dettaglio";
    }

    @GetMapping("/admin/giochi/nuovo")
    public String nuovoGioco(Model model) {
        model.addAttribute("gioco", new Gioco());
        return "giochi/form";
    }

    @PostMapping("/admin/giochi")
    @Transactional
    public String aggiungiGioco(@Valid @ModelAttribute Gioco gioco, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("gioco", gioco);
            return "giochi/form";
        }

        giocoService.save(gioco);
        return "redirect:/giochi";
    }

    @GetMapping("/admin/giochi/{id}/modifica")
    public String modificaGioco(@PathVariable("id") Long id, Model model) {
        Gioco gioco = giocoService.findById(id);
        if (gioco == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gioco non trovato");
        }

        model.addAttribute("gioco", gioco);
        return "giochi/form";
    }

    @Transactional
    @PostMapping("/admin/giochi/{id}")
    public String modificaGioco(@PathVariable("id") Long id, @Valid @ModelAttribute("gioco") Gioco giocoRequestBody, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("gioco", giocoRequestBody);
            return "giochi/form";
        }

        Gioco gioco = giocoService.findById(id);
        if (gioco == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gioco non trovato");
        }
        
        giocoService.update(giocoRequestBody, gioco);
        return "redirect:/giochi";
    }

    @Transactional
    @PostMapping("/admin/giochi/{id}/elimina")
    public String eliminaGioco(@PathVariable("id") Long id) {
        Gioco gioco = giocoService.findById(id);

        if (gioco == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gioco non trovato");
        }

        giocoService.delete(gioco);
        return "redirect:/giochi";
    }
}
