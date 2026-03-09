package modele;

import modele.enums.StatutReservation;
import modele.Utilisateurs.Utilisateur;
import modele.enums.Jour;


public class Reservation {

    private int idReservation;
    private Salle salle;
    private CreneauHoraire creneau;
    private Utilisateur utilisateur;
    private StatutReservation statut;
    private Jour jour;

    public Reservation(int idReservation, Salle salle, CreneauHoraire creneau,
                       Utilisateur utilisateur, StatutReservation statut, Jour jour) {

        this.idReservation = idReservation;
        this.salle = salle;
        this.creneau = creneau;
        this.utilisateur = utilisateur;
        this.statut = statut;
        this.jour = jour;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public Salle getSalle() {
        return salle;
    }

    public CreneauHoraire getCreneauHoraire() {
        return creneau;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public StatutReservation getStatut() {
        return statut;
    }
    public Jour getJour(){
        return jour;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", salle=" + salle.getNumero() +
                ", creneau=" + creneau.getJour() +
                ", utilisateur=" + utilisateur.getNom() +
                ", statut=" + statut +
                ", Jour=" + jour +
                '}';
    }
}