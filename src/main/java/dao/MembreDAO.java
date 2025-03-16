package dao;

import models.Membre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {
    private Connection connection;

    public MembreDAO(Connection connection) {
        this.connection = connection;
    }

    public void ajouterMembre(Membre membre) throws SQLException {
        String sql = "INSERT INTO membres (nom, dateNaissance, sport) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getDateNaissance());
            stmt.setString(3, membre.getSport());
            stmt.executeUpdate();
        }
    }

    public List<Membre> getAllMembres() throws SQLException {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membres";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                membres.add(new Membre(rs.getInt("id"), rs.getString("nom"), rs.getString("dateNaissance"), rs.getString("sport")));
            }
        }
        return membres;
    }

    public void updateMembre(Membre membre) throws SQLException {
        String sql = "UPDATE membres SET nom=?, dateNaissance=?, sport=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getDateNaissance());
            stmt.setString(3, membre.getSport());
            stmt.setInt(4, membre.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteMembre(int id) throws SQLException {
        String sql = "DELETE FROM membres WHERE id=?";
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