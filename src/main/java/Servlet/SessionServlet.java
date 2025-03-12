package Servlet;

import Dao.SessionDAO;
import Model.Session;
import Model.Trainer;
import Model.Member;
import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Time;

@WebServlet("/sessions/*")
public class SessionServlet extends HttpServlet {
    private SessionDAO sessionDAO = new SessionDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            String trainerId = request.getParameter("trainerId");
            String memberId = request.getParameter("memberId");
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && !pathInfo.equals("/")) {
                // Get session by ID
                String[] splits = pathInfo.split("/");
                if (splits.length == 2) {
                    try {
                        int id = Integer.parseInt(splits[1]);
                        Session session = sessionDAO.findById(id);
                        if (session != null) {
                            response.getWriter().write(gson.toJson(session));
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }
                        return;
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid session ID");
                        return;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
                    return;
                }
            }

            if (trainerId != null) {
                try {
                    response.getWriter().write(gson.toJson(
                            sessionDAO.findByTrainerId(Integer.parseInt(trainerId))
                    ));
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid trainer ID");
                }
            } else if (memberId != null) {
                try {
                    response.getWriter().write(gson.toJson(
                            sessionDAO.findByMemberId(Integer.parseInt(memberId))
                    ));
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid member ID");
                }
            } else {
                response.getWriter().write(gson.toJson(sessionDAO.findAll()));
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Session session = new Session();

            String trainerIdParam = request.getParameter("trainerId");
            String memberIdParam = request.getParameter("memberId");
            String sessionDateParam = request.getParameter("sessionDate");
            String sessionTimeParam = request.getParameter("sessionTime");

            if (trainerIdParam == null || memberIdParam == null || sessionDateParam == null || sessionTimeParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }

            int trainerId = Integer.parseInt(trainerIdParam);
            int memberId = Integer.parseInt(memberIdParam);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date sessionDate = dateFormat.parse(sessionDateParam);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Time sessionTime = new Time(timeFormat.parse(sessionTimeParam).getTime());

            session.setTrainerId(trainerId);
            session.setMemberId(memberId);
            session.setSessionDate(sessionDate);
            session.setSessionTime(sessionTime);

            int id = sessionDAO.insert(session);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"id\": " + id + "}");


        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date or time format");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid trainerId or memberId");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing session ID");
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path format");
            return;
        }

        try {
            int id = Integer.parseInt(splits[1]);
            Session session = sessionDAO.findById(id);

            if (session == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found");
                return;
            }

            String trainerIdParam = request.getParameter("trainerId");
            String memberIdParam = request.getParameter("memberId");
            String sessionDateParam = request.getParameter("sessionDate");
            String sessionTimeParam = request.getParameter("sessionTime");

            if (trainerIdParam == null || memberIdParam == null || sessionDateParam == null || sessionTimeParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
                return;
            }

            int trainerId = Integer.parseInt(trainerIdParam);
            int memberId = Integer.parseInt(memberIdParam);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date sessionDate = dateFormat.parse(sessionDateParam);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            Time sessionTime = new Time(timeFormat.parse(sessionTimeParam).getTime());

            session.setId(id); // Set the session ID for updating.
            session.setTrainerId(trainerId);
            session.setMemberId(memberId);
            session.setSessionDate(sessionDate);
            session.setSessionTime(sessionTime);

            boolean updated = sessionDAO.update(session);

            if (updated) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update session");
            }

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date or time format");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid trainerId, memberId, or session ID");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing session ID");
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path format");
            return;
        }

        try {
            int id = Integer.parseInt(splits[1]);
            boolean deleted = sessionDAO.delete(id);

            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found or could not be deleted");
            }

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid session ID");
        }
    }
}