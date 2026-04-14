package modele;

import modele.Utilisateurs.Enseignant;

public class Cours {

    private int idCours;
    private Matiere matiere;
    private Enseignant enseignant;

    public Cours(int idCours, Matiere matiere, Enseignant enseignant) {
        this.idCours     = idCours;
        this.matiere     = matiere;
        this.enseignant  = enseignant;
    }

    public int getIdCours()          { return idCours; }
    public Matiere getMatiere()      { return matiere; }
    public Enseignant getEnseignant(){ return enseignant; }

    public void setMatiere(Matiere matiere)          { this.matiere = matiere; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    @Override
    public String toString() {
        return matiere.getNomMatiere() + " — " + enseignant.getNom();
    }
}