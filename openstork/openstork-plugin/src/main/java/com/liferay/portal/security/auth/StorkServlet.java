/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.util.PortalUtil;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author karkaletsis
 */
public class StorkServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(StorkServlet.class.getName());

    private final String USER_AGENT = "Mozilla/5.0";
    private String SAMLResponse;
    private String samlResponseXML;
    private String SAMLRequest;
    private String samlRequestXML;
    private ArrayList<PersonalAttribute> attrList;

    private HttpServletRequest request;
    private static Properties configs;
    private static String providerName;
    private static String homepage = "/SP/";
    private static String allowIP = "127.0.0.1";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StorkServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StorkServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check for ip address called this servlet
        String ipAddress = "";
        if (request.getHeader("x-forwarded-for") != null) {
            ipAddress = InetAddress.getByName(request.getHeader("x-forwarded-for")).getHostAddress();
        } else {
            ipAddress = request.getRemoteAddr();
        }

        System.out.println("IP-Addr: " + ipAddress);

        String STORK_ENABLED = "stork.enabled";
        Company company;
        try {
            company = PortalUtil.getCompany(request);
            String storkEnabled = PrefsPropsUtil.getString(company.getCompanyId(), STORK_ENABLED, "false");
            System.out.println("storkEnabled:" + PrefsPropsUtil.getString(company.getCompanyId(), STORK_ENABLED, "false"));
            System.out.println("provider.name:" + PrefsPropsUtil.getString(company.getCompanyId(), "provider.name", ""));
            System.out.println("sp.sector:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.sector", ""));
            System.out.println("sp.aplication:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.aplication", ""));
            System.out.println("sp.country:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.country", ""));
            System.out.println("sp.qaalevel:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.qaalevel", ""));
            System.out.println("peps.url:" + PrefsPropsUtil.getString(company.getCompanyId(), "peps.url", ""));
            System.out.println("stork.login.url:" + PrefsPropsUtil.getString(company.getCompanyId(), "stork.login.url", ""));
            System.out.println("sp.mandatory.personal.attributes:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.mandatory.personal.attributes", ""));
            System.out.println("sp.mandatory.business.attributes:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.mandatory.business.attributes", ""));
            System.out.println("sp.mandatory.legal.attributes:" + PrefsPropsUtil.getString(company.getCompanyId(), "sp.mandatory.legal.attributes", ""));

            request.getRequestDispatcher("/stork.jsp").forward(request, response);
        } catch (PortalException ex) {
            //java.util.logging.LoggerFactory.getLogger(StorkServlet.class.getName()).log(Level.SEVERE, null, ex);
            log.error(null, ex);
        } catch (SystemException ex) {
            //java.util.logging.LoggerFactory.getLogger(StorkServlet.class.getName()).log(Level.SEVERE, null, ex);
            log.error(null, ex);
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("##################### Stork Servlet Post ...");
        providerName = PropsUtil.get("provider.name");
        SAMLResponse = request.getParameter("SAMLResponse");
        byte[] decSamlToken = PEPSUtil.decodeSAMLToken(SAMLResponse);
        samlResponseXML = new String(decSamlToken);
        System.out.println("SAML IS : " + samlResponseXML);

        STORKAuthnResponse authnResponse = null;
        IPersonalAttributeList personalAttributeList = null;
        STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);
        String host = (String) request.getRemoteHost();
        log.info("HOST IS : " + host);
        try {
            authnResponse = engine.validateSTORKAuthnResponseWithQuery(decSamlToken, host);
        } catch (STORKSAMLEngineException e) {
            log.error("######################" + e.getMessage());
        }
        if (authnResponse.isFail()) {
            log.error("Problem with response");
        } else {
            personalAttributeList = authnResponse.getPersonalAttributeList();
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StorkServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("Servlet StorkServlet at " + personalAttributeList.get("givenName"));
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        StorkServlet.log = log;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public static String getHomepage() {
        return homepage;
    }

    public static void setHomepage(String homepage) {
        StorkServlet.homepage = homepage;
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public String doSubmit(String url, Map<String, String> data) throws Exception {
        URL siteUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("referer", "localhost");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        DataOutputStream out = new DataOutputStream(conn.getOutputStream());

        Set keys = data.keySet();
        Iterator keyIter = keys.iterator();
        String content = "";
        for (int i = 0; keyIter.hasNext(); i++) {
            Object key = keyIter.next();
            if (i != 0) {
                content += "&";
            }
            content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
        }
        //System.out.println(content);
        out.writeBytes(content);
        out.flush();
        out.close();
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
}
