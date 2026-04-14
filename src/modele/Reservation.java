package modele;

import modele.enums.StatutReservation;
import modele.Utilisateurs.Utilisateur;
import java.time.LocalDate;

public class Reservation {

    private int idReservation;
    private Salle salle;
    private CreneauHoraire creneau;
    private Utilisateur utilisateur;
    private StatutReservation statut;
    private LocalDate dateReservation; // ✅ date concrète au lieu de Jour

    public Reservation(int idReservation, Salle salle, CreneauHoraire creneau,
                       Utilisateur utilisateur, StatutReservation statut,
                       LocalDate dateReservation) {
        this.idReservation = idReservation;
        this.salle = salle;
        this.creneau = creneau;
        this.utilisateur = utilisateur;
        this.statut = statut;
        this.dateReservation = dateReservation;
    }

    public int getIdReservation() { return idReservation; }
    public Salle getSalle() { return salle; }
    public CreneauHoraire getCreneau() { return creneau; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public StatutReservation getStatut() { return statut; }
    public LocalDate getDateReservation() { return dateReservation; }

    public void setStatut(StatutReservation statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", salle=" + salle.getNumeroSalle() +
                ", date=" + dateReservation +
                ", creneau=" + creneau.getHeureDebut() + "-" + creneau.getHeureFin() +
                ", utilisateur=" + utilisateur.getNom() +
                ", statut=" + statut +
                '}';
    }
}