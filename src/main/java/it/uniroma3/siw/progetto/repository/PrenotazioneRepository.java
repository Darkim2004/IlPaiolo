package it.uniroma3.siw.progetto.repository;

import it.uniroma3.siw.progetto.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Prenotazione;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByUtente(Utente utente);

    /**
     *
     * @param tavoloId
     * @param data
     * @param oraInizio
     * @param oraFine
     * @param escludiId
     * @return true se esiste una prenotazione che si sovrappone con i parametri forniti, false altrimenti
     *
     * Funzionamento:
     * Conta le prenotazioni dello stesso tavolo, nella stessa ora, dove gli orari si sovrappongono. Se il conteggio
     * è maggiore di 0 allora esiste una sovrapposizione.
     *
     * La condizione:
     * oraInizio < oraFine_esistente AND oraFine > oraInizio_esistente
     * copre tutti i casi di sovrapposizione.
     *
     * Il parametro escludiId serve quando si modifica una prenotazione, bisogna escludere se stessi dal controllo
     * altrimenti si auto-bloccherà per sempre. Se passi null non l'esclusione non si applica.
     */
    @Query("""
    SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
    FROM Prenotazione p
    WHERE p.tavolo.id = :tavoloId
      AND p.data = :data
      AND p.oraInizio < :oraFine
      AND p.oraFine > :oraInizio
      AND (:escludiId IS NULL OR p.id <> :escludiId)
""")
    boolean esisteSovrapposizione(
        @Param("tavoloId") Long tavoloId,
        @Param("data") LocalDate data,
        @Param("oraInizio") LocalTime oraInizio,
        @Param("oraFine") LocalTime oraFine,
        @Param("escludiId") Long escludiId
    );
}