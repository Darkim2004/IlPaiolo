package it.uniroma3.siw.progetto.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 2000)
    private String descrizione;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime oraInizio;

    @Column(nullable = false)
    private LocalTime oraFine;

    @Column(nullable = false)
    private int numeroMaxPartecipanti;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoEvento stato = StatoEvento.APERTO;

    private BigDecimal costoPartecipazione;

    @ManyToMany(mappedBy = "eventi")
    private Set<Gioco> giochi = new HashSet<>();

    @OneToMany(mappedBy = "evento")
    private List<IscrizioneEvento> iscrizioni = new ArrayList<>();

    public Evento() {
    }

    public Evento(String nome, String descrizione, LocalDate data, LocalTime oraInizio, LocalTime oraFine,
            int numeroMaxPartecipanti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.numeroMaxPartecipanti = numeroMaxPartecipanti;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public int getNumeroMaxPartecipanti() {
        return numeroMaxPartecipanti;
    }

    public void setNumeroMaxPartecipanti(int numeroMaxPartecipanti) {
        this.numeroMaxPartecipanti = numeroMaxPartecipanti;
    }

    public StatoEvento getStato() {
        return stato;
    }

    public void setStato(StatoEvento stato) {
        this.stato = stato;
    }

    public BigDecimal getCostoPartecipazione() {
        return costoPartecipazione;
    }

    public void setCostoPartecipazione(BigDecimal costoPartecipazione) {
        this.costoPartecipazione = costoPartecipazione;
    }

    public Set<Gioco> getGiochi() {
        return giochi;
    }

    public void setGiochi(Set<Gioco> giochi) {
        this.giochi = giochi;
    }

    public List<IscrizioneEvento> getIscrizioni() {
        return iscrizioni;
    }

    public void setIscrizioni(List<IscrizioneEvento> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
}
