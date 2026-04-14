package dao.interfaces;

import modele.Equipement;
import java.util.List;

public interface IEquipementDAO {
    List<Equipement> findAll();
    void ajouter(Equipement eq);
    void modifier(Equipement eq);
    void supprimer(int id);
    List<Equipement> trouverParSalle(int salleId);
    void assignerEquipement(int salleId, int equipementId);
    void retirerEquipement(int salleId, int equipementId);
}