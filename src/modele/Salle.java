package modele;

import modele.enums.TypeSalle;
import java.util.ArrayList;
import java.util.List;

public class Salle {

    private int idSalle;
    private String numeroSalle; // String car peut être "A101"
    private int capacite;
    private TypeSalle type;
    private Batiment batiment;
    private List<Equipement> equipements; // ✅ Liste

    // Constructeur sans équipements
    public Salle(int idSalle, String numeroSalle, int capacite, TypeSalle type, Batiment batiment) {
        this.idSalle = idSalle;
        this.numeroSalle = numeroSalle;
        this.capacite = capacite;
        this.type = type;
        this.batiment = batiment;
        this.equipements = new ArrayList<>(); // ✅ liste vide par défaut
    }

    // Getters
    public int getIdSalle() { return idSalle; }
    public String getNumeroSalle() { return numeroSalle; }
    public int getCapacite() { return capacite; }
    public TypeSalle getType() { return type; }
    public Batiment getBatiment() { return batiment; }
    public List<Equipement> getEquipements() { return equipements; }

    // Setters
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setType(TypeSalle type) { this.type = type; }
    public void setBatiment(Batiment batiment) { this.batiment = batiment; }
    public void setEquipements(List<Equipement> equipements) { this.equipements = equipements; }

    // ✅ Ajouter un équipement à la liste
    public void ajouterEquipement(Equipement equipement) {
        this.equipements.add(equipement);
    }

    @Override
    public String toString() {
        return numeroSalle + " - " + type + " (" + capacite + " places)";
    }
}