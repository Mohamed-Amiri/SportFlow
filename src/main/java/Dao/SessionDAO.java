package Dao;

import Model.Session;
import Model.Trainer;
import Model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO implements GenericDAO<Session> {

    private TrainerDAO trainerDAO = new TrainerDAO();
    private MemberDAO memberDAO = new MemberDAO();

    @Override
    public Session findById(int id) throws SQLException {
        String query = "SELECT * FROM sessions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractSessionFromResultSet(rs);
            }
        }

        return null;
    }

    @Override
    public List<Session> findAll() throws SQLException {
        String query = "SELECT * FROM sessions";
        List<Session> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }
        }

        return sessions;
    }

    @Override
    public int insert(Session session) throws SQLException {
        String query = "INSERT INTO sessions (trainer_id, member_id, session_date, session_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, session.getTrainerId());
            stmt.setInt(2, session.getMemberId());
            stmt.setDate(3, new java.sql.Date(session.getSessionDate().getTime()));
            stmt.setTime(4, session.getSessionTime());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating session failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating session failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public boolean update(Session session) throws SQLException {
        String query = "UPDATE sessions SET trainer_id = ?, member_id = ?, session_date = ?, session_time = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, session.getTrainerId());
            stmt.setInt(2, session.getMemberId());
            stmt.setDate(3, new java.sql.Date(session.getSessionDate().getTime()));
            stmt.setTime(4, session.getSessionTime());
            stmt.setInt(5, session.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM sessions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public List<Session> findByTrainerId(int trainerId) throws SQLException {
        String query = "SELECT * FROM sessions WHERE trainer_id = ?";
        List<Session> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, trainerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }
        }

        return sessions;
    }

    public List<Session> findByMemberId(int memberId) throws SQLException {
        String query = "SELECT * FROM sessions WHERE member_id = ?";
        List<Session> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }
        }

        return sessions;
    }

    public List<Session> findByDate(Date date) throws SQLException {
        String query = "SELECT * FROM sessions WHERE session_date = ?";
        List<Session> sessions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sessions.add(extractSessionFromResultSet(rs));
            }
        }

        return sessions;
    }

    private Session extractSessionFromResultSet(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getInt("id"));
        session.setTrainerId(rs.getInt("trainer_id"));
        session.setMemberId(rs.getInt("member_id"));
        session.setSessionDate(rs.getDate("session_date"));
        session.setSessionTime(rs.getTime("session_time"));

        // Load related entities
        session.setTrainer(trainerDAO.findById(session.getTrainerId()));
        session.setMember(memberDAO.findById(session.getMemberId()));

        return session;
    }
}
