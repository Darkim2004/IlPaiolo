package it.uniroma3.siw.progetto.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tavolo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer numero;

    private String nome;

    @Column(nullable = false)
    private Integer capienza;

    @Enumerated(EnumType.STRING)
    private PosizioneTavolo posizione;

    @Column(nullable = false)
    private boolean disponibile = true;

    @Column(nullable = false)
    private boolean attivo = true;

    @OneToMany(mappedBy = "tavolo")
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    public Tavolo() {
    }

    public Tavolo(Integer numero, String nome, Integer capienza, PosizioneTavolo posizione) {
        this.numero = numero;
        this.nome = nome;
        this.capienza = capienza;
        this.posizione = posizione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapienza() {
        return capienza;
    }

    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    public PosizioneTavolo getPosizione() {
        return posizione;
    }

    public void setPosizione(PosizioneTavolo posizione) {
        this.posizione = posizione;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }
}
