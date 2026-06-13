package it.uniroma3.siw.progetto.repository;

import it.uniroma3.siw.progetto.model.StatoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.progetto.model.Evento;
import it.uniroma3.siw.progetto.model.Gioco;

import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByStatoAndDataAfterOrderByDataAsc(StatoEvento stato, LocalDate data);
    List<Evento> findByStatoAndDataBetweenOrderByDataAscOraInizioAsc(StatoEvento stato, LocalDate dataInizio, LocalDate dataFine);
    List<Evento> findByGiochiContaining(Gioco gioco);
    long countByStatoAndDataGreaterThanEqual(StatoEvento stato, LocalDate data);
}
