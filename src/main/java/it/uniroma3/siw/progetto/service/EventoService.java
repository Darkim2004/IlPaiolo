package it.uniroma3.siw.progetto.service;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.StatoEvento;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.EventoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Transactional(readOnly = true)
    public List<Evento> getEventiAperti(){
        return eventoRepository.findByStatoAndDataAfterOrderByDataAsc(StatoEvento.APERTO, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Evento non trovato con id: " + id));
    }
}
