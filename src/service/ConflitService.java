package service;

import modele.Reservation;

public class ConflitService {

    public boolean verifierConflit(Reservation r1, Reservation r2) {

        if(r1.getSalle().equals(r2.getSalle()) &&
                r1.getJour() == r2.getJour() &&
                r1.getCreneauHoraire().equals(r2.getCreneauHoraire())) {

            return true;
        }

        return false;
    }
}