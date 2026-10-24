package com.example.exchange.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.exchange.helper.CsrfTokens;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final String[] authorizedUris = new String[] { "user", "listing", "my-offers", "offer" };
    private static final String[] notAuthorizedUris = new String[] { "login", "register" };
    private static final String defaultUri = "/exchange/";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setAttribute("origin", req.getRequestURI());

        String reqUri = req.getRequestURI();
        reqUri = Objects.equals(reqUri, defaultUri) ? defaultUri + "home" : reqUri;

        HttpSession session = req.getSession(true);
        String csrfToken = (String) session.getAttribute(CsrfTokens.SESSION_ATTRIBUTE);
        if (csrfToken == null) {
            csrfToken = CsrfTokens.newToken();
            session.setAttribute(CsrfTokens.SESSION_ATTRIBUTE, csrfToken);
        }
        req.setAttribute(CsrfTokens.SESSION_ATTRIBUTE, csrfToken);

        if (isStateChanging(req.getMethod())) {
            String supplied = req.getHeader(CsrfTokens.HEADER_NAME);
            if (supplied == null || supplied.isEmpty()) {
                supplied = req.getParameter(CsrfTokens.FORM_PARAMETER);
            }
            if (!CsrfTokens.isValid(csrfToken, supplied)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing CSRF token");
                return;
            }
        }

        Object userSession = session.getAttribute("user");

        if (isAuthorizedUri(reqUri) && userSession == null) {
            res.sendRedirect("login");
            return;
        }

        if (isNotAuthorizedUri(reqUri) && userSession != null) {
            res.sendRedirect("home");
            return;
        }

        if (userSession != null) {
            req.setAttribute("authenticateduser", userSession);
        } else {
            req.removeAttribute("authenticateduser");
        }

        chain.doFilter(request, response);
    }

    private boolean isStateChanging(String method) {
        return !("GET".equalsIgnoreCase(method)
                || "HEAD".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method)
                || "TRACE".equalsIgnoreCase(method));
    }

    private boolean isAuthorizedUri(String uri) {
        for (String link : authorizedUris) {
            if (uri.startsWith(defaultUri + link))
                return true;
        }

        return false;
    }

    private boolean isNotAuthorizedUri(String uri) {
        for (String link : notAuthorizedUris) {
            if (uri.startsWith(defaultUri + link))
                return true;
        }

        return false;
    }
}
