package servlets;

import dao.SeanceDAO;
import models.Seance;

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

@WebServlet("/seances")
public class SeanceServlet extends HttpServlet {
    private SeanceDAO seanceDAO;
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

            seanceDAO = new SeanceDAO(connection); // Pass the connection to the DAO

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
            List<Seance> seances = seanceDAO.getAllSeances();
            request.setAttribute("seances", seances);
            request.getRequestDispatcher("/views/seances.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/views/seances.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                int idMembre;
                int idEntraineur;
                try {
                    idMembre = Integer.parseInt(request.getParameter("idMembre"));
                    idEntraineur = Integer.parseInt(request.getParameter("idEntraineur"));
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid ID format for Membre or Entraineur.");
                    doGet(request, response);
                    return;
                }
                String dateHeure = request.getParameter("dateHeure");
                if (dateHeure == null || dateHeure.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Date and Time are required.");
                    doGet(request, response);
                    return;
                }
                seanceDAO.ajouterSeance(new Seance(0, idMembre, idEntraineur, dateHeure));
            } else if ("update".equals(action)) {
                int id;
                int idMembre;
                int idEntraineur;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    idMembre = Integer.parseInt(request.getParameter("idMembre"));
                    idEntraineur = Integer.parseInt(request.getParameter("idEntraineur"));
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid ID format.");
                    doGet(request, response);
                    return;
                }
                String dateHeure = request.getParameter("dateHeure");
                if (dateHeure == null || dateHeure.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Date and Time are required.");
                    doGet(request, response);
                    return;
                }
                seanceDAO.updateSeance(new Seance(id, idMembre, idEntraineur, dateHeure));
            } else if ("delete".equals(action)) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid ID format.");
                    doGet(request, response);
                    return;
                }
                seanceDAO.deleteSeance(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/seances");
    }
}