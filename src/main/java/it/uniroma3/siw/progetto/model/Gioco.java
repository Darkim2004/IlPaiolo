package it.uniroma3.siw.progetto.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Gioco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(length = 2000)
    private String descrizione;

    private String categoria;

    private Integer numeroMinGiocatori;

    private Integer numeroMaxGiocatori;

    private Integer durataMediaMinuti;

    private String difficolta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoGioco stato = StatoGioco.DISPONIBILE;

    private String immagineUrl;

    @ManyToMany
    @JoinTable(
            name = "gioco_evento",
            joinColumns = @JoinColumn(name = "gioco_id"),
            inverseJoinColumns = @JoinColumn(name = "evento_id")
    )
    private Set<Evento> eventi = new HashSet<>();

    public Gioco() {
    }

    public Gioco(String titolo, String descrizione, String categoria, Integer numeroMinGiocatori,
            Integer numeroMaxGiocatori, Integer durataMediaMinuti, String difficolta) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.numeroMinGiocatori = numeroMinGiocatori;
        this.numeroMaxGiocatori = numeroMaxGiocatori;
        this.durataMediaMinuti = durataMediaMinuti;
        this.difficolta = difficolta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getNumeroMinGiocatori() {
        return numeroMinGiocatori;
    }

    public void setNumeroMinGiocatori(Integer numeroMinGiocatori) {
        this.numeroMinGiocatori = numeroMinGiocatori;
    }

    public Integer getNumeroMaxGiocatori() {
        return numeroMaxGiocatori;
    }

    public void setNumeroMaxGiocatori(Integer numeroMaxGiocatori) {
        this.numeroMaxGiocatori = numeroMaxGiocatori;
    }

    public Integer getDurataMediaMinuti() {
        return durataMediaMinuti;
    }

    public void setDurataMediaMinuti(Integer durataMediaMinuti) {
        this.durataMediaMinuti = durataMediaMinuti;
    }

    public String getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }

    public StatoGioco getStato() {
        return stato;
    }

    public void setStato(StatoGioco stato) {
        this.stato = stato;
    }

    public String getImmagineUrl() {
        return immagineUrl;
    }

    public void setImmagineUrl(String immagineUrl) {
        this.immagineUrl = immagineUrl;
    }

    public Set<Evento> getEventi() {
        return eventi;
    }

    public void setEventi(Set<Evento> eventi) {
        this.eventi = eventi;
    }
}
