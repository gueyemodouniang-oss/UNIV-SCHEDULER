package dao.interfaces;

import modele.CreneauHoraire;
import modele.enums.Jour;
import java.util.List;

public interface ICreneauHoraireDAO {
    void ajouter(CreneauHoraire creneau);
    CreneauHoraire trouverParId(int id);
    List<CreneauHoraire> findAll();
    void modifier(CreneauHoraire creneau);
    void supprimer(int id);
    List<CreneauHoraire> trouverParJour(Jour jour);
}
