package it.uniroma3.siw.progetto.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }
}
