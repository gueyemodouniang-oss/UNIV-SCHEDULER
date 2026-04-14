package dao.interfaces;

import modele.Batiment;
import java.util.List;

public interface IBatimentDAO {
    void ajouter(Batiment batiment);
    Batiment trouverParId(int id);
    List<Batiment> findAll();
    void modifier(Batiment batiment);
    void supprimer(int id);
}