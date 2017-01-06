package com.gnomon.epsos.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class RestAuthenticationFilter implements javax.servlet.Filter {

    public static final String AUTHENTICATION_HEADER = "Authorization";
    private static final Logger log = LoggerFactory.getLogger("RestAuthenticationFilter");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filter) throws IOException, ServletException, UnsupportedEncodingException {
        log.info("Rest Authentication Filter ...");
        String url = "";
        HttpServletRequest httpServletRequest = null;
        if (request instanceof HttpServletRequest) {
            url = ((HttpServletRequest) request).getRequestURL().toString();
            httpServletRequest = (HttpServletRequest) request;
        }

        if (httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            log.info("OPTIONS METHOD ACCEPTED");
            if (response instanceof HttpServletResponse) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse
                        .setStatus(HttpServletResponse.SC_ACCEPTED);
            }
            return;
        }

        log.info("go into authentication filter: " + url);
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;

            String authCredentials = httpServletRequest
                    .getHeader(AUTHENTICATION_HEADER);

            log.info(AUTHENTICATION_HEADER + ": " + authCredentials);
            // better injected
            AuthenticationService authenticationService = new AuthenticationService();

            boolean authenticationStatus = false;
            try {
                authenticationStatus = authenticationService
                        .authenticate(authCredentials);
                log.info("Authentication status; " + authenticationStatus);
            } catch (ParseException ex) {
                log.error(null, ex);
            }
            if (authenticationStatus) {
                filter.doFilter(request, response);
            } else {
                if (httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
                    if (response instanceof HttpServletResponse) {
                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse
                                .setStatus(HttpServletResponse.SC_ACCEPTED);
                    }
                } else {
                    if (response instanceof HttpServletResponse) {
                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse
                                .setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                }
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
