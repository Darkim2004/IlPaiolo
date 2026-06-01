package it.uniroma3.siw.progetto.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Evento;

public interface EventoRepository extends CrudRepository<Evento, Long> {
}
