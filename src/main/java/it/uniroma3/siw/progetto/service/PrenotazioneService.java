package it.uniroma3.siw.progetto.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }
}
