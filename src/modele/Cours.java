package modele;

import modele.Utilisateurs.Enseignant;

public class Cours {

    private int idCours;
    private String matiere;
    private Enseignant enseignant;
    private Salle salle;
    private CreneauHoraire creneau;

    // Constructeur
    public Cours(int idCours, String matiere, Enseignant enseignant,
                 Salle salle, CreneauHoraire creneau) {

        this.idCours = idCours;
        this.matiere = matiere;
        this.enseignant = enseignant;
        this.salle = salle;
        this.creneau = creneau;
    }

    // Getters
    public int getIdCours() {
        return idCours;
    }

    public String getMatiere() {
        return matiere;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public Salle getSalle() {
        return salle;
    }

    public CreneauHoraire getCreneau() {
        return creneau;
    }

    // Setters
    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public void setCreneau(CreneauHoraire creneau) {
        this.creneau = creneau;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "idCours=" + idCours +
                ", matiere='" + matiere + '\'' +
                ", enseignant=" + enseignant.getNom() +
                ", salle=" + salle.getNumero() +
                ", creneau=" + creneau.getJour() + " " + creneau.getHeureDebut() +
                '}';
    }
}