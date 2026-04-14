
package modele;

public class Matiere {

    private int idMatiere;
    private String nomMatiere;
    private int volumeHoraire;

    public Matiere(int idMatiere, String nomMatiere, int volumeHoraire) {
        this.idMatiere = idMatiere;
        this.nomMatiere = nomMatiere;
        this.volumeHoraire = volumeHoraire;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public String getNomMatiere() {
        return nomMatiere;
    }

    public int getVolumeHoraire() {
        return volumeHoraire;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public void setVolumeHoraire(int volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }

    @Override
    public String toString() {
        return nomMatiere + " (" + volumeHoraire + "h)";
    }
}