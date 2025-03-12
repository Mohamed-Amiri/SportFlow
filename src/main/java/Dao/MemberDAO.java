package Dao;

import Model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO implements GenericDAO<Member> {

    @Override
    public Member findById(int id) throws SQLException {
        String query = "SELECT * FROM members WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractMemberFromResultSet(rs);
            }
        }

        return null;
    }

    @Override
    public List<Member> findAll() throws SQLException {
        String query = "SELECT * FROM members";
        List<Member> members = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
        }

        return members;
    }

    @Override
    public int insert(Member member) throws SQLException {
        String query = "INSERT INTO members (first_name, last_name, birth_date, sport) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setDate(3, new java.sql.Date(member.getBirthDate().getTime()));
            stmt.setString(4, member.getSport());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating member failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating member failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public boolean update(Member member) throws SQLException {
        String query = "UPDATE members SET first_name = ?, last_name = ?, birth_date = ?, sport = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setDate(3, new java.sql.Date(member.getBirthDate().getTime()));
            stmt.setString(4, member.getSport());
            stmt.setInt(5, member.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM members WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public List<Member> findBySport(String sport) throws SQLException {
        String query = "SELECT * FROM members WHERE sport = ?";
        List<Member> members = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sport);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
        }

        return members;
    }

    private Member extractMemberFromResultSet(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setBirthDate(rs.getDate("birth_date"));
        member.setSport(rs.getString("sport"));
        return member;
    }
}