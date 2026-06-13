package it.uniroma3.siw.progetto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.Gioco;
import it.uniroma3.siw.progetto.model.StatoEvento;
import it.uniroma3.siw.progetto.model.StatoGioco;
import it.uniroma3.siw.progetto.repository.EventoRepository;
import it.uniroma3.siw.progetto.repository.GiocoRepository;

@Service
public class GiocoService {

    private final GiocoRepository giocoRepository;
    private final EventoRepository eventoRepository;

    public GiocoService(GiocoRepository giocoRepository, EventoRepository eventoRepository) {
        this.giocoRepository = giocoRepository;
        this.eventoRepository = eventoRepository;
    }

    public List<Gioco> findAll() {
        return (List<Gioco>)giocoRepository.findAll();
    }

    public int count() {
        return (int) giocoRepository.count();
    }

    public Gioco findById(Long id) {
        return giocoRepository.findById(id).orElse(null);
    }

    public void save(Gioco gioco) {
        giocoRepository.save(gioco);
    }

    public void delete(Gioco gioco) {
        // Rimuovo il gioco da tutti gli eventi a cui è associato
        for(Evento e: gioco.getEventi()) {
            e.getGiochi().remove(gioco);
        }
        
        giocoRepository.delete(gioco);
    }

    public List<Evento> findEventiByGioco(Gioco gioco) {
        if (gioco == null) {
            return List.of();
        }
        return eventoRepository.findByGiochiContaining(gioco);
    }

    public List<Gioco> findAllGiochi(){
        return giocoRepository.findByStato(StatoGioco.DISPONIBILE);
    }

    public void update(Gioco giocoRequestBody, Gioco gioco) {
        gioco.setTitolo(giocoRequestBody.getTitolo());
        gioco.setDescrizione(giocoRequestBody.getDescrizione());
        gioco.setCategoria(giocoRequestBody.getCategoria());
        gioco.setNumeroMinGiocatori(giocoRequestBody.getNumeroMinGiocatori());
        gioco.setNumeroMaxGiocatori(giocoRequestBody.getNumeroMaxGiocatori());
        gioco.setDurataMediaMinuti(giocoRequestBody.getDurataMediaMinuti());
        gioco.setDifficolta(giocoRequestBody.getDifficolta());
        gioco.setStato(giocoRequestBody.getStato());
        gioco.setImmagineUrl(giocoRequestBody.getImmagineUrl());

        giocoRepository.save(gioco);
    }
}
