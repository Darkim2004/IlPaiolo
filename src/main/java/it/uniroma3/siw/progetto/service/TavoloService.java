package it.uniroma3.siw.progetto.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.TavoloRepository;

@Service
public class TavoloService {

    private final TavoloRepository tavoloRepository;

    public TavoloService(TavoloRepository tavoloRepository) {
        this.tavoloRepository = tavoloRepository;
    }
}
