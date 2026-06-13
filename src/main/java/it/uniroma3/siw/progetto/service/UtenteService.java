package it.uniroma3.siw.progetto.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.UtenteRepository;

import java.util.Optional;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(Utente utente) {
        utente.setPassword(this.passwordEncoder.encode(utente.getPassword()));
        this.utenteRepository.save(utente);
    }

    public Optional<Utente> findByEmail(String email) {
        return this.utenteRepository.findByEmail(email);
    }
}
