package modele;

public class Batiment {

    private int idBatiment;
    private String nomBatiment;
    private String localisation;

    // Constructeur
    public Batiment(int idBatiment, String nomBatiment, String localisation) {
        this.idBatiment = idBatiment;
        this.nomBatiment = nomBatiment;
        this.localisation = localisation;
    }

    // Getters
    public int getIdBatiment() {
        return idBatiment;
    }

    public String getNomBatiment() {
        return nomBatiment;
    }

    public String getLocalisation() {
        return localisation;
    }

    // Setters
    public void setNomBatiment(String nomBatiment) {
        this.nomBatiment = nomBatiment;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    @Override
    public String toString() {
        return "Batiment{" +
                "idBatiment=" + idBatiment +
                ", nomBatiment='" + nomBatiment + '\'' +
                ", localisation='" + localisation + '\'' +
                '}';
    }
}