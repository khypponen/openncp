package com.gnomon.stork.rest;

import com.google.gson.Gson;
import com.liferay.portal.datamodel.StorkResponse;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.Constants;
import com.liferay.portal.security.auth.StorkAutoLogin;
import com.liferay.portal.security.auth.StorkHelper;
import com.liferay.portal.security.auth.StorkProperties;
import com.liferay.portal.stork.util.PatientSearchAttributes;
import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import eu.europa.ec.joinup.ecc.openstork.utils.StorkUtils;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.opensaml.saml2.core.Assertion;

/**
 *
 * @author karkaletsis
 *
 */
@Path("/stork")
public class StorkRestService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("StorkRestService");

    @Context
    private HttpServletRequest servletRequest;

//    @GET
//    @Path("/{param}")
//    public Response test(@Context HttpServletRequest request, @PathParam("param") String msg) {
//        String ip = request.getRemoteAddr();
//        log.info("IP ADDRESS IS : " + ip);
//        String output = "OpenNCP says : " + msg;
//        return Response.status(200).entity(output).build();
//    }
    @GET
    @Path("/peps/url/get")
    public Response getPepsURL() throws SystemException {
        String pepsurl = StorkHelper.getPepsURL();
        log.info("pepsurl : " + pepsurl);
        return Response.status(200).entity(pepsurl).build();
    }

    @GET
    @Path("/peps/country/get")
    public Response getPepsCountry() throws SystemException {
        String pepscountry = StorkHelper.getPepsCountry();
        log.info("pepscountry : " + pepscountry);
        return Response.status(200).entity(pepscountry).build();
    }

    @POST
    @Path("/peps/saml/decode")
    public Response decodeSaml(
            @FormParam("SAMLResponse") String SAMLResponse) throws UnsupportedEncodingException {
        String ret = "";
        System.out.println("DECODE SAML: Getting saml attributes");
        System.out.println("SAML RESPONSE IS : " + SAMLResponse);
        Map<String, String> ehpattributes = new HashMap();

        byte[] decSamlToken = PEPSUtil.decodeSAMLToken(SAMLResponse);
        String samlResponseXML = new String(decSamlToken);
        System.out.println("SAML RESPONSE IS : " + samlResponseXML);
        String host = (String) servletRequest.getRemoteHost();
        System.out.println("HOST : " + host);

        STORKAuthnResponse authnResponse = null;
        IPersonalAttributeList attrs = null;
        STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);
        try {
            authnResponse = engine.validateSTORKAuthnResponseWithQuery(decSamlToken, host);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StorkResponse resp = new StorkResponse();
        if (Validator.isNotNull(authnResponse)) {
            Assertion onBehalf = StorkUtils.convertOnBehalfStorktoHcpAssertion(authnResponse);
            Map<String, String> onbehalfattrs = StorkUtils.getRepresentedPersonInformation(authnResponse);
            Map<String, String> onbehalfdemattrs = StorkUtils.getRepresentedDemographics(authnResponse);

            resp.setRepresentedPersonalData(onbehalfattrs);
            resp.setRepresentedDemographicsData(onbehalfdemattrs);
            try {
                log.info("###############: " + onbehalfattrs.size());
            } catch (Exception e) {
                log.error("Error getting onbehalf attributes");
            }
            if (authnResponse.isFail()) {
                log.error("Problem with response");
            } else {
                attrs = authnResponse.getTotalPersonalAttributeList();
                if (attrs.isEmpty()) {
                    attrs = authnResponse.getPersonalAttributeList();
                }
            }
        }

        StorkAutoLogin sal = new StorkAutoLogin();
        String hcpInfo = sal.getSamlValue(attrs, "isHealthCareProfessional");
        String hcpRole = sal.getHCPRoleString(hcpInfo);
        String epsosRole = sal.getEpsosRole(hcpRole);
        resp.setGivenName(sal.getSamlValue(attrs, "givenName"));
        resp.setEmail(sal.getSamlValue(attrs, "eMail"));
        resp.seteIdentifier(sal.getSamlValue(attrs, "eIdentifier"));
        resp.setSurname(sal.getSamlValue(attrs, "surname"));
        resp.setHcpRole(hcpRole);
        resp.setHcpInfo(hcpInfo);
        resp.setEpsosRole(epsosRole);
        resp.setAttrs(attrs);

        String countryCode = ConfigurationManagerService.getInstance().getProperty("COUNTRY_CODE");
        System.out.println("The country code is :" + countryCode);
        System.out.println("Reading the required attributes from International Search Mask");
        Map<String, String> attributes = PatientSearchAttributes.getRequiredAttributesByCountry(countryCode);
        Iterator it = attributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println("$$$$ " + pairs.getKey() + " = " + pairs.getValue());
            try {
                String attrName = pairs.getValue().toString(); //"2.16.470.1.100.1.1.1000.990.1";
                String storkKey = pairs.getKey().toString();
                String value = sal.getSamlValue(storkKey);
                try {
                    if (storkKey.equalsIgnoreCase("eIdentifier")) {
                        String[] temp = value.split("/");
                        value = temp[2];
                    } else {
                        value = sal.getSamlValue(storkKey);
                    }
                } catch (Exception e) {
                    System.out.println("Problem with eIdentifier " + value);
                }

                System.out.println("Adding attribute :" + attrName + " with value " + value);
                ehpattributes.put(attrName, value);
                resp.setAttributes(ehpattributes);

            } catch (Exception e) {
                e.printStackTrace();
            }
            ret = new Gson().toJson(resp);
            System.out.println(ret);
        }

        return Response.status(200).entity(ret).build();
    }

    @POST
    @Path("/peps/saml/create")
    public Response createSaml(
            @FormParam("providerName") String providerName,
            @FormParam("spSector") String spSector,
            @FormParam("spApplication") String spApplication,
            @FormParam("spCountry") String spCountry,
            @FormParam("spQaLevel") String spQaLevel,
            @FormParam("pepsURL") String pepsURL,
            @FormParam("loginURL") String loginURL,
            @FormParam("spReturnURL") String spReturnURL,
            @FormParam("spPersonalAttributes") String spPersonalAttributes,
            @FormParam("spBusinessAttributes") String spBusinessAttributes,
            @FormParam("spLegalAttributes") String spLegalAttributes) throws SystemException {
        StorkProperties properties = new StorkProperties();
        properties.setLoginURL(loginURL);
        properties.setPepsURL(pepsURL);
        properties.setProviderName(providerName);
        properties.setSpApplication(spApplication);
        properties.setSpPersonalAttributes(spPersonalAttributes);
        properties.setSpBusinessAttributes(spBusinessAttributes);
        properties.setSpCountry(spCountry);
        properties.setSpLegalAttributes(spLegalAttributes);
        properties.setSpQaLevel(spQaLevel);
        properties.setSpReturnURL(spReturnURL);
        properties.setSpSector(spSector);

        String saml = StorkHelper.createStorkSAML(properties);
        log.info("saml : " + saml);
        return Response.status(200).entity(saml).build();
    }

}
