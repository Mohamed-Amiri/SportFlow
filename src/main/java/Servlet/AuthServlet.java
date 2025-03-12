package Servlet;

import Dao.UserDAO;
import Model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        if ("/login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                if (userDAO.authenticate(username, password)) {
                    User user = userDAO.findByUsername(username);

                    // Prevent session fixation
                    HttpSession session = request.getSession();
                    session.invalidate();  // Invalidate the old session
                    session = request.getSession(true); // Get a new session

                    session.setAttribute("user", user);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.sendRedirect("/home.jsp"); // Redirect after login
                } else {
                    request.setAttribute("loginError", "Invalid username or password"); // Set error message
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    //Consider forwarding to login page rather than just throwing unauthorized:
                    //RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
                    //rd.forward(request, response);
                }
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else if ("/logout".equals(action)) {
            HttpSession session = request.getSession();
            session.invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/login.jsp"); // Redirect after logout
        } else {
            // Handle unknown paths
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid path");
        }
    }
}