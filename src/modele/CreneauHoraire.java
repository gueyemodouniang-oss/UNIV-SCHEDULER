package modele;

import modele.enums.Jour;

public class CreneauHoraire {

    private int idCreneau;
    private Jour jour;
    private String heureDebut;
    private String heureFin;

    // Constructeur
    public CreneauHoraire(int idCreneau, Jour jour, String heureDebut, String heureFin) {
        this.idCreneau = idCreneau;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    // Getters
    public int getIdCreneauHoraire() {
        return idCreneau;
    }

    public Jour getJour() {
        return jour;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    // Setters
    public void setJour(Jour jour) {
        this.jour = jour;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    @Override
    public String toString() {
        return "CreneauHoraire{" +
                "idCreneau=" + idCreneau +
                ", jour='" + jour + '\'' +
                ", heureDebut='" + heureDebut + '\'' +
                ", heureFin='" + heureFin + '\'' +
                '}';
    }
}