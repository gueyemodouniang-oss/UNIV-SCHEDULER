package service;

import dao.impl.SeanceDAO;
import dao.impl.ReservationDAO;
import modele.Reservation;
import modele.Seance;
import modele.Salle;
import modele.Utilisateurs.Utilisateur;
import modele.enums.Jour;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Planning {

    private SeanceDAO seanceDAO = new SeanceDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();

    // ✅ Afficher tout le planning (séances)
    public void afficherPlanning() {
        List<Seance> seances = seanceDAO.findAll();
        if (seances.isEmpty()) {
            System.out.println("Aucune séance planifiée.");
            return;
        }
        seances.forEach(System.out::println);
    }

    // ✅ Planning par jour
    public void afficherPlanningParJour(Jour jour) {
        List<Seance> seances = seanceDAO.findAll();
        seances.stream()
                .filter(s -> s.getCreneau().getJour() == jour)
                .forEach(System.out::println);
    }


    // ✅ Planning par salle
    public void afficherPlanningParSalle(Salle salle) {
        List<Seance> seances = seanceDAO.trouverParSalle(salle.getIdSalle());
        if (seances.isEmpty()) {
            System.out.println("Aucune séance pour la salle " + salle.getNumeroSalle());
            return;
        }
        seances.forEach(System.out::println);
    }

    // ✅ Planning par enseignant
    public void afficherPlanningParEnseignant(int enseignantId) {
        List<Seance> seances = seanceDAO.findAll().stream()
                .filter(s -> s.getCours().getEnseignant().getId() == enseignantId)
                .collect(Collectors.toList());
        if (seances.isEmpty()) {
            System.out.println("Aucune séance pour cet enseignant.");
            return;
        }
        seances.forEach(System.out::println);
    }

    // ✅ Planning par groupe
    public void afficherPlanningParGroupe(int groupeId) {
        List<Seance> seances = seanceDAO.findAll().stream()
                .filter(s -> s.getGroupe().getIdGroupe() == groupeId)
                .collect(Collectors.toList());
        if (seances.isEmpty()) {
            System.out.println("Aucune séance pour ce groupe.");
            return;
        }
        seances.forEach(System.out::println);
    }

    // ✅ Afficher les réservations ponctuelles
    public void afficherReservations() {
        List<Reservation> reservations = reservationDAO.findAll();
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation.");
            return;
        }
        reservations.forEach(System.out::println);
    }

    // ✅ Réservations par utilisateur
    public void afficherReservationsParUtilisateur(Utilisateur utilisateur) {
        List<Reservation> reservations = reservationDAO.trouverParUtilisateur(utilisateur.getId());
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation pour " + utilisateur.getNom());
            return;
        }
        reservations.forEach(System.out::println);
    }
}