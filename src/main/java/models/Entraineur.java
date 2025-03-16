package models;

public class Entraineur {
    private int id;
    private String nom;
    private String specialite;

    public Entraineur() {}

    public Entraineur(int id, String nom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    @Override
    public String toString() {
        return "Entraineur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", specialite='" + specialite + '\'' +
                '}';
    }
}