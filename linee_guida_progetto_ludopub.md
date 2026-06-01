# Linee guida progetto personale SIW  
# Sistema informativo per la gestione di un Ludopub

## 1. Obiettivo del progetto

Il progetto consiste nella realizzazione di un piccolo sistema informativo web per la gestione di un **ludopub**, cioè un locale in cui i clienti possono prenotare tavoli, consultare un catalogo di giochi da tavolo e partecipare a eventi ludici organizzati dal locale.

Il sistema deve permettere a:

- utenti non autenticati di consultare informazioni pubbliche;
- utenti registrati di effettuare prenotazioni e iscriversi agli eventi;
- amministratori di gestire tavoli, giochi, eventi e prenotazioni.

Il sistema deve essere implementato utilizzando:

- **Spring Boot** per il backend;
- **JPA / Hibernate** per la persistenza;
- **PostgreSQL** o altro RDBMS;
- **Thymeleaf** per parte del frontend;
- **React** per almeno una parte del frontend.

L'obiettivo principale non è solo creare operazioni CRUD, ma progettare un sistema con:

- architettura a livelli;
- separazione delle responsabilità;
- gestione corretta delle transazioni;
- controllo dell'accesso tramite ruoli;
- accesso efficiente ai dati;
- logica di business realistica.

---

## 2. Scenario applicativo

Il sistema gestisce le attività principali di un ludopub.

Un cliente può visitare il sito per consultare il catalogo dei giochi disponibili, vedere gli eventi organizzati dal locale e controllare le informazioni principali del pub.

Dopo la registrazione e il login, il cliente può:

- prenotare un tavolo per una certa data e fascia oraria;
- modificare o annullare le proprie prenotazioni;
- iscriversi a eventi ludici;
- visualizzare le proprie prenotazioni e iscrizioni.

L'amministratore può:

- inserire, modificare ed eliminare tavoli;
- inserire, modificare ed eliminare giochi;
- creare eventi associando uno o più giochi;
- gestire le prenotazioni;
- gestire le iscrizioni agli eventi;
- eventualmente rendere non disponibili tavoli o giochi.

Il sistema distingue quindi tra tre livelli di accesso:

1. visitatore anonimo;
2. utente registrato;
3. amministratore.

---

## 3. Entità principali

Il progetto deve contenere almeno 5-6 entità. Il modello attualmente implementato contiene le sei entità principali del dominio, con alcune semplificazioni rispetto alla proposta iniziale.

---

## 3.1 Utente

Rappresenta un utente del sistema.

### Attributi principali

- id
- nome
- cognome
- username
- email
- password
- ruolo

### Possibili valori di ruolo

- `USER`
- `ADMIN`

### Relazioni

- un utente può effettuare più prenotazioni;
- un utente può iscriversi a più eventi.

### Relazioni consigliate

```text
Utente 1 --- N Prenotazione
Utente 1 --- N IscrizioneEvento
```

---

## 3.2 Tavolo

Rappresenta un tavolo fisico del ludopub.

### Attributi principali

- id
- numero
- nome
- capienza
- posizione
- disponibile
- attivo

### Valori di posizione nel modello attuale

- `INTERNO`
- `ESTERNO`

### Relazioni

- un tavolo può essere associato a più prenotazioni nel tempo;
- una prenotazione riguarda un solo tavolo.

### Relazione consigliata

```text
Tavolo 1 --- N Prenotazione
```

---

## 3.3 Gioco

Rappresenta un gioco da tavolo disponibile nel ludopub.

### Attributi principali

- id
- titolo
- descrizione
- categoria
- numeroMinGiocatori
- numeroMaxGiocatori
- durataMediaMinuti
- difficolta
- stato
- immagineUrl, opzionale

### Possibili valori di stato

- `DISPONIBILE`
- `NON_DISPONIBILE`
- `DANNEGGIATO`
- `RISERVATO_EVENTO`

### Esempi di categoria

- strategia;
- party game;
- cooperativo;
- carte;
- investigativo;
- astratto;
- family game.

### Relazioni

- un gioco può essere incluso in più eventi;
- un evento può includere più giochi.

### Relazione consigliata

