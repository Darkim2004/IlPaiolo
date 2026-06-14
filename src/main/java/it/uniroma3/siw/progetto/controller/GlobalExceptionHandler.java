package it.uniroma3.siw.progetto.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import it.uniroma3.siw.progetto.exception.EntityNotFoundException;

/**
 * Gestore globale delle eccezioni dell'applicazione.
 *
 * In Spring MVC, quando un'eccezione non viene catturata da un controller,
 * risale lo stack fino a raggiungere questa classe, che decide come rispondere
 * all'utente senza che ogni controller debba gestirla individualmente.
 *
 * @ControllerAdvice trasforma questa classe in un componente trasversale:
 * intercetta le eccezioni lanciate da qualsiasi controller dell'applicazione
 * e le gestisce in un unico punto. Questo evita la duplicazione di blocchi
 * try-catch nei controller e mantiene la logica di gestione degli errori
 * separata dalla logica di business.
 *
 * Ogni metodo annotato con @ExceptionHandler è associato a uno o più tipi
 * di eccezione. Quando viene lanciata un'eccezione di quel tipo, Spring
 * invoca il metodo corrispondente e usa il ModelAndView restituito per
 * visualizzare la pagina di errore appropriata.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce il caso in cui una risorsa non esiste nel database.
     * Lanciata dal service quando findById non trova l'entità richiesta.
     * Restituisce la pagina 404 con il messaggio dell'eccezione.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound(EntityNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("messaggio", ex.getMessage());
        return mav;
    }

    /**
     * Gestisce errori causati da input non validi o da uno stato del sistema
     * incompatibile con l'operazione richiesta.
     *
     * IllegalArgumentException: i dati forniti dall'utente non sono validi
     * (es. Ora di fine precedente all'ora di inizio, data passata,
     * numero di persone superiore alla capienza del tavolo).
     *
     * IllegalStateException: i dati sono formalmente validi ma il sistema
     * non può eseguire l'operazione nello stato attuale
     * (es. Tavolo già prenotato in quella fascia oraria, tavolo disattivato).
     *
     * Entrambe restituiscono la pagina 400 con il messaggio dell'eccezione.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ModelAndView handleBadRequest(RuntimeException ex) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("messaggio", ex.getMessage());
        return mav;
    }

    /**
     * Gestisce i tentativi di accesso non autorizzato a risorse altrui.
     * Lanciata dal service quando un utente tenta di modificare o eliminare
     * una prenotazione che non gli appartiene.
     * Restituisce la pagina 403 con il messaggio dell'eccezione.
     */
    @ExceptionHandler(SecurityException.class)
    public ModelAndView handleForbidden(SecurityException ex) {
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("messaggio", ex.getMessage());
        return mav;
    }
}