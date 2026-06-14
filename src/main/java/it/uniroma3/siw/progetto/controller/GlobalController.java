package it.uniroma3.siw.progetto.controller;

import it.uniroma3.siw.progetto.service.UtenteService;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Per far modo che i dati dell'utente autenticato siano sempre disponibili ai template possiamo
 * definire una classe con annotazione @ControllerAdvice che espone attributi globali del model.
 * In questo modo, nei template Thymeleaf,
 * possiamo accedere all'oggetto userDetails e al nome visualizzato nell'header.
 */
@ControllerAdvice
public class GlobalController {

	private final UtenteService utenteService;

	public GlobalController(UtenteService utenteService) {
		this.utenteService = utenteService;
	}

	@ModelAttribute("userDetails")
	public UserDetails getUser() {
		return getAuthenticatedUserDetails();
	}

	@ModelAttribute("nomeUtente")
	public String getNomeUtente() {
		UserDetails userDetails = getAuthenticatedUserDetails();
		if (userDetails == null) {
			return null;
		}

		String email = userDetails.getUsername();
		return this.utenteService.findByEmail(email)
				.map(utente -> utente.getNome() != null && !utente.getNome().isBlank() ? utente.getNome() : email)
				.orElse(email);
	}

	private UserDetails getAuthenticatedUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails userDetails) {
			return userDetails;
		}

		return null;
	}
}
