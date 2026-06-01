package it.uniroma3.siw.progetto.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }
}
