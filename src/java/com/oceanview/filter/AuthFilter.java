package com.oceanview.filter;

import com.oceanview.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Set cache control for all responses to prevent back-button access after
        // logout
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        // Allow access to login page, assets, and login/logout servlets
        if (path.equals("/") ||
                path.equals("/index.html") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/images/") ||
                path.equals("/login") ||
                path.equals("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Unauthenticated
            if (path.endsWith(".html") || path.endsWith("/") || !path.contains(".")) {
                // If it's a page or directory, redirect to login
                res.sendRedirect(req.getContextPath() + "/index.html");
            } else {
                // If it's an API request, return 401
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.getWriter().write("{\"success\": false, \"message\": \"Authentication required\"}");
            }
        } else {
            // Authenticated - Check role-based access for /admin/
            if (path.startsWith("/admin/") && !"ADMIN".equals(user.getRole())) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
