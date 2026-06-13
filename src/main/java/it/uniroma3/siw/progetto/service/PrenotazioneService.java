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
    public List<Prenotazione> findByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    // ──────────────────────────────────────────
    // UC4 - Crea prenotazione
    // ──────────────────────────────────────────

    @Transactional
    public Prenotazione crea(Utente utente, Long tavoloId, Prenotazione dati) {
        Tavolo tavolo = tavoloRepository.findById(tavoloId)
                .orElseThrow(() -> new EntityNotFoundException("Tavolo non trovato"));

        validaPrenotazione(tavolo, dati, null);

        dati.setUtente(utente);
        dati.setTavolo(tavolo);
        return prenotazioneRepository.save(dati);
    }

    // ──────────────────────────────────────────
    // UC5 - Modifica prenotazione
    // ──────────────────────────────────────────

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

    // ──────────────────────────────────────────
    // UC6 - Elimina prenotazione
    // ──────────────────────────────────────────

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