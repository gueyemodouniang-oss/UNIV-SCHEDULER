package dao.interfaces;

import modele.Groupe;
import java.util.List;

public interface IGroupeDAO {
    void ajouter(Groupe groupe);
    Groupe trouverParId(int id);
    List<Groupe> findAll();
    void modifier(Groupe groupe);
    void supprimer(int id);
    List<Groupe> trouverParAnnee(int anneeId);
}