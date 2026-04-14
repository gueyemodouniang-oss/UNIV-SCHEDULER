package dao.interfaces;

import modele.Cours;
import java.util.List;

public interface ICoursDAO {
    void ajouter(Cours cours);
    Cours trouverParId(int id);
    List<Cours> findAll();
    void modifier(Cours cours);
    void supprimer(int id);
    List<Cours> trouverParEnseignant(int enseignantId);
    List<Cours> trouverParGroupe(int groupeId);
}