package service;

import modele.Reservation;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private List<Reservation> reservations = new ArrayList<>();
    private ConflitService conflitService = new ConflitService();

    // Ajouter une réservation
    public void ajouterReservation(Reservation nouvelleReservation) {

        for (Reservation r : reservations) {

            if (conflitService.verifierConflit(r, nouvelleReservation)) {
                System.out.println("❌ Conflit détecté : réservation impossible.");
                return;
            }
        }

        reservations.add(nouvelleReservation);
        System.out.println("✅ Réservation ajoutée avec succès.");
    }

    // Supprimer une réservation
    public void supprimerReservation(Reservation reservation) {

        if (reservations.remove(reservation)) {
            System.out.println("🗑 Réservation supprimée.");
        } else {
            System.out.println("⚠ Réservation introuvable.");
        }

    }

    // Afficher toutes les réservations
    public void afficherReservations() {

        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Getter
    public List<Reservation> getReservations() {
        return reservations;
    }
}