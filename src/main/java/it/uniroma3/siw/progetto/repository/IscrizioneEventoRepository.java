package it.uniroma3.siw.progetto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.IscrizioneEvento;
import it.uniroma3.siw.progetto.model.Utente;

public interface IscrizioneEventoRepository extends JpaRepository<IscrizioneEvento, Long> {
    boolean existsByUtenteAndEvento(Utente utente, Evento evento);
    int countByEvento(Evento evento);
}
