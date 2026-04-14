package modele;

public class Equipement {

    private int idEquipement;
    private String nomEquipement;
    private String description;

    // Constructeur
    public Equipement(int idEquipement, String nomEquipement, String description) {
        this.idEquipement = idEquipement;
        this.nomEquipement = nomEquipement;
        this.description = description;
    }

    // Getters
    public int getIdEquipement() {
        return idEquipement;
    }

    public String getNomEquipement() {
        return nomEquipement;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setNomEquipement(String nomEquipement) {
        this.nomEquipement = nomEquipement;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {return nomEquipement;}
}