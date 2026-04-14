package service;

import dao.impl.ReservationDAO;
import modele.Reservation;
import modele.enums.StatutReservation;

import java.util.List;

public class ReservationService {

    private ReservationDAO reservationDAO = new ReservationDAO();
    private ConflitService conflitService = new ConflitService();

    // ✅ Ajouter une réservation confirmée directement
    public boolean ajouterReservation(Reservation reservation) {
        boolean conflit = conflitService.salleOccupee(
                reservation.getSalle().getIdSalle(),
                reservation.getCreneau().getIdCreneauHoraire(),
                reservation.getDateReservation().toString()
        );

        if (conflit) {
            System.out.println("❌ Conflit détecté : salle déjà occupée !");
            return false;
        }

        reservationDAO.ajouter(reservation);
        System.out.println("✅ Réservation ajoutée avec succès.");
        return true;
    }

    // ✅ Ajouter une réservation en attente
    public boolean ajouterReservationEnAttente(Reservation reservation) {
        boolean conflit = conflitService.salleOccupee(
                reservation.getSalle().getIdSalle(),
                reservation.getCreneau().getIdCreneauHoraire(),
                reservation.getDateReservation().toString()
        );

        if (conflit) {
            System.out.println("❌ Conflit détecté : salle déjà occupée !");
            return false;
        }

        reservation.setStatut(StatutReservation.En_attente);
        reservationDAO.ajouter(reservation);
        System.out.println("⏳ Réservation ajoutée en attente de confirmation.");
        return true;
    }

    // ✅ Confirmer une réservation
    public void confirmerReservation(int idReservation) {
        reservationDAO.changerStatut(idReservation, StatutReservation.Confirme);
        System.out.println("✅ Réservation confirmée.");
    }

    // ✅ Mettre une réservation en attente
    public void mettreEnAttente(int idReservation) {
        reservationDAO.changerStatut(idReservation, StatutReservation.En_attente);
        System.out.println("⏳ Réservation mise en attente.");
    }

    // ✅ Annuler une réservation
    public void annulerReservation(int idReservation) {
        reservationDAO.changerStatut(idReservation, StatutReservation.Annule);
        System.out.println("🗑 Réservation annulée.");
    }

    // ✅ Supprimer une réservation
    public void supprimerReservation(int idReservation) {
        reservationDAO.supprimer(idReservation);
        System.out.println("🗑 Réservation supprimée.");
    }

    // ✅ Toutes les réservations
    public List<Reservation> getReservations() {
        return reservationDAO.findAll();
    }

    // ✅ Réservations en attente
    public List<Reservation> getReservationsEnAttente() {
        return reservationDAO.trouverParStatut(StatutReservation.En_attente);
    }

    // ✅ Réservations confirmées
    public List<Reservation> getReservationsConfirmees() {
        return reservationDAO.trouverParStatut(StatutReservation.Confirme);
    }

    // ✅ Réservations annulées
    public List<Reservation> getReservationsAnnulees() {
        return reservationDAO.trouverParStatut(StatutReservation.Annule);
    }

    // ✅ Réservations par utilisateur
    public List<Reservation> getReservationsParUtilisateur(int utilisateurId) {
        return reservationDAO.trouverParUtilisateur(utilisateurId);
    }

    // ✅ Réservations par statut
    public List<Reservation> getReservationsParStatut(StatutReservation statut) {
        return reservationDAO.trouverParStatut(statut);
    }
}