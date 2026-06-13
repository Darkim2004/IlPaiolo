package it.uniroma3.siw.progetto.service;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.Gioco;
import it.uniroma3.siw.progetto.model.IscrizioneEvento;
import it.uniroma3.siw.progetto.model.StatoEvento;
import it.uniroma3.siw.progetto.model.StatoGioco;
import it.uniroma3.siw.progetto.model.Utente;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.EventoRepository;
import it.uniroma3.siw.progetto.repository.GiocoRepository;
import it.uniroma3.siw.progetto.repository.IscrizioneEventoRepository;
import it.uniroma3.siw.progetto.repository.UtenteRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final GiocoRepository giocoRepository;
    private final IscrizioneEventoRepository iscrizioneEventoRepository;
    private final UtenteRepository utenteRepository;

    public EventoService(EventoRepository eventoRepository, GiocoRepository giocoRepository,
            IscrizioneEventoRepository iscrizioneEventoRepository, UtenteRepository utenteRepository) {
        this.eventoRepository = eventoRepository;
        this.giocoRepository = giocoRepository;
        this.iscrizioneEventoRepository = iscrizioneEventoRepository;
        this.utenteRepository = utenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Evento> getEventiAperti() {
        return eventoRepository.findByStatoAndDataAfterOrderByDataAsc(StatoEvento.APERTO, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Evento getEventoById(Long id) {
        Optional<Evento> optEvento = eventoRepository.findById(id);
        if (optEvento.isEmpty()) {
            throw new EntityNotFoundException("Evento non trovato con id: " + id);
        }
        return optEvento.get();
    }

    @Transactional
    public void save(Evento evento, List<Long> giochiIds) {

        if (evento.getData().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La data dell'evento deve essere futura");
        }
        if (evento.getOraFine().isBefore(evento.getOraInizio())) {
            throw new IllegalArgumentException("L'ora di fine deve essere successiva all'ora di inizio");
        }
        if (evento.getNumeroMaxPartecipanti() <= 0) {
            throw new IllegalArgumentException("Il numero max di partecipanti deve essere positivo");
        }

        Set<Gioco> giochi = new HashSet<>();
        if (giochiIds != null) {
            for (Long id : giochiIds) {
                Optional<Gioco> optGioco = giocoRepository.findById(id);
                if (optGioco.isEmpty()) {
                    throw new IllegalArgumentException("Gioco non trovato");
                }
                Gioco gioco = optGioco.get();
                if (gioco.getStato() != StatoGioco.DISPONIBILE) {
                    throw new IllegalArgumentException("Il gioco " + gioco.getTitolo() + " non è disponibile");
                }
                giochi.add(gioco);
            }
        }
        evento.setGiochi(giochi);
        eventoRepository.save(evento);
    }

    @Transactional
    public void iscriviti(Long eventoId, String email) {

        Optional<Evento> optEvento = eventoRepository.findById(eventoId);
        if (optEvento.isEmpty()) {
            throw new IllegalArgumentException("Evento non trovato");
        }
        Evento evento = optEvento.get();

        Optional<Utente> optUtente = utenteRepository.findByEmail(email);
        if (optUtente.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        Utente utente = optUtente.get();

        if (evento.getStato() != StatoEvento.APERTO) {
            throw new IllegalStateException("L'evento non è aperto alle iscrizioni");
        }
        if (iscrizioneEventoRepository.existsByUtenteAndEvento(utente, evento)) {
            throw new IllegalStateException("Sei già iscritto a questo evento");
        }
        int iscritti = iscrizioneEventoRepository.countByEvento(evento);
        if (iscritti >= evento.getNumeroMaxPartecipanti()) {
            throw new IllegalStateException("L'evento ha raggiunto il numero massimo di partecipanti");
        }

        IscrizioneEvento iscrizione = new IscrizioneEvento(utente, evento);
        iscrizioneEventoRepository.save(iscrizione);
    }

    @Transactional
    public void annullaIscrizione(Long eventoId, String email) {

        Optional<Evento> optEvento = eventoRepository.findById(eventoId);
        if (optEvento.isEmpty()) {
            throw new IllegalArgumentException("Evento non trovato");
        }
        Evento evento = optEvento.get();

        Optional<Utente> optUtente = utenteRepository.findByEmail(email);
        if (optUtente.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        Utente utente = optUtente.get();

        Optional<IscrizioneEvento> optIscrizione = iscrizioneEventoRepository.findByUtenteAndEvento(utente, evento);
        if (optIscrizione.isEmpty()) {
            throw new IllegalArgumentException("Nessuna iscrizione trovata");
        }
        iscrizioneEventoRepository.delete(optIscrizione.get());
    }

    // questo serve solo per non usare nel controller il repository
    @Transactional(readOnly = true)
    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<IscrizioneEvento> getIscrizioniByEvento(Long eventoId) {
        Optional<Evento> optEvento = eventoRepository.findById(eventoId);
        if (optEvento.isEmpty()) {
            throw new IllegalArgumentException("Evento non trovato");
        }
        return iscrizioneEventoRepository.findByEvento(optEvento.get());
    }

    @Transactional
    public void deleteIscrizione(Long iscrizioneId) {
        if (!iscrizioneEventoRepository.existsById(iscrizioneId)) {
            throw new IllegalArgumentException("Iscrizione non trovata");
        }
        iscrizioneEventoRepository.deleteById(iscrizioneId);
    }

    @Transactional
    public void cambiaStato(Long eventoId, StatoEvento nuovoStato) {
        Optional<Evento> optEvento = eventoRepository.findById(eventoId);
        if (optEvento.isEmpty()) {
            throw new IllegalArgumentException("Evento non trovato");
        }
        optEvento.get().setStato(nuovoStato);
        eventoRepository.save(optEvento.get());
    }
}