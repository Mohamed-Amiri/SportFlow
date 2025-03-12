package Servlet;

import Dao.TrainerDAO;
import Model.Trainer;
import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/trainers/*")
public class TrainerServlet extends HttpServlet {
    private TrainerDAO trainerDAO = new TrainerDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all trainers or filter by specialty
                String specialty = request.getParameter("specialty");
                if (specialty != null) {
                    response.getWriter().write(gson.toJson(trainerDAO.findBySpecialty(specialty)));
                } else {
                    response.getWriter().write(gson.toJson(trainerDAO.findAll()));
                }
            } else {
                // Get trainer by ID
                String[] splits = pathInfo.split("/");
                if (splits.length != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                int id = Integer.parseInt(splits[1]);
                Trainer trainer = trainerDAO.findById(id);
                if (trainer != null) {
                    response.getWriter().write(gson.toJson(trainer));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Trainer trainer = new Trainer();
            trainer.setFirstName(request.getParameter("firstName"));
            trainer.setLastName(request.getParameter("lastName"));
            trainer.setSpecialty(request.getParameter("specialty"));

            int id = trainerDAO.insert(trainer);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"id\": " + id + "}");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int id = Integer.parseInt(splits[1]);
            Trainer trainer = trainerDAO.findById(id);
            if (trainer == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            trainer.setFirstName(request.getParameter("firstName"));
            trainer.setLastName(request.getParameter("lastName"));
            trainer.setSpecialty(request.getParameter("specialty"));

            boolean updated = trainerDAO.update(trainer);
            response.setStatus(updated ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (SQLException | NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int id = Integer.parseInt(splits[1]);
            boolean deleted = trainerDAO.delete(id);
            response.setStatus(deleted ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException | NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
