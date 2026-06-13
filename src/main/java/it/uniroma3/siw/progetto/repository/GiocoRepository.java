package it.uniroma3.siw.progetto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.progetto.model.Gioco;

public interface GiocoRepository extends JpaRepository<Gioco, Long> {
}
