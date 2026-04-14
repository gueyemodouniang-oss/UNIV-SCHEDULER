package dao.interfaces;

import modele.Matiere;
import java.util.List;

public interface IMatiereDAO {
    void ajouter(Matiere matiere);
    Matiere trouverParId(int id);
    List<Matiere> findAll();
    void modifier(Matiere matiere);
    void supprimer(int id);
}