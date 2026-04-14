package dao.interfaces;

import modele.Utilisateurs.Utilisateur;
import java.util.List;

public interface IUtilisateurDAO {
    void ajouter(Utilisateur utilisateur);
    Utilisateur trouverParId(int id);
    List<Utilisateur> findAll();
    void modifier(Utilisateur utilisateur);
    void supprimer(int id);
    Utilisateur login(String email, String motDePasse);
}