```text
Gioco N --- M Evento
```

In alternativa, se si vogliono salvare informazioni aggiuntive sulla presenza di un gioco in un evento, si può introdurre una classe intermedia:

```text
Evento 1 --- N EventoGioco
Gioco 1 --- N EventoGioco
```

Questa seconda soluzione è più flessibile, ma leggermente più complessa.

---

## 3.4 Prenotazione

Rappresenta la prenotazione di un tavolo da parte di un utente.

### Attributi principali

- id
- data
- oraInizio
- oraFine
- numeroPersone
- note

### Relazioni

- una prenotazione appartiene a un utente;
- una prenotazione riguarda un tavolo;
- un utente può avere più prenotazioni;
- un tavolo può avere più prenotazioni in date e orari diversi.

### Relazioni consigliate

```text
Utente 1 --- N Prenotazione
Tavolo 1 --- N Prenotazione
```

### Regola di business importante

Non devono esistere due prenotazioni per lo stesso tavolo nella stessa fascia oraria.

In altre parole, se un tavolo è già prenotato dalle 20:00 alle 22:00, un altro utente non deve poter prenotare lo stesso tavolo dalle 21:00 alle 23:00.

Questa è una delle regole più importanti del progetto, perché permette di dimostrare vera logica applicativa nel service layer.

Nota sul modello attuale: `Prenotazione` non contiene un campo `stato`. Se in futuro si vuole mantenere lo storico delle prenotazioni annullate o completate, si può reintrodurre uno stato dedicato.

---

## 3.5 Evento

Rappresenta un evento organizzato dal ludopub.

Esempi:

- serata giochi strategici;
- torneo di Catan;
- serata giochi investigativi;
- evento introduttivo per principianti;
- serata party game.

### Attributi principali

- id
- nome
- descrizione
- data
- oraInizio
- oraFine
- numeroMaxPartecipanti
- stato
- costoPartecipazione, opzionale

### Possibili valori di stato

- `APERTO`
- `CHIUSO`
- `ANNULLATO`
- `COMPLETATO`

### Relazioni

- un evento può includere più giochi;
- un gioco può essere usato in più eventi;
- un evento può avere più utenti iscritti;
- un utente può iscriversi a più eventi.

### Relazioni consigliate

```text
Evento N --- M Gioco
Evento 1 --- N IscrizioneEvento
Utente 1 --- N IscrizioneEvento
```

---

## 3.6 IscrizioneEvento

Rappresenta l'iscrizione di un utente a un evento.

Questa entità è preferibile rispetto a una relazione many-to-many diretta tra `Utente` ed `Evento`, perché permette di salvare informazioni aggiuntive.

### Attributi principali

- id
- dataIscrizione
- note

### Relazioni

- una iscrizione appartiene a un utente;
- una iscrizione riguarda un evento.

### Relazioni consigliate

```text
Utente 1 --- N IscrizioneEvento
Evento 1 --- N IscrizioneEvento
```

### Regola di business importante

Un utente non deve potersi iscrivere due volte allo stesso evento.

Inoltre, il sistema deve impedire nuove iscrizioni se l'evento ha già raggiunto il numero massimo di partecipanti.

Nota sul modello attuale: `IscrizioneEvento` non contiene un campo `stato` e non contiene `numeroPartecipanti`; ogni iscrizione rappresenta quindi la partecipazione di un utente a un evento.

---

## 3.7 Entità facoltative

Le seguenti entità non sono obbligatorie, ma possono essere aggiunte se si vuole rendere il progetto più ricco.

### RecensioneGioco

Permette agli utenti registrati di recensire un gioco.

Attributi:

- voto;
- commento;
- data;
- utente;
- gioco.

Relazioni:

```text
Utente 1 --- N RecensioneGioco
Gioco 1 --- N RecensioneGioco
```

### CategoriaGioco

Può essere usata se si vuole evitare di rappresentare la categoria come semplice stringa o enum.

Relazione:

```text
CategoriaGioco 1 --- N Gioco
```

### ImmagineGioco

Può essere usata se si vuole gestire l'upload di immagini.

Relazione:

```text
Gioco 1 --- N ImmagineGioco
```

