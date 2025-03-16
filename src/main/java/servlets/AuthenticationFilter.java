package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*") // Intercept all requests
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code (optional)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        // Allow access to login page, LoginServlet, LogoutServlet, and static resources without authentication
        if (uri.equals(contextPath + "/login.jsp") ||
                uri.equals(contextPath + "/LoginServlet") ||
                uri.equals(contextPath + "/LogoutServlet") ||
                uri.startsWith(contextPath + "/css") ||  // Corrected to startsWith for directories
                uri.startsWith(contextPath + "/js") ||
                uri.startsWith(contextPath + "/images")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the user is authenticated (session attribute exists)
        HttpSession session = httpRequest.getSession(false); // Don't create a session if one doesn't exist
        if (session != null && session.getAttribute("userId") != null) {
            // User is authenticated, proceed to role-based access control
            String userRole = (String) session.getAttribute("userRole"); // Retrieve user role from session

            //Role-Based Access Control: Apply the logic here

            //Example, Restricting Access
            if ("membre".equals(userRole) && (uri.equals(contextPath + "/entraineurs") || uri.equals(contextPath + "/EntraineurServlet"))) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/membres"); // Or an error page
                return;
            }

            // Entraineur can only access /entraineurs and /SeanceServlet
            if ("entraineur".equals(userRole) && (uri.equals(contextPath + "/membres") || uri.equals(contextPath + "/MembreServlet"))) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/entraineurs");
                return;
            }

            chain.doFilter(request, response); //User Authorized
        } else {
            // User is not authenticated, redirect to the login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code (optional)
    }
}