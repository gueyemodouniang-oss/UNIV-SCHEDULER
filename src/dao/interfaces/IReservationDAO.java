package dao.interfaces;

import modele.Reservation;
import modele.enums.StatutReservation;
import java.util.List;

public interface IReservationDAO {
    void ajouter(Reservation reservation);
    Reservation trouverParId(int id);
    List<Reservation> findAll();
    void modifier(Reservation reservation);
    void supprimer(int id);
    List<Reservation> trouverParUtilisateur(int utilisateurId);
    List<Reservation> trouverParSalle(int salleId);
    List<Reservation> trouverParStatut(StatutReservation statut);
    boolean verifierConflit(int salleId, int creneauId, String date);
    void changerStatut(int idReservation, StatutReservation statut);
}