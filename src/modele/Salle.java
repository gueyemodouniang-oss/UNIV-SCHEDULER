package modele;

import modele.enums.TypeSalle;

public class Salle {

    private int numeroSalle;
    private int capacite;
    private TypeSalle type; // TD, TP, Amphi
    private Batiment batiment;

    // Constructeur
    public Salle(int numeroSalle, int capacite, TypeSalle type, Batiment batiment) {
        this.numeroSalle = numeroSalle;
        this.capacite = capacite;
        this.type = type;
        this.batiment = batiment;
    }

    // Getters
    public int getNumero() {
        return numeroSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public TypeSalle getType() {
        return type;
    }

    public Batiment getBatiment() {
        return batiment;
    }

    // Setters
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setType(TypeSalle type) {
        this.type = type;
    }

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "numero=" + numeroSalle +
                ", capacite=" + capacite +
                ", type='" + type + '\'' +
                ", batiment=" + batiment.getNomBatiment() +
                '}';
    }
}