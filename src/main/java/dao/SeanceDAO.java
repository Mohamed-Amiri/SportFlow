package dao;

import models.Seance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {
    private Connection connection;

    public SeanceDAO(Connection connection) {
        this.connection = connection;
    }

    public void ajouterSeance(Seance seance) throws SQLException {
        String sql = "INSERT INTO seances (idMembre, idEntraineur, dateHeure) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, seance.getIdMembre());
            stmt.setInt(2, seance.getIdEntraineur());
            stmt.setString(3, seance.getDateHeure());
            stmt.executeUpdate();
        }
    }

    public List<Seance> getAllSeances() throws SQLException {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM seances";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seances.add(new Seance(rs.getInt("id"), rs.getInt("idMembre"), rs.getInt("idEntraineur"), rs.getString("dateHeure")));
            }
        }
        return seances;
    }

    public void updateSeance(Seance seance) throws SQLException {
        String sql = "UPDATE seances SET idMembre=?, idEntraineur=?, dateHeure=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, seance.getIdMembre());
            stmt.setInt(2, seance.getIdEntraineur());
            stmt.setString(3, seance.getDateHeure());
            stmt.setInt(4, seance.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteSeance(int id) throws SQLException {
        String sql = "DELETE FROM seances WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}