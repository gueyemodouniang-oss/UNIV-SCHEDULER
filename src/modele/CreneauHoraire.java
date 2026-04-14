package modele;

import modele.enums.Jour;
import java.time.LocalTime;

public class CreneauHoraire {

    private int idCreneau;
    private Jour jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    // Constructeur
    public CreneauHoraire(int idCreneau, Jour jour, LocalTime heureDebut, LocalTime heureFin) {
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

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    // Setters
    public void setJour(Jour jour) {
        this.jour = jour;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    @Override
    public String toString() {
        return jour + " " + heureDebut + " - " + heureFin;
    }
}