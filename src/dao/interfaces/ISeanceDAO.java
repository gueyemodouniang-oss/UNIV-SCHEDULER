package dao.interfaces;

import modele.Seance;
import java.util.List;

public interface ISeanceDAO {
    void ajouter(Seance seance);
    Seance trouverParId(int id);
    List<Seance> findAll();
    void modifier(Seance seance);
    void supprimer(int id);
    List<Seance> trouverParCours(int coursId);
    List<Seance> trouverParSalle(int salleId);
    boolean verifierConflit(int salleId, int creneauId);
}