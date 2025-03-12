package Servlet;

import Dao.MemberDAO;
import Model.Member;
import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;

@WebServlet("/members/*")
public class MemberServlet extends HttpServlet {
    private MemberDAO memberDAO = new MemberDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all members
                response.getWriter().write(gson.toJson(memberDAO.findAll()));
            } else {
                // Get member by ID
                String[] splits = pathInfo.split("/");
                if (splits.length != 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                int id = Integer.parseInt(splits[1]);
                Member member = memberDAO.findById(id);
                if (member != null) {
                    response.getWriter().write(gson.toJson(member));
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
            Member member = new Member();
            member.setFirstName(request.getParameter("firstName"));
            member.setLastName(request.getParameter("lastName"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(request.getParameter("birthDate"));
            member.setBirthDate(birthDate);

            member.setSport(request.getParameter("sport"));

            int id = memberDAO.insert(member);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"id\": " + id + "}");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // Implement doPut() and doDelete() similarly
}