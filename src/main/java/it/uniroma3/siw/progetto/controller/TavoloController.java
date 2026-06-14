package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.exception.EntityNotFoundException;
import it.uniroma3.siw.progetto.model.Tavolo;
import it.uniroma3.siw.progetto.service.TavoloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Classe controller per i tavoli.
 * Gestisce la creazione, modifica, disattivazione e cancellazione di tavoli.
 * Queste operazioni sono consentite solo agli amministratori, quindi tutte le rotte sono prefissate da /admin/tavoli.
 */
@Controller
@RequestMapping("admin/tavoli")
public class TavoloController {

    private final TavoloService tavoloService;

    public TavoloController(TavoloService tavoloService) {
        this.tavoloService = tavoloService;
    }

    @GetMapping
    public String lista(Model model){
        model.addAttribute("tavoli", tavoloService.findAllWithPrenotazioni());
        return "admin/tavoli/lista";
    }

    @GetMapping("/nuovo")
    public String createForm(Model model){
        model.addAttribute("tavolo", new Tavolo());
        return "admin/tavoli/form";
    }

    /**
     *
     * @param tavolo
     * @param redirectAttributes
     * @return redirect alla lista dei tavoli
     *
     * Gestisce la creazione i un nuovo tavolo.
     * Riceve i dati del form tramite @ModelAttribute che popola automaticamente un oggetto Tavolo con i campi
     * inviati dalla richiesta HTTP.
     *
     * Dopo il salvataggio una RedirectAttributes epr passare un messaggio di successo attraverso il
     * redirect.
     * Il messaggio sopravvive alla nuova richiesta HTTP GET grazie alla sessione e poi viene automaticamente
     * cancellato.
     */
    @PostMapping("/nuovo")
    public String save(@ModelAttribute Tavolo tavolo,
                       RedirectAttributes redirectAttributes){
        tavoloService.save(tavolo);
        redirectAttributes.addFlashAttribute("successMessage", "Tavolo creato con successo!");
        return "redirect:/admin/tavoli";
    }

    @GetMapping("/{id}/modifica")
    public String edit(@PathVariable("id") Long id, Model model){
        Tavolo tavolo = tavoloService.findById(id).orElseThrow(() -> new IllegalArgumentException("Tavolo non trovato"));
        model.addAttribute("tavolo", tavolo);
        return "admin/tavoli/edit";
    }


    @PostMapping("/{id}/modifica")
    public String saveEdit(@PathVariable("id") Long id,
                           @ModelAttribute Tavolo dati,
                           RedirectAttributes redirectAttributes){
        Tavolo tavolo = tavoloService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tavolo non trovato"));
        tavolo.setNumero(dati.getNumero());
        tavolo.setNome(dati.getNome());
        tavolo.setCapienza(dati.getCapienza());
        tavolo.setPosizione(dati.getPosizione());
        tavolo.setDisponibile(dati.isDisponibile());
        tavoloService.save(tavolo);

        redirectAttributes.addFlashAttribute("successMessage", "Tavolo modificato con successo!");
        return "redirect:/admin/tavoli";
    }

    /**
     *
     * @param id
     * @param redirectAttributes
     * @return redirect per lista dei tavoli
     *
     * Se un tavolo ha delle prenotazioni attive non può essere eliminato, ma può essere disattivato.
     * Disattivando un tavolo questo non sarà più disponibile per future prenotazioni.
     */
    @PostMapping("/{id}/disattiva")
    public String disattiva(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        tavoloService.disattiva(id);
        redirectAttributes.addFlashAttribute("successo", "Tavolo disattivato.");
        return "redirect:/admin/tavoli";
    }


    /**
     *
     * @param id
     * @param redirectAttributes
     * @return redirect per lista dei tavoli
     *
     * Un tavolo per poter essere cancellato non deve avere prenotazioni attive
     */
    @PostMapping("/{id}/elimina")
    public String elimina(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        boolean eliminato = tavoloService.eliminaSeVuoto(id);
        if (eliminato) {
            redirectAttributes.addFlashAttribute("successo", "Tavolo eliminato.");
        } else {
            redirectAttributes.addFlashAttribute("errore",
                    "Impossibile eliminare il tavolo: ha prenotazioni associate. Puoi disattivarlo.");
        }
        return "redirect:/admin/tavoli";
    }


    @PostMapping("/{id}/attiva")
    public String attiva(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        tavoloService.attiva(id);
        redirectAttributes.addFlashAttribute("successo", "Tavolo attivato.");
        return "redirect:/admin/tavoli";
    }
}
