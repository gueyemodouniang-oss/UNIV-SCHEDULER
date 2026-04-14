package modele;

public class Seance {

    private int idSeance;
    private Cours cours;
    private Salle salle;
    private CreneauHoraire creneau;
    private Groupe groupe;

    public Seance(int idSeance, Cours cours, Salle salle,
                  CreneauHoraire creneau, Groupe groupe) {
        this.idSeance = idSeance;
        this.cours    = cours;
        this.salle    = salle;
        this.creneau  = creneau;
        this.groupe   = groupe;
    }

    public int getIdSeance()             { return idSeance; }
    public Cours getCours()              { return cours; }
    public Salle getSalle()              { return salle; }
    public CreneauHoraire getCreneau()   { return creneau; }
    public Groupe getGroupe()            { return groupe; }

    public void setCours(Cours cours)            { this.cours = cours; }
    public void setSalle(Salle salle)            { this.salle = salle; }
    public void setCreneau(CreneauHoraire c)     { this.creneau = c; }
    public void setGroupe(Groupe groupe)         { this.groupe = groupe; }

    @Override
    public String toString() {
        return cours.getMatiere().getNomMatiere() +
                " — " + creneau.getJour() +
                " " + creneau.getHeureDebut() +
                " — " + salle.getNumeroSalle() +
                " — " + groupe.getNomGroupe();
    }
}