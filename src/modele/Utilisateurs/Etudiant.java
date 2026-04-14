package modele.Utilisateurs;

import modele.enums.Role;

public class Etudiant extends Utilisateur {

    private String matricule;
    private String filiere;
    private String niveau;
    private int groupeId;

    public Etudiant(int id, String prenom, String nom, String email,
                    String motDePasse, Role role, String matricule,
                    String filiere, String niveau) {
        super(id, prenom, nom, email, motDePasse, role);
        this.matricule = matricule;
        this.filiere   = filiere;
        this.niveau    = niveau;
        this.groupeId  = 0;
    }

    @Override
    public String getPermission() {
        return "ETUDIANT : consulter l'emploi du temps";
    }

    public String getMatricule() { return matricule; }
    public String getFiliere()   { return filiere; }
    public String getNiveau()    { return niveau; }
    public int    getGroupeId()  { return groupeId; }

    public void setMatricule(String matricule) { this.matricule = matricule; }
    public void setFiliere(String filiere)     { this.filiere   = filiere; }
    public void setNiveau(String niveau)       { this.niveau    = niveau; }
    public void setGroupeId(int groupeId)      { this.groupeId  = groupeId; }

    @Override
    public String toString() {
        return "Etudiant{matricule='" + matricule + "', filiere='" +
                filiere + "', niveau='" + niveau + "'} " + super.toString();
    }
}