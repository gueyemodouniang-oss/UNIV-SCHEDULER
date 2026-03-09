package service;

import modele.Reservation;
import modele.Salle;
import modele.Utilisateurs.Utilisateur;
import modele.enums.Jour;

import java.util.List;

public class Planning {

    private List<Reservation> reservations;

    public Planning(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    // Afficher tout le planning
    public void afficherPlanning() {

        if(reservations.isEmpty()){
            System.out.println("Aucune réservation.");
            return;
        }

        for(Reservation r : reservations){
            System.out.println(r);
        }
    }

    // Planning par jour
    public void afficherPlanningParJour(Jour jour){

        for(Reservation r : reservations){

            if(r.getJour() == jour){
                System.out.println(r);
            }

        }
    }

    // Planning par salle
    public void afficherPlanningParSalle(Salle salle){

        for(Reservation r : reservations){

            if(r.getSalle().equals(salle)){
                System.out.println(r);
            }

        }
    }

    // Planning par utilisateur
    public void afficherPlanningParUtilisateur(Utilisateur utilisateur){

        for(Reservation r : reservations){

            if(r.getUtilisateur().equals(utilisateur)){
                System.out.println(r);
            }

        }
    }
}