Per mantenere il progetto gestibile, si consiglia di partire con le sei entità principali:

```text
Utente
Tavolo
Gioco
Prenotazione
Evento
IscrizioneEvento
```

---

## 4. Casi d'uso

Il progetto personale deve includere almeno 6 casi d'uso significativi.

Autenticazione e registrazione non devono essere contate tra questi casi d'uso.

---

## 4.1 Funzionalità pubbliche

Le funzionalità pubbliche sono accessibili anche da utenti non autenticati.

### UC1 - Visualizzare il catalogo dei giochi

L'utente visualizza l'elenco dei giochi disponibili nel ludopub.

Informazioni mostrate:

- titolo;
- categoria;
- numero minimo e massimo di giocatori;
- durata media;
- difficoltà;
- stato del gioco.

Tipo di operazione:

```text
Lettura
```

Entità coinvolte:

```text
Gioco
```

---

### UC2 - Visualizzare il dettaglio di un gioco

L'utente seleziona un gioco e ne visualizza il dettaglio.

Informazioni mostrate:

- descrizione;
- categoria;
- durata;
- numero di giocatori;
- stato;
- eventuali eventi in cui il gioco è incluso.

Tipo di operazione:

```text
Lettura di una o più entità
```

Entità coinvolte:

```text
Gioco
Evento
```

Questo caso d'uso è utile per mostrare una relazione tra giochi ed eventi.

---

### UC3 - Visualizzare gli eventi disponibili

L'utente visualizza gli eventi futuri organizzati dal ludopub.

Informazioni mostrate:

- nome evento;
- data;
- orario;
- giochi inclusi;
- posti disponibili;
- stato.

Tipo di operazione:

```text
Lettura di una o più entità
```

Entità coinvolte:

```text
Evento
Gioco
IscrizioneEvento
```

Questo è un buon caso d'uso per testare strategie di fetch, perché il sistema deve mostrare eventi, giochi associati e numero di iscritti.

---

## 4.2 Funzionalità per utenti registrati

Le funzionalità utente richiedono login con ruolo `USER`.

---

### UC4 - Prenotare un tavolo

L'utente registrato sceglie:

- data;
- ora di inizio;
- ora di fine;
- numero di persone.

Il sistema mostra i tavoli disponibili o assegna un tavolo compatibile.

Il sistema deve controllare che:

1. il tavolo esista;
2. il tavolo sia disponibile;
3. la capienza del tavolo sia sufficiente;
4. non esista già una prenotazione sovrapposta nello stesso intervallo orario;
5. l'orario di fine sia successivo all'orario di inizio.

Tipo di operazione:

```text
Inserimento
```

Entità coinvolte:

```text
Utente
Tavolo
Prenotazione
```

Questo è uno dei casi d'uso principali del progetto.

---

### UC5 - Modificare una propria prenotazione

L'utente registrato può modificare una propria prenotazione futura.

Campi modificabili:

- data;
- ora inizio;
- ora fine;
- numero persone;
- note.

Il sistema deve controllare che:

1. la prenotazione appartenga all'utente autenticato;
2. la prenotazione sia futura;
3. la nuova fascia oraria non si sovrapponga con altre prenotazioni dello stesso tavolo;
4. il numero di persone sia compatibile con la capienza del tavolo.

Tipo di operazione:

```text
Aggiornamento
```

Entità coinvolte:

```text
Utente
Prenotazione
Tavolo
```

---

### UC6 - Annullare una propria prenotazione

L'utente registrato può annullare una propria prenotazione futura.

Nel modello attuale `Prenotazione` non ha un campo `stato`, quindi l'annullamento può essere gestito eliminando la prenotazione. Se si vuole mantenere lo storico, sarà possibile aggiungere in seguito uno stato dedicato.

Tipo di operazione:

```text
Cancellazione o futura cancellazione logica
```

Entità coinvolte:

```text
Utente
Prenotazione
```

---

### UC7 - Iscriversi a un evento

L'utente registrato può iscriversi a un evento aperto.

Il sistema deve controllare che:

