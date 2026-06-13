package it.uniroma3.siw.progetto.model;

public enum StatoGioco {

    DISPONIBILE("Disponibile"),
    NON_DISPONIBILE("Non disponibile"),
    DANNEGGIATO("Danneggiato"),
    RISERVATO_EVENTO("Riservato per evento");

    private final String label;

    StatoGioco(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
