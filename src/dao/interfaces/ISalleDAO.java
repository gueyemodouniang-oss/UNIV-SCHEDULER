package dao.interfaces;

import modele.Salle;
import modele.enums.TypeSalle;
import java.util.List;

public interface ISalleDAO {
    void ajouter(Salle salle);
    Salle trouverParId(int id);
    List<Salle> findAll();
    void modifier(Salle salle);
    void supprimer(int id);
    List<Salle> trouverSallesDisponibles(int creneauId, String date);
    List<Salle> trouverParType(TypeSalle type);
    List<Salle> trouverParCapaciteMin(int capaciteMin);
}