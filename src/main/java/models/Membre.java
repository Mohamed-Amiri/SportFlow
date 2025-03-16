package models;

public class Membre {
    private int id;
    private String nom;
    private String dateNaissance;
    private String sport;

    public Membre() {}

    public Membre(int id, String nom, String dateNaissance, String sport) {
        this.id = id;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.sport = sport;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    @Override
    public String toString() {
        return "Membre{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", sport='" + sport + '\'' +
                '}';
    }
}