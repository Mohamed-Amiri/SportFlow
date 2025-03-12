package Dao;

import Model.Trainer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainerDAO implements GenericDAO<Trainer> {

    @Override
    public Trainer findById(int id) throws SQLException {
        String query = "SELECT * FROM trainers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractTrainerFromResultSet(rs);
            }
        }

        return null;
    }

    @Override
    public List<Trainer> findAll() throws SQLException {
        String query = "SELECT * FROM trainers";
        List<Trainer> trainers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                trainers.add(extractTrainerFromResultSet(rs));
            }
        }

        return trainers;
    }

    @Override
    public int insert(Trainer trainer) throws SQLException {
        String query = "INSERT INTO trainers (first_name, last_name, specialty) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trainer.getFirstName());
            stmt.setString(2, trainer.getLastName());
            stmt.setString(3, trainer.getSpecialty());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating trainer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating trainer failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public boolean update(Trainer trainer) throws SQLException {
        String query = "UPDATE trainers SET first_name = ?, last_name = ?, specialty = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, trainer.getFirstName());
            stmt.setString(2, trainer.getLastName());
            stmt.setString(3, trainer.getSpecialty());
            stmt.setInt(4, trainer.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM trainers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public List<Trainer> findBySpecialty(String specialty) throws SQLException {
        String query = "SELECT * FROM trainers WHERE specialty = ?";
        List<Trainer> trainers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, specialty);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                trainers.add(extractTrainerFromResultSet(rs));
            }
        }

        return trainers;
    }

    private Trainer extractTrainerFromResultSet(ResultSet rs) throws SQLException {
        Trainer trainer = new Trainer();
        trainer.setId(rs.getInt("id"));
        trainer.setFirstName(rs.getString("first_name"));
        trainer.setLastName(rs.getString("last_name"));
        trainer.setSpecialty(rs.getString("specialty"));
        return trainer;
    }
}
