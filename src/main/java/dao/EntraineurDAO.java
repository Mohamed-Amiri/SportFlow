package dao;

import models.Entraineur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntraineurDAO {
    private Connection connection;

    public EntraineurDAO(Connection connection) {
        this.connection = connection;
    }

    public void ajouterEntraineur(Entraineur entraineur) throws SQLException {
        String sql = "INSERT INTO entraineurs (nom, specialite) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entraineur.getNom());
            stmt.setString(2, entraineur.getSpecialite());
            stmt.executeUpdate();
        }
    }

    public List<Entraineur> getAllEntraineurs() throws SQLException {
        List<Entraineur> entraineurs = new ArrayList<>();
        String sql = "SELECT * FROM entraineurs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entraineurs.add(new Entraineur(rs.getInt("id"), rs.getString("nom"), rs.getString("specialite")));
            }
        }
        return entraineurs;
    }

    public void updateEntraineur(Entraineur entraineur) throws SQLException {
        String sql = "UPDATE entraineurs SET nom=?, specialite=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entraineur.getNom());
            stmt.setString(2, entraineur.getSpecialite());
            stmt.setInt(3, entraineur.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteEntraineur(int id) throws SQLException {
        String sql = "DELETE FROM entraineurs WHERE id=?";
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