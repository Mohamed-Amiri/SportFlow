package servlets;

import dao.EntraineurDAO;
import models.Entraineur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/entraineurs")
public class EntraineurServlet extends HttpServlet {
    private EntraineurDAO entraineurDAO;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            // Load the database driver (if not already loaded)
            Class.forName("com.mysql.cj.jdbc.Driver"); // Replace with your driver classname

            // Establish a database connection
            String dbUrl = "jdbc:mysql://localhost:3306/your_database_name"; // Replace with your database URL
            String dbUser = "your_db_user"; // Replace with your database username
            String dbPassword = "your_db_password"; // Replace with your database password
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            entraineurDAO = new EntraineurDAO(connection); // Pass the connection to the DAO

        } catch (ClassNotFoundException | SQLException e) {
            // Handle driver loading or connection errors appropriately.
            e.printStackTrace(); // For debugging.  In production, log the error.
            throw new ServletException("Failed to initialize database connection", e); // Rethrow as ServletException to stop servlet initialization.
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Entraineur> entraineurs = entraineurDAO.getAllEntraineurs();
            request.setAttribute("entraineurs", entraineurs);
            request.getRequestDispatcher("/views/entraineurs.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/views/entraineurs.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                String nom = request.getParameter("nom");
                String specialite = request.getParameter("specialite");

                // Validate input data!  For example:
                if (nom == null || nom.trim().isEmpty() || specialite == null || specialite.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Nom and Specialite are required.");
                    doGet(request, response); // Redisplay the form with the error message
                    return;
                }
                entraineurDAO.ajouterEntraineur(new Entraineur(0, nom, specialite));
            } else if ("update".equals(action)) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid ID format.");
                    doGet(request, response);
                    return;
                }
                String nom = request.getParameter("nom");
                String specialite = request.getParameter("specialite");

                // Validate input data!  For example:
                if (nom == null || nom.trim().isEmpty() || specialite == null || specialite.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Nom and Specialite are required.");
                    doGet(request, response); // Redisplay the form with the error message
                    return;
                }

                entraineurDAO.updateEntraineur(new Entraineur(id, nom, specialite));
            } else if ("delete".equals(action)) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid ID format.");
                    doGet(request, response);
                    return;
                }
                entraineurDAO.deleteEntraineur(id);
            }
        } catch (SQLException e) {
            // Handle database errors!
            e.printStackTrace(); // Log the error
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            doGet(request, response); // Redisplay the form with the error message
            return;
        }

        response.sendRedirect(request.getContextPath() + "/entraineurs"); // Use a context-relative URL
    }
}