1. l'evento esista;
2. l'evento sia in stato `APERTO`;
3. l'evento non sia già pieno;
4. l'utente non sia già iscritto allo stesso evento;
5. il numero di iscrizioni già presenti sia inferiore ai posti disponibili.

Tipo di operazione:

```text
Inserimento
```

Entità coinvolte:

```text
Utente
Evento
IscrizioneEvento
```

---

### UC8 - Annullare l'iscrizione a un evento

L'utente registrato può annullare la propria iscrizione a un evento.

Nel modello attuale `IscrizioneEvento` non ha un campo `stato`, quindi l'annullamento può essere gestito eliminando l'iscrizione. Se si vuole mantenere lo storico, sarà possibile aggiungere in seguito uno stato dedicato.

Tipo di operazione:

```text
Cancellazione o futura cancellazione logica
```

Entità coinvolte:

```text
Utente
Evento
IscrizioneEvento
```

---

## 4.3 Funzionalità amministratore

Le funzionalità amministrative richiedono login con ruolo `ADMIN`.

---

### UC9 - Creare, modificare ed eliminare un tavolo

L'amministratore può gestire i tavoli del locale.

Operazioni:

- inserimento nuovo tavolo;
- modifica della capienza;
- modifica della posizione;
- disattivazione di un tavolo;
- eliminazione, se non ci sono prenotazioni collegate.

Tipo di operazione:

```text
Inserimento
Aggiornamento
Cancellazione
```

Entità coinvolte:

```text
Tavolo
Prenotazione
```

Nota: se un tavolo ha prenotazioni associate, è meglio non eliminarlo fisicamente. Si può usare un campo booleano `attivo`.

---

### UC10 - Creare, modificare ed eliminare un gioco

L'amministratore può gestire il catalogo dei giochi.

Operazioni:

- inserimento di un nuovo gioco;
- modifica dati del gioco;
- modifica dello stato;
- eliminazione o disattivazione del gioco.

Tipo di operazione:

```text
Inserimento
Aggiornamento
Cancellazione
```

Entità coinvolte:

```text
Gioco
Evento
```

Nota: se un gioco è associato a eventi passati, è meglio non eliminarlo fisicamente. Si può usare uno stato come `NON_DISPONIBILE`.

---

### UC11 - Creare un evento associando più giochi

L'amministratore crea un evento e seleziona uno o più giochi da associare.

Campi evento:

- nome;
- descrizione;
- data;
- ora inizio;
- ora fine;
- numero massimo partecipanti;
- giochi inclusi.

Il sistema deve controllare che:

1. la data dell'evento sia futura;
2. l'orario sia valido;
3. il numero massimo di partecipanti sia positivo;
4. i giochi selezionati esistano;
5. i giochi selezionati siano disponibili.

Tipo di operazione:

```text
Inserimento multi-entità
```

Entità coinvolte:

```text
Evento
Gioco
```

Questo è uno dei casi d'uso più interessanti per l'orale, perché coinvolge più entità e una relazione many-to-many.

---

### UC12 - Gestire le iscrizioni agli eventi

L'amministratore può visualizzare gli iscritti a un evento e, se necessario:

- annullare una iscrizione;
- chiudere le iscrizioni;
- segnare gli utenti presenti;
- annullare l'evento.

Tipo di operazione:

```text
Lettura
Aggiornamento
```

Entità coinvolte:

```text
Evento
IscrizioneEvento
Utente
```

---

## 5. Requisiti minimi del progetto

Il progetto dovrebbe includere almeno:

- 6 entità principali;
- almeno 6 casi d'uso reali;
- almeno un caso d'uso di inserimento;
- almeno un caso d'uso di aggiornamento;
- almeno un caso d'uso di cancellazione o cancellazione logica;
- almeno due casi d'uso di lettura;
- almeno un caso d'uso che coinvolga più entità;
- autenticazione e autorizzazione;
- ruoli `USER` e `ADMIN`;
- architettura a livelli;
- persistenza con JPA/Hibernate;
- database relazionale;
- almeno una parte del frontend in React.

---

## 6. Requisiti di sicurezza

Il sistema deve prevedere autenticazione e autorizzazione.

### Ruoli

```text
USER
ADMIN
```

### Utente anonimo

Può:

- visualizzare la home;
- visualizzare il catalogo giochi;
- visualizzare il dettaglio di un gioco;
- visualizzare gli eventi pubblici.

Non può:

- prenotare tavoli;
- iscriversi a eventi;
- accedere alla dashboard admin.

### Utente registrato

Può:

- prenotare un tavolo;
- modificare una propria prenotazione;
- annullare una propria prenotazione;
- iscriversi a eventi;
- annullare una propria iscrizione;
- visualizzare le proprie prenotazioni;
- visualizzare le proprie iscrizioni.

Non può:

- modificare tavoli;
- modificare giochi;
- creare eventi;
- modificare prenotazioni di altri utenti.

### Amministratore

Può:

- gestire tavoli;
- gestire giochi;
- gestire eventi;
- visualizzare tutte le prenotazioni;
- gestire iscrizioni agli eventi;
- chiudere o annullare eventi.

---

## 7. Logica di business fondamentale

Il progetto deve contenere regole applicative chiare.

---

## 7.1 Regola sulle prenotazioni sovrapposte

Un tavolo non può avere due prenotazioni nella stessa fascia oraria.

Due intervalli si sovrappongono se:

```text
nuovoInizio < prenotazioneEsistenteFine
AND
nuovoFine > prenotazioneEsistenteInizio
```

---

## 7.2 Regola sulla capienza del tavolo

Il numero di persone della prenotazione deve essere minore o uguale alla capienza del tavolo.

---

## 7.3 Regola sugli eventi pieni

Un utente non può iscriversi a un evento se il numero massimo di partecipanti è stato raggiunto.

Nel modello attuale `IscrizioneEvento` non ha un campo `stato`, quindi il conteggio considera le iscrizioni presenti per l'evento.

---

## 7.4 Regola sulla doppia iscrizione

Lo stesso utente non può avere due iscrizioni allo stesso evento.

---

## 7.5 Regola sui giochi associati agli eventi

Un evento dovrebbe poter includere solo giochi disponibili.

Esempio:

```text
Gioco in stato DANNEGGIATO
```

Risultato:

```text
Non può essere associato a un nuovo evento.
```

---

## 8. Frontend

Il progetto deve usare Thymeleaf e almeno una parte in React.

---

## 8.1 Parte realizzata con Thymeleaf

Si consiglia di usare Thymeleaf per:

- home page;
- login;
- registrazione;
- dashboard utente;
- dashboard admin;
- gestione tavoli;
- gestione giochi;
- gestione eventi;
- pagine di dettaglio.

---

## 8.2 Parte realizzata con React

La parte React dovrebbe essere scelta in modo intelligente, non solo decorativo.

Opzioni consigliate:

### Opzione A - Prenotazione tavolo dinamica <---(Punterei a questa)

React gestisce una pagina in cui l'utente seleziona:

- data;
- ora inizio;
- ora fine;
- numero persone.

Il frontend chiama un endpoint REST per ottenere i tavoli disponibili.

Esempio:

```text
GET /api/tavoli/disponibili?data=2026-06-10&oraInizio=20:00&oraFine=22:00&numeroPersone=4
```

Il sistema mostra dinamicamente solo i tavoli prenotabili.

Si possono filtrare per tavoli con capienza esatta o maggiore/dentro o fuori/etc...

Questa è l'opzione più forte perché è collegata alla logica principale del progetto.

---

### Opzione B - Catalogo giochi con filtri

React gestisce un catalogo giochi filtrabile per:

- titolo;
- categoria;
- numero giocatori;
- durata;
- difficoltà.

Esempio:

```text
GET /api/giochi?categoria=Strategia&numeroGiocatori=4
```

Questa opzione è più semplice, ma comunque valida.

---

### Opzione C - Calendario eventi

React mostra gli eventi in una vista dinamica.

Possibili funzioni:

- filtro per data;
- filtro per categoria;
- visualizzazione posti disponibili;
- bottone per iscriversi.

---

## 8.3 Upload immagini

Possibile per:

- immagini dei giochi;
- locandine degli eventi.

Soluzioni possibili:

- salvataggio locale in sviluppo;
- salvataggio su Azure Blob Storage in deploy.

---
