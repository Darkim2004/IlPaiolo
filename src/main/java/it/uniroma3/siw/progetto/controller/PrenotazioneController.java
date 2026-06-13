package it.uniroma3.siw.progetto.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.progetto.model.Prenotazione;
import it.uniroma3.siw.progetto.model.Tavolo;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.UtenteRepository;
import it.uniroma3.siw.progetto.service.PrenotazioneService;
import it.uniroma3.siw.progetto.service.TavoloService;
import it.uniroma3.siw.progetto.exception.EntityNotFoundException;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;
    private final TavoloService tavoloService;
    private final UtenteRepository utenteRepository;

    public PrenotazioneController(PrenotazioneService prenotazioneService,
                                  TavoloService tavoloService,
                                  UtenteRepository utenteRepository) {
        this.prenotazioneService = prenotazioneService;
        this.tavoloService = tavoloService;
        this.utenteRepository = utenteRepository;
    }

    // Recupera l'utente loggato dal database tramite email (username di Spring Security)
    private Utente getUtenteLoggato(Principal principal) {
        return utenteRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }

    @GetMapping
    public String index() {
        return "redirect:/prenotazioni/mie";
    }

    // ──────────────────────────────────────────
    // Le mie prenotazioni
    // ──────────────────────────────────────────

    @GetMapping("/mie")
    public String miePrenotazioni(Principal principal, Model model) {
        Utente utente = getUtenteLoggato(principal);
        model.addAttribute("prenotazioni", prenotazioneService.findByUtente(utente));
        return "prenotazioni/lista";
    }

    // ──────────────────────────────────────────
    // UC4 - Nuova prenotazione
    // ──────────────────────────────────────────

    @GetMapping("/nuova")
    public String formNuovaPrenotazione() {
        return "prenotazioni/form";
    }

    @PostMapping("/nuova/cerca")
    public String cercaTavoli(@RequestParam LocalDate data,
                              @RequestParam LocalTime oraInizio,
                              @RequestParam LocalTime oraFine,
                              @RequestParam int numeroPersone,
                              Model model) {
        List<Tavolo> tavoli = tavoloService.findDisponibili(data, oraInizio, oraFine, numeroPersone);
        model.addAttribute("tavoli", tavoli);
        model.addAttribute("data", data);
        model.addAttribute("oraInizio", oraInizio);
        model.addAttribute("oraFine", oraFine);
        model.addAttribute("numeroPersone", numeroPersone);
        return "prenotazioni/cerca";
    }

    @PostMapping("/nuova")
    public String creaPrenotazione(@RequestParam Long tavoloId,
                                   @RequestParam LocalDate data,
                                   @RequestParam LocalTime oraInizio,
                                   @RequestParam LocalTime oraFine,
                                   @RequestParam int numeroPersone,
                                   @RequestParam(required = false) String note,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        Utente utente = getUtenteLoggato(principal);
        Prenotazione dati = new Prenotazione();
        dati.setData(data);
        dati.setOraInizio(oraInizio);
        dati.setOraFine(oraFine);
        dati.setNumeroPersone(numeroPersone);
        dati.setNote(note);
        prenotazioneService.crea(utente, tavoloId, dati);
        redirectAttributes.addFlashAttribute("successo", "Prenotazione effettuata con successo.");
        return "redirect:/prenotazioni/mie";
    }

    // ──────────────────────────────────────────
    // UC5 - Modifica prenotazione
    // ──────────────────────────────────────────

    @GetMapping("/{id}/modifica")
    public String formModifica(@PathVariable Long id, Principal principal, Model model) {
        Utente utente = getUtenteLoggato(principal);
        Prenotazione prenotazione = prenotazioneService.findById(id);

        // Controllo ownership già nel service, ma verifichiamo prima di mostrare il form
        if (!prenotazione.getUtente().getId().equals(utente.getId())) {
            return "redirect:/prenotazioni/mie";
        }

        model.addAttribute("prenotazione", prenotazione);
        return "prenotazioni/form-modifica";
    }

    @PostMapping("/{id}/modifica")
    public String modificaPrenotazione(@PathVariable Long id,
                                       @ModelAttribute Prenotazione dati,
                                       Principal principal,
                                       RedirectAttributes redirectAttributes) {
        Utente utente = getUtenteLoggato(principal);
        prenotazioneService.modifica(id, utente, dati);
        redirectAttributes.addFlashAttribute("successo", "Prenotazione modificata con successo.");
        return "redirect:/prenotazioni/mie";
    }

    // ──────────────────────────────────────────
    // UC6 - Elimina prenotazione
    // ──────────────────────────────────────────

    @PostMapping("/{id}/elimina")
    public String eliminaPrenotazione(@PathVariable Long id,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        Utente utente = getUtenteLoggato(principal);
        prenotazioneService.elimina(id, utente);
        redirectAttributes.addFlashAttribute("successo", "Prenotazione annullata.");
        return "redirect:/prenotazioni/mie";
    }

    // ──────────────────────────────────────────
    // API REST per React (UC4 - tavoli disponibili)
    // ──────────────────────────────────────────

    @GetMapping("/api/tavoli-disponibili")
    @ResponseBody
    public List<Tavolo> tavoliDisponibili(@RequestParam String data,
                                          @RequestParam String oraInizio,
                                          @RequestParam String oraFine,
                                          @RequestParam int numeroPersone) {
        return tavoloService.findDisponibili(
                java.time.LocalDate.parse(data),
                java.time.LocalTime.parse(oraInizio),
                java.time.LocalTime.parse(oraFine),
                numeroPersone
        );
    }
}