package it.uniroma3.siw.progetto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.progetto.model.Gioco;
import it.uniroma3.siw.progetto.model.StatoGioco;

import java.util.List;


public interface GiocoRepository extends JpaRepository<Gioco, Long> {
    List<Gioco> findByStato(StatoGioco stato);
}
