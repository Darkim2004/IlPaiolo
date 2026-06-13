insert into utente (id, nome, cognome, email, password, ruolo) values (1, 'Admin', 'Test', 'admin@ilpaiolo.test', '$2a$12$7hxLJMzJyjjTFyFJ8mHLJOTdR0MWEyjhDBmoK9Dd1N0Vmzg3Jztoa', 'ADMIN');
insert into utente (id, nome, cognome, email, password, ruolo) values (2, 'Mario', 'Rossi', 'mario.rossi@ilpaiolo.test', '$2a$12$GUn7KL0zKbwX1WkGjZdcBuIk8j8FevbAkX05c8UtAu9E/YtXb3IcO', 'USER');
insert into utente (id, nome, cognome, email, password, ruolo) values (3, 'Giulia', 'Bianchi', 'giulia.bianchi@ilpaiolo.test', '$2a$12$Gb54ufbAz4IAqca9332KzuWs9X8MQP6bIWsQoM/WO0gSdyzIvR/Oi', 'USER');

insert into tavolo (id, numero, nome, capienza, posizione, disponibile, attivo) values (1, 1, 'Tavolo finestra', 4, 'INTERNO', true, true);
insert into tavolo (id, numero, nome, capienza, posizione, disponibile, attivo) values (2, 2, 'Tavolo grande', 8, 'INTERNO', true, true);
insert into tavolo (id, numero, nome, capienza, posizione, disponibile, attivo) values (3, 3, 'Tavolo esterno', 6, 'ESTERNO', false, true);

insert into gioco (id, titolo, descrizione, categoria, numero_min_giocatori, numero_max_giocatori, durata_media_minuti, difficolta, stato) values (1, 'Catan', 'Classico gioco di colonizzazione, scambi e gestione risorse.', 'Strategia', 3, 4, 90, 'Media', 'DISPONIBILE');
insert into gioco (id, titolo, descrizione, categoria, numero_min_giocatori, numero_max_giocatori, durata_media_minuti, difficolta, stato) values (2, 'Dixit', 'Gioco narrativo con carte illustrate, perfetto per gruppi creativi.', 'Party game', 3, 8, 30, 'Facile', 'DISPONIBILE');
insert into gioco (id, titolo, descrizione, categoria, numero_min_giocatori, numero_max_giocatori, durata_media_minuti, difficolta, stato) values (3, 'Ticket to Ride', 'Costruisci tratte ferroviarie e completa obiettivi segreti.', 'Famiglia', 2, 5, 60, 'Facile', 'DISPONIBILE');
insert into gioco (id, titolo, descrizione, categoria, numero_min_giocatori, numero_max_giocatori, durata_media_minuti, difficolta, stato) values (4, 'Terraforming Mars', 'Sviluppa Marte con carte progetto, produzione e gestione risorse.', 'Gestionale', 1, 5, 120, 'Difficile', 'RISERVATO_EVENTO');
insert into gioco (id, titolo, descrizione, categoria, numero_min_giocatori, numero_max_giocatori, durata_media_minuti, difficolta, stato) values (5, 'Azul', 'Piazza tessere colorate e ottieni punti con pattern ordinati.', 'Astratto', 2, 4, 45, 'Media', 'NON_DISPONIBILE');
insert into gioco (id, titolo, descrizione, categoria, numero_min_giocatori, numero_max_giocatori, durata_media_minuti, difficolta, stato) values (6, 'Dobble', 'Gioco rapido di osservazione e riflessi.', 'Party game', 2, 8, 15, 'Facile', 'DANNEGGIATO');

insert into evento (id, nome, descrizione, data, ora_inizio, ora_fine, numero_max_partecipanti, stato, costo_partecipazione) values (1, 'Serata nuovi giocatori', 'Evento introduttivo con giochi semplici e spiegazione delle regole.', date '2030-06-20', time '20:30:00', time '23:30:00', 12, 'APERTO', 5.00);
insert into evento (id, nome, descrizione, data, ora_inizio, ora_fine, numero_max_partecipanti, stato, costo_partecipazione) values (2, 'Torneo strategia', 'Serata dedicata ai giochi strategici piu lunghi.', date '2030-07-05', time '19:00:00', time '23:45:00', 8, 'APERTO', 10.00);
insert into evento (id, nome, descrizione, data, ora_inizio, ora_fine, numero_max_partecipanti, stato, costo_partecipazione) values (3, 'Party game night', 'Giochi veloci per gruppi numerosi.', date '2030-07-18', time '21:00:00', time '23:30:00', 20, 'APERTO', 0.00);
insert into evento (id, nome, descrizione, data, ora_inizio, ora_fine, numero_max_partecipanti, stato, costo_partecipazione) values (4, 'Evento passato completato', 'Evento usato per testare gli stati non aperti.', date '2024-05-10', time '20:00:00', time '22:30:00', 10, 'COMPLETATO', 3.00);
insert into evento (id, nome, descrizione, data, ora_inizio, ora_fine, numero_max_partecipanti, stato, costo_partecipazione) values (5, 'Evento annullato', 'Evento usato per testare lo stato annullato.', date '2030-08-01', time '20:00:00', time '22:00:00', 10, 'ANNULLATO', null);

insert into evento_giochi (eventi_id, giochi_id) values (1, 2);
insert into evento_giochi (eventi_id, giochi_id) values (1, 3);
insert into evento_giochi (eventi_id, giochi_id) values (1, 6);
insert into evento_giochi (eventi_id, giochi_id) values (2, 1);
insert into evento_giochi (eventi_id, giochi_id) values (2, 4);
insert into evento_giochi (eventi_id, giochi_id) values (3, 2);
insert into evento_giochi (eventi_id, giochi_id) values (3, 6);
insert into evento_giochi (eventi_id, giochi_id) values (4, 5);
insert into evento_giochi (eventi_id, giochi_id) values (5, 1);

insert into iscrizione_evento (id, data_iscrizione, note, utente_id, evento_id) values (1, timestamp '2030-06-01 12:00:00', 'Prima iscrizione test.', 2, 1);
insert into iscrizione_evento (id, data_iscrizione, note, utente_id, evento_id) values (2, timestamp '2030-06-02 18:30:00', 'Vorrei provare Dixit.', 3, 1);
insert into iscrizione_evento (id, data_iscrizione, note, utente_id, evento_id) values (3, timestamp '2030-06-15 09:15:00', 'Posto per il torneo strategia.', 2, 2);

insert into prenotazione (id, data, ora_inizio, ora_fine, numero_persone, note, utente_id, tavolo_id) values (1, date '2030-06-20', time '19:30:00', time '20:30:00', 4, 'Prenotazione prima della serata introduttiva.', 2, 1);
insert into prenotazione (id, data, ora_inizio, ora_fine, numero_persone, note, utente_id, tavolo_id) values (2, date '2030-07-05', time '18:30:00', time '19:30:00', 6, 'Gruppo per il torneo.', 3, 2);

alter sequence if exists utente_seq restart with 100;
alter sequence if exists tavolo_seq restart with 100;
alter sequence if exists gioco_seq restart with 100;
alter sequence if exists evento_seq restart with 100;
alter sequence if exists iscrizione_evento_seq restart with 100;
alter sequence if exists prenotazione_seq restart with 100;
alter sequence if exists hibernate_sequence restart with 100;
