package it.uniroma3.siw.progetto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.progetto.model.Tavolo;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TavoloRepository extends JpaRepository<Tavolo, Long> {
    List<Tavolo> findByAttivoTrue();

    @Query("""
        SELECT t FROM Tavolo t
        WHERE t.attivo = true
          AND t.disponibile = true
          AND t.capienza >= :numeroPersone
          AND NOT EXISTS (
              SELECT p FROM Prenotazione p
              WHERE p.tavolo = t
                AND p.data = :data
                AND p.oraInizio < :oraFine
                AND p.oraFine > :oraInizio
          )
        ORDER BY t.numero ASC
    """)
    List<Tavolo> findTavoliDisponibili(
            @Param("data")LocalDate data,
            @Param("oraInizio") LocalTime oraInizio,
            @Param("oraFine")LocalTime oraFine,
            @Param("numeroPersone")int numeroPersone
    );
}
