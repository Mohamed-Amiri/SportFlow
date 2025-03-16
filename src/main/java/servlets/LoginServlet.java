package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Database connection details (replace with your actual values)
        String dbUrl = "jdbc:mysql://localhost:3306/your_database_name";
        String dbUser = "your_db_user";
        String dbPassword = "your_db_password";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the JDBC driver
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // SQL query to authenticate the user (replace with your actual query)
            String sql = "SELECT id, role FROM users WHERE username = ? AND password = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Authentication successful
                int userId = rs.getInt("id");
                String userRole = rs.getString("role");

                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);  // Store user ID in session
                session.setAttribute("userRole", userRole); // Store user role in session

                // Redirect based on role
                if ("membre".equals(userRole)) {
                    response.sendRedirect(request.getContextPath() + "/membres");
                } else if ("entraineur".equals(userRole)) {
                    response.sendRedirect(request.getContextPath() + "/entraineurs");
                } else {
                    // Default redirect (e.g., to a general dashboard)
                    response.sendRedirect(request.getContextPath() + "/");
                }
            } else {
                // Authentication failed
                request.setAttribute("errorMessage", "Invalid username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } finally {
            // Close database resources in a finally block
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}