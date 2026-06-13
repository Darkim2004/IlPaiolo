package it.uniroma3.siw.progetto.service;

import it.uniroma3.siw.progetto.model.Tavolo;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.repository.TavoloRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TavoloService {

    private final TavoloRepository tavoloRepository;

    public TavoloService(TavoloRepository tavoloRepository) {
        this.tavoloRepository = tavoloRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Tavolo> findById(Long id) {
        return tavoloRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Tavolo> findAllAttivi(){
        return tavoloRepository.findByAttivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Tavolo> findAll(){
        return this.tavoloRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Tavolo> findDisponibili(LocalDate date,
                                        LocalTime oraInizio,
                                        LocalTime oraFine,
                                        int numeroPersone){
        return this.tavoloRepository.findTavoliDisponibili(date, oraInizio, oraFine, numeroPersone);
    }

    @Transactional
    public Tavolo save(Tavolo tavolo){
        return tavoloRepository.save(tavolo);
    }


    /**
     * @param id
     * Disattiva un tavolo rendendolo non più disponibile per future prenotazioni.
     * @Darkim2004
     * TODO: metto disattiva e non cancella perché se un tavolo è stato prenotato in passato, non voglio cancellarlo dal database, ma solo renderlo non più disponibile per future prenotazioni. secondo te ci sta?
     */
    @Transactional
    public void disattiva(Long id) {
        tavoloRepository.findById(id).ifPresent(t -> {
            t.setAttivo(false);
            t.setDisponibile(false);
            tavoloRepository.save(t);
        });
    }

    /**
     *
     * @param id
     * @return true se il tavolo è stato eliminato, false se non è stato eliminato perché aveva prenotazioni associate
     *
     * Elimina un tavolo solo se non ha prenotazioni associate. Se il tavolo ha prenotazioni, non viene eliminato e viene restituito false.
     */
    @Transactional
    public boolean eliminaSeVuoto(Long id){
        return tavoloRepository.findById(id).map(t -> {
            if (t.getPrenotazioni().isEmpty()) {
                tavoloRepository.delete(t);
                return true;
            }
            return false;
        }).orElse(false);
    }
}