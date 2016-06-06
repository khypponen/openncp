package com.gnomon.epsos.servlet;

import com.gnomon.epsos.MyServletContextListener;
import com.gnomon.epsos.model.Patient;
import com.gnomon.epsos.model.ViewResult;
import com.gnomon.epsos.service.EpsosHelperService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import epsos.ccd.gnomon.auditmanager.EventOutcomeIndicator;
import epsos.openncp.protocolterminator.ClientConnectorConsumer;
import epsos.openncp.protocolterminator.clientconnector.EpsosDocument1;
import epsos.openncp.protocolterminator.clientconnector.GenericDocumentCode;
import epsos.openncp.protocolterminator.clientconnector.SubmitDocumentResponse;
import eu.epsos.util.EvidenceUtils;
import eu.epsos.util.IheConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import tr.com.srdc.epsos.util.Constants;

public class DispenseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger("dispenseServlet");

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        SubmitDocumentResponse resp = null;
        log.info("dispensing ...");

        HttpSession session = req.getSession();
        String selectedCountry = (String) session.getAttribute("selectedCountry");
        Patient patient = (Patient) session.getAttribute("patient");
        Assertion hcpAssertion = (Assertion) session.getAttribute("hcpAssertion");
        Assertion trcAssertion = (Assertion) session.getAttribute("trcAssertion");
        User user = (User) session.getAttribute("user");

        byte[] edBytes = null;
        try {
            byte[] epBytes = (byte[]) session.getAttribute("epBytes");

            List<ViewResult> lines = EpsosHelperService.parsePrescriptionDocumentForPrescriptionLines(epBytes);

            String[] dispensedIds = new String[lines.size()];

            for (int i = 0; i < lines.size(); i++) {
                dispensedIds[i] = req.getParameter("dispensationid_" + i);
            }

            String language = ParamUtil.getString(req, "language");
            String fullname = "EPSOS PORTAL";

            try {
                fullname = user.getFullName();
            } catch (Exception e1) {
                log.error(ExceptionUtils.getStackTrace(e1));
            }

            if (dispensedIds != null) {
                List<String> dispensedIdsList = Arrays.asList(dispensedIds);

                ArrayList<ViewResult> dispensedLines = new ArrayList<ViewResult>();

                for (ViewResult line : lines) {
                    int id = line.getMainid();

                    String measures_id = req.getParameter("measures_" + id);
                    String dispensed_id = req.getParameter("dispensationid_" + id); //field1
                    String dispensedProduct = req.getParameter("dispensedProductValue_" + id);

                    if (Validator.isNull(dispensedProduct)) {
                        dispensedProduct = line.getField1() + "";
                    }

                    String dispensed_substitute = req.getParameter("dispense_" + id); // field3
                    boolean substitute = GetterUtil.getBoolean(dispensed_substitute, false);

                    String dispensed_quantity = req.getParameter("dispensedPackageSize_" + id); // field7 //lathos

                    if (Validator.isNull(dispensed_quantity)) {
                        dispensed_quantity = line.getField21() + "";
                    }

                    String dispensed_name = dispensedProduct;
                    String dispensed_strength = line.getField3() + "";
                    String dispensed_form = line.getField4() + "";
                    String dispensed_package = line.getField4() + ""; //request.getParameter("packaging2_"+id); // field6
                    String dispensed_nrOfPacks = line.getField8().toString();
                    String prescriptionid = line.getField14() + ""; // field9 //lathos
                    String materialid = line.getField19() + ""; // field10
                    String activeIngredient = line.getField2().toString();

                    ViewResult d_line = new ViewResult(
                            id,
                            dispensed_id,
                            dispensed_name,
                            substitute,
                            dispensed_strength,
                            dispensed_form,
                            dispensed_package,
                            dispensed_quantity,
                            dispensed_nrOfPacks,
                            prescriptionid,
                            materialid,
                            activeIngredient,
                            measures_id);

                    dispensedLines.add(d_line);
                }

                if (dispensedLines.size() > 0) {
                    edBytes = EpsosHelperService.generateDispensationDocumentFromPrescription2(epBytes, dispensedLines, user);
                }

                if (Validator.isNotNull(edBytes)) {
                    String serviceUrl = EpsosHelperService.getConfigProperty(EpsosHelperService.PORTAL_CLIENT_CONNECTOR_URL);

                    ClientConnectorConsumer proxy = MyServletContextListener.getClientConnectorConsumer();
                    GenericDocumentCode classCode = GenericDocumentCode.Factory.newInstance();
                    classCode.setNodeRepresentation(Constants.ED_CLASSCODE);
                    classCode.setSchema(IheConstants.ClASSCODE_SCHEME);
                    classCode.setValue(Constants.ED_TITLE);

                    GenericDocumentCode formatCode = GenericDocumentCode.Factory.newInstance();
                    formatCode.setSchema(IheConstants.DISPENSATION_FORMATCODE_CODINGSCHEMA);
                    formatCode.setNodeRepresentation(IheConstants.DISPENSATION_FORMATCODE_NODEREPRESENTATION);
                    formatCode.setValue(IheConstants.DISPENSATION_FORMATCODE_DISPLAYNAME);

                    EpsosDocument1 document = EpsosDocument1.Factory.newInstance();
                    document.setAuthor(user.getFullName());
                    Calendar cal = Calendar.getInstance();
                    document.setCreationDate(cal);
                    document.setDescription(Constants.ED_TITLE);
                    document.setTitle(Constants.ED_TITLE);
                    document.setUuid(EpsosHelperService.getUniqueId());
                    document.setClassCode(classCode);
                    document.setFormatCode(formatCode);
                    document.setBase64Binary(edBytes);

                    try {
                        EvidenceUtils.createEvidenceREMNRO(classCode.toString(),
                                "NI_XDR_" + classCode.getValue(),
                                new DateTime(),
                                EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                                "NI_XDR_" + classCode.getValue() + "_REQ",
                                trcAssertion.getID());
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                    }

                    resp = proxy.submitDocument(hcpAssertion, trcAssertion, selectedCountry, document, patient.getPatientDemographics());

                    if (Validator.isNotNull(resp)) {
                        try {
                            EvidenceUtils.createEvidenceREMNRR(classCode.toString(),
                                    "NI_XDR" + classCode.getValue(),
                                    new DateTime(),
                                    EventOutcomeIndicator.FULL_SUCCESS.getCode().toString(),
                                    "NI_XDR_" + classCode.getValue() + "_RES_SUCC",
                                    trcAssertion.getID());
                        } catch (Exception e) {
                            log.error(ExceptionUtils.getStackTrace(e));
                        }
                    } else {
                        try {
                            EvidenceUtils.createEvidenceREMNRR(classCode.toString(),
                                    "NI_XDR" + classCode.getValue(),
                                    new DateTime(),
                                    EventOutcomeIndicator.TEMPORAL_FAILURE.getCode().toString(),
                                    "NI_XDR_" + classCode.getValue() + "_RES_FAIL",
                                    trcAssertion.getID());
                        } catch (Exception e) {
                            log.error(ExceptionUtils.getStackTrace(e));
                        }

                    }
                    res.setContentType("text/html");
                    String message = "Dispensation successful";
                    res.setHeader("Cache-Control", "no-cache");
                    res.setDateHeader("Expires", 0);
                    res.setHeader("Pragma", "No-cache");

                    OutputStream OutStream = res.getOutputStream();
                    OutStream.write(message.getBytes());
                    OutStream.flush();
                    OutStream.close();
                } else {
                    log.error("UPLOAD DISP DOC RESPONSE ERROR", null);
                    res.setContentType("text/html");
                    String message = resp.toString();
                    res.setHeader("Cache-Control", "no-cache");
                    res.setDateHeader("Expires", 0);
                    res.setHeader("Pragma", "No-cache");

                    OutputStream OutStream = res.getOutputStream();
                    OutStream.write(message.getBytes());
                    OutStream.flush();
                    OutStream.close();
                    req.setAttribute("exception", "UPLOAD DISP DOC RESPONSE ERROR");
                }
            }
        } catch (Exception ex) {
            log.error("UPLOAD DISP DOC RESPONSE ERROR: " + ex.getMessage());
            res.setContentType("text/html");
            String message = "";
            if (Validator.isNotNull(resp)) {
                message = resp.toString();
            } else {
                message = ex.getLocalizedMessage();
            }
            res.setHeader("Cache-Control", "no-cache");
            res.setDateHeader("Expires", 0);
            res.setHeader("Pragma", "No-cache");

            OutputStream OutStream = res.getOutputStream();
            OutStream.write(message.getBytes());
            OutStream.flush();
            OutStream.close();
            log.error(ExceptionUtils.getStackTrace(ex));
        }
    }
}
