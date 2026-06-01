package it.uniroma3.siw.progetto.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.IscrizioneEventoRepository;

@Service
public class IscrizioneEventoService {

    private final IscrizioneEventoRepository iscrizioneEventoRepository;

    public IscrizioneEventoService(IscrizioneEventoRepository iscrizioneEventoRepository) {
        this.iscrizioneEventoRepository = iscrizioneEventoRepository;
    }
}
