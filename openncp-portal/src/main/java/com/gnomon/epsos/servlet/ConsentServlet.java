package com.gnomon.epsos.servlet;

import com.gnomon.epsos.model.Patient;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.model.User;
import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

public class ConsentServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger("consentServlet");

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        byte[] pdf;
        log.info("consentdocument");
        try {
            HttpSession session = req.getSession();

            Assertion hcpAssertion = (Assertion) session.getAttribute("hcpAssertion");
            log.info(hcpAssertion.getID());

            User user = (User) session.getAttribute("user");
            log.info(user.getEmailAddress());

            Patient patient = (Patient) session.getAttribute("patient");
            pdf = EpsosHelperService.getConsentReport(user.getLanguageId(), user.getFullName(), patient);

            res.setContentType("application/pdf");

            res.setHeader("Content-Disposition", "inline; filename=architect.pdf");
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");

            OutputStream OutStream = res.getOutputStream();
            OutStream.write(pdf);
            OutStream.flush();
            OutStream.close();

        } catch (Exception ex) {
            res.setContentType("text/html");

            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");

            OutputStream OutStream = res.getOutputStream();
            OutStream.write(ex.getMessage().getBytes());
            OutStream.flush();
            OutStream.close();
            log.error(ex.getMessage());
        }
    }
}
