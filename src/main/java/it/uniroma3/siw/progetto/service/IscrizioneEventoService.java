package it.uniroma3.siw.progetto.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.IscrizioneEvento;
import it.uniroma3.siw.progetto.model.StatoEvento;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.EventoRepository;
import it.uniroma3.siw.progetto.repository.IscrizioneEventoRepository;
import it.uniroma3.siw.progetto.repository.UtenteRepository;
import jakarta.transaction.Transactional;

@Service
public class IscrizioneEventoService {

    private final IscrizioneEventoRepository iscrizioneEventoRepository;
    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    public IscrizioneEventoService(IscrizioneEventoRepository iscrizioneEventoRepository,
            EventoRepository eventoRepository, UtenteRepository utenteRepository) {
        this.iscrizioneEventoRepository = iscrizioneEventoRepository;
        this.eventoRepository = eventoRepository;
        this.utenteRepository = utenteRepository;
    }
    @Transactional
    public void save(Long eventoId, String username) {

        Optional<Evento> eventoOptional = eventoRepository.findById(eventoId);
        if (eventoOptional.isEmpty()) {
            throw new IllegalArgumentException("Evento non trovato");
        }
        Evento evento = eventoOptional.get();

        Optional<Utente> utenteOptional = utenteRepository.findByUsername(username);
        if (utenteOptional.isEmpty()) {
            throw new IllegalArgumentException("Utente non trovato");
        }
        Utente utente = utenteOptional.get();

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
    //ho eliminato l'iscrizione all'evento eliminando l'iscrizione perche per ora non abbiamo messo uno Stato, come c'è scritto nel caso d'uso
    @Transactional
    public void delete(Long eventoId, String username){
        Optional<Evento> opt = eventoRepository.findById(eventoId);
        if(opt.isEmpty()){
            throw new IllegalArgumentException("Evento non trovato");
        }
        Optional<Utente> optUtente = utenteRepository.findByUsername(username);
        if(optUtente.isEmpty()){
            throw new IllegalArgumentException("Utente non trovato");
        }
        Evento evento = opt.get();
        Utente utente = optUtente.get();
        Optional<IscrizioneEvento> optIscrizione = iscrizioneEventoRepository.findByUtenteAndEvento(utente, evento);
        if(optIscrizione.isEmpty()){
            throw new IllegalArgumentException("Nessuna iscrizione all'evento");
        }
        IscrizioneEvento iscrizione = optIscrizione.get();
        iscrizioneEventoRepository.delete(iscrizione);
    }
    
}
