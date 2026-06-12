package it.uniroma3.siw.progetto.service;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.Gioco;
import it.uniroma3.siw.progetto.model.StatoEvento;
import it.uniroma3.siw.progetto.model.StatoGioco;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.EventoRepository;
import it.uniroma3.siw.progetto.repository.GiocoRepository;

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

    public EventoService(EventoRepository eventoRepository,GiocoRepository giocoRepository) {
        this.eventoRepository = eventoRepository;
        this.giocoRepository = giocoRepository;
    }

    @Transactional(readOnly = true)
    public List<Evento> getEventiAperti(){
        return eventoRepository.findByStatoAndDataAfterOrderByDataAsc(StatoEvento.APERTO, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));
    }

    @Transactional
    public void save(Evento evento,List<Long>giochiIds){
        if(evento.getData().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("La data dell'evento deve essere futura");
        }
        if(evento.getOraFine().isBefore(evento.getOraInizio())){
            throw new IllegalArgumentException("L'ora di fine deve essere successiva all'ora di inizio");
        }
        if(evento.getNumeroMaxPartecipanti()<=0){
            throw new IllegalArgumentException("Il numero max di partecipanti deve essere positivo");
        }
        Set<Gioco> giochi = new HashSet<>();
        if(giochiIds!=null){
            for(Long id : giochiIds){
                Optional<Gioco> optGioco = giocoRepository.findById(id);
                if(optGioco.isEmpty()){
                    throw new IllegalArgumentException("Non ho trovato il gioco");
                }
                Gioco gioco = optGioco.get();
                if(gioco.getStato()!=StatoGioco.DISPONIBILE){
                    throw new IllegalArgumentException("Il gioco non è disponibile");
                }
                giochi.add(gioco);
            }
        }
        evento.setGiochi(giochi);
        eventoRepository.save(evento);
    }
    @Transactional
    public void cambiaStato(Long eventoId, StatoEvento nuovoStato){
        Optional<Evento> optEvento = eventoRepository.findById(eventoId);
        if(optEvento.isEmpty()){
            throw new IllegalArgumentException("Evento non trovato");
        }
        Evento evento = optEvento.get();
        evento.setStato(nuovoStato);
        eventoRepository.save(evento);
    }
}
