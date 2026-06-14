package it.uniroma3.siw.progetto.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.UtenteService;
import jakarta.servlet.http.HttpServletRequest;

class AuthControllerTests {

	@Test
	void postRegisterLogsOutPreviousUserBeforeLoggingInNewUser() throws Exception {
		UtenteService utenteService = mock(UtenteService.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		AuthController controller = new AuthController(utenteService);
		Utente utente = new Utente();
		utente.setEmail("boh@gmail.com");
		utente.setPassword("password");
		BindingResult bindingResult = new BeanPropertyBindingResult(utente, "utente");

		when(utenteService.findByEmail("boh@gmail.com")).thenReturn(Optional.empty());
		when(request.getUserPrincipal()).thenReturn((Principal) () -> "admin@ilpaiolo.test");

		String viewName = controller.postRegister(utente, bindingResult, request);

		assertThat(viewName).isEqualTo("redirect:/");
		verify(utenteService).save(utente);
		InOrder requestCalls = inOrder(request);
		requestCalls.verify(request).logout();
		requestCalls.verify(request).login("boh@gmail.com", "password");
	}
}
