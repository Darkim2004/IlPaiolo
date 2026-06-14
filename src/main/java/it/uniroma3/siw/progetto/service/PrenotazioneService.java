package it.uniroma3.siw.progetto.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Prenotazione;
import it.uniroma3.siw.progetto.model.Tavolo;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.PrenotazioneRepository;
import it.uniroma3.siw.progetto.repository.TavoloRepository;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final TavoloRepository tavoloRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository,
                               TavoloRepository tavoloRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.tavoloRepository = tavoloRepository;
    }
    @Transactional(readOnly = true)
    public Prenotazione findById(Long id) {
        return prenotazioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));
    }

    @Transactional(readOnly = true)
    public List<Prenotazione> findAll() {
        return prenotazioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Prenotazione> findByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }


    /**
     *
     * @param utente -> l'utente che vuole creare la prenotazione
     * @param tavoloId -> su quale tavolo vuole prenotare
     * @param dati -> i dati della prenotazione.
     *
     * Un utente può creare una prenotazione solo se la prenotazione è valida (vedi validaPrenotazione)
     */
    @Transactional
    public void crea(Utente utente, Long tavoloId, Prenotazione dati) {
        Tavolo tavolo = tavoloRepository.findById(tavoloId)
                .orElseThrow(() -> new EntityNotFoundException("Tavolo non trovato"));

        validaPrenotazione(tavolo, dati, null);

        dati.setUtente(utente);
        dati.setTavolo(tavolo);
        prenotazioneRepository.save(dati);
    }

    /**
     *
     * @param prenotazioneId -> id della prenotazione da modificare
     * @param utente -> utente che vuole modificare la propria prenotazione
     * @param dati -> i nuovi dati della prenotazione (data, ora inizio, ora fine, numero persone, note)
     * @return -> la prenotazione modificata
     * Un utente può modificare le prenotazioni solo se:
     * - è il proprietario della prenotazione (non puoi modificare una prenotazione altrui)
     * - la prenotazione è futura (non puoi modificare una prenotazione passata o in corso)
     * - le modifiche che vuole apportare sono valide (vedi validaPrenotazione).
     */
    @Transactional
    public Prenotazione modifica(Long prenotazioneId, Utente utente, Prenotazione dati) {
        Prenotazione esistente = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

        if (!esistente.getUtente().getId().equals(utente.getId()))
            throw new SecurityException("Non puoi modificare una prenotazione altrui");

        if (!esistente.getData().isAfter(LocalDate.now()))
            throw new IllegalStateException("Puoi modificare solo prenotazioni future");

        validaPrenotazione(esistente.getTavolo(), dati, prenotazioneId);

        esistente.setData(dati.getData());
        esistente.setOraInizio(dati.getOraInizio());
        esistente.setOraFine(dati.getOraFine());
        esistente.setNumeroPersone(dati.getNumeroPersone());
        esistente.setNote(dati.getNote());

        return prenotazioneRepository.save(esistente);
    }

    /**
     *
     * @param prenotazioneId -> l'id della prenotazione da eliminare
     * @param utente -> l'utente che vuole eliminare la prenotazione
     * NOTA: un utente USER può eliminare solo le proprie prenotazioni,
     *      e può eliminare solamente le prenotazioni future (non puoi annullare una prenotazione passata o in corso)
     *
     */
    @Transactional
    public void elimina(Long prenotazioneId, Utente utente) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));

        if (!prenotazione.getUtente().getId().equals(utente.getId()))
            throw new SecurityException("Non puoi eliminare una prenotazione altrui");

        if (!prenotazione.getData().isAfter(LocalDate.now()))
            throw new IllegalStateException("Puoi annullare solo prenotazioni future");

        prenotazioneRepository.delete(prenotazione);
    }

    /**
     *
     * @param prenotazioneId -> L'id prenotazione da eliminare
     *
     * Un utente ADMIN può eliminare tutte le prenotazioni (presenti, passate e future) di qualsiasi utente
     */
    @Transactional
    public void eliminaAdmin(Long prenotazioneId){
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovata"));
        prenotazioneRepository.delete((prenotazione));
    }


    /**
     *
     * @param tavolo -> il tavolo che vuoi prenotare
     * @param dati -> i dati della prenotazione che vuoi creare/modificare
     * @param escludiId -> un l'id di un tavolo che vorresti escludere dal check (per esempio se vuoi modificare un tavolo
     *                  già esistente non devi considerare la sovrapposizione con te stesso)
     * Una prenotazione è valida se:
     *  - Il tavolo è sia disponibile che attivo.
     *  - La prenotazione avviene in una data presente o futura
     *  - L'orario di ingresso non deve essere superiore a quella dell'uscita
     *  - Il numero di persone non deve superare la capienza del tavolo
     *  - Non deve esistere una prenotazione sovrapposta per lo stesso tavolo (escludendo eventualmente una prenotazione già esistente se stiamo modificando una prenotazione)
     */
    private void validaPrenotazione(Tavolo tavolo, Prenotazione dati, Long escludiId) {
        if (!tavolo.isAttivo() || !tavolo.isDisponibile())
            throw new IllegalStateException("Il tavolo non è disponibile");

        if (dati.getData().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("La data deve essere oggi o futura");

        if (!dati.getOraFine().isAfter(dati.getOraInizio()))
            throw new IllegalArgumentException("L'ora di fine deve essere successiva all'ora di inizio");

        if (dati.getNumeroPersone() > tavolo.getCapienza())
            throw new IllegalArgumentException(
                    "Il tavolo ha capienza " + tavolo.getCapienza() +
                            ", richieste " + dati.getNumeroPersone() + " persone");

        if (prenotazioneRepository.esisteSovrapposizione(
                tavolo.getId(), dati.getData(),
                dati.getOraInizio(), dati.getOraFine(), escludiId))
            throw new IllegalStateException("Il tavolo è già prenotato in questa fascia oraria");
    }
}