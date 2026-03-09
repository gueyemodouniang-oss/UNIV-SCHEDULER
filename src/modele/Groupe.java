package modele;

public class Groupe {

    private int idGroupe;
    private String nomGroupe;
    private int effectif;

    public Groupe(int idGroupe, String nomGroupe, int effectif) {
        this.idGroupe = idGroupe;
        this.nomGroupe = nomGroupe;
        this.effectif = effectif;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public int getEffectif() {
        return effectif;
    }
}