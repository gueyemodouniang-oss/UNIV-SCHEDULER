package modele;

public class Groupe {

    private int idGroupe;
    private String nomGroupe;
    private int effectif;
    private int anneeId;

    // Constructeur complet
    public Groupe(int idGroupe, String nomGroupe, int effectif, int anneeId) {
        this.idGroupe = idGroupe;
        this.nomGroupe = nomGroupe;
        this.effectif = effectif;
        this.anneeId = anneeId;
    }

    // ✅ Constructeur sans anneeId pour CoursDAO
    public Groupe(int idGroupe, String nomGroupe, int effectif) {
        this.idGroupe = idGroupe;
        this.nomGroupe = nomGroupe;
        this.effectif = effectif;
        this.anneeId = 0;
    }

    public int getIdGroupe() { return idGroupe; }
    public String getNomGroupe() { return nomGroupe; }
    public int getEffectif() { return effectif; }
    public int getAnneeId() { return anneeId; }

    public void setNomGroupe(String nomGroupe) { this.nomGroupe = nomGroupe; }
    public void setEffectif(int effectif) { this.effectif = effectif; }
    public void setAnneeId(int anneeId) { this.anneeId = anneeId; }

    @Override
    public String toString() {
        return nomGroupe + " (" + effectif + " étudiants)";
    }
}