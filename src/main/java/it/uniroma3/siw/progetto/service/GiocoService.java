package it.uniroma3.siw.progetto.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.GiocoRepository;

@Service
public class GiocoService {

    private final GiocoRepository giocoRepository;

    public GiocoService(GiocoRepository giocoRepository) {
        this.giocoRepository = giocoRepository;
    }
}
