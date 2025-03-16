package models;

public class Seance {
    private int id;
    private int idMembre;
    private int idEntraineur;
    private String dateHeure;

    public Seance() {}

    public Seance(int id, int idMembre, int idEntraineur, String dateHeure) {
        this.id = id;
        this.idMembre = idMembre;
        this.idEntraineur = idEntraineur;
        this.dateHeure = dateHeure;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMembre() { return idMembre; }
    public void setIdMembre(int idMembre) { this.idMembre = idMembre; }
    public int getIdEntraineur() { return idEntraineur; }
    public void setIdEntraineur(int idEntraineur) { this.idEntraineur = idEntraineur; }
    public String getDateHeure() { return dateHeure; }
    public void setDateHeure(String dateHeure) { this.dateHeure = dateHeure; }

    @Override
    public String toString() {
        return "Seance{" +
                "id=" + id +
                ", idMembre=" + idMembre +
                ", idEntraineur=" + idEntraineur +
                ", dateHeure='" + dateHeure + '\'' +
                '}';
    }
}