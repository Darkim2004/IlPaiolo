package it.uniroma3.siw.progetto.authentication;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userdetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(this.dataSource);
        manager.setUsersByUsernameQuery("SELECT email, password, 1 as enabled FROM utente WHERE email = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT email, CONCAT('ROLE_', ruolo) FROM utente WHERE email = ?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(HttpMethod.GET, "/", "/index", "/css/*", "/images/*",
                "/giochi/**", "/eventi/**", "/login", "/register"
            ).permitAll();
            auth.requestMatchers(HttpMethod.POST, "/login", "/register").permitAll();
            // TODO: Aggiungere i vari permessi (Matteo gay)
            // auth.requestMatchers().authenticated();
            auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.anyRequest().authenticated();
        });
        http.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.usernameParameter("email");
            form.loginProcessingUrl("/perform-login");
            form.failureUrl("/login?error=true");
        });
        http.logout(logout -> {
            logout.logoutUrl("/logout"); // endpoint trigger for logout
            logout.invalidateHttpSession(true); // invalidate http session
            logout.deleteCookies("JSESSIONID"); // delete session cookie
            logout.clearAuthentication(true); // removes the authentication from the security context
            logout.permitAll(); // allow all to access logout
        });
        return http.build();
    }
}