/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**/
package se.sb.epsos.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.sb.epsos.web.auth.UserinfoPanel;
import se.sb.epsos.web.model.BreadCrumbVO;
import se.sb.epsos.web.model.ErrorFeedback;
import se.sb.epsos.web.pages.HelpPage;
import se.sb.epsos.web.pages.QueryDocumentsPage;
import se.sb.epsos.web.service.NcpServiceException;
import se.sb.epsos.web.service.NcpServiceFacade;
import se.sb.epsos.web.util.SpecialBreadCrumbPanel;

public abstract class BasePage extends WebPage implements Serializable, IAjaxIndicatorAware {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    public static final ResourceReference AJAX_INDICATOR = new ResourceReference("/images/spinner1.gif");
    private AjaxLink<?> showWarningsLink;
    private FeedbackPanel feedback;
    private ErrorFeedbackPanel errorFeedbackPanel;
    private List<ErrorFeedback> list = new ArrayList<ErrorFeedback>();
    private SpecialBreadCrumbPanel bcPanel;
    private AjaxIndicatorAppender ajaxIndicator;

    protected BasePage() {
        super();
        setupPage();
    }

    protected BasePage(PageParameters parameters) {
        super(parameters);
        setupPage();
    }

    protected void clearErrorList(){
        list.clear();
    }
    
    protected void clearErrorList(AjaxRequestTarget target){
        list.clear();
        errorFeedbackPanel.setVisible(false);
        target.addComponent(errorFeedbackPanel);
    }

    @Override
    public EpsosAuthenticatedWebSession getSession() {
        return (EpsosAuthenticatedWebSession) super.getSession();
    }

    public NcpServiceFacade getServiceFacade() {
        return getSession().getServiceFacade();
    }

    /**
     * Handle a known NcpServiceException. Displays the error code, when it
     * happened and the cause.
     *
     * @param ncpExc The exception
     * @param actionDescription Text describing what went wrong. Eg. "Failed to
     * retreive receipt".
     */
    public void handleKnownNcpException(NcpServiceException ncpExc, String actionDescription) {
        list.clear();
        ErrorFeedback error = new ErrorFeedback();
        error.setErrorCode(ncpExc.getEpsosErrorCode());
        error.setSeverityType("Failure");
        String epsosErrorMessage;
        try {
            epsosErrorMessage = getString("error.epsos." + ncpExc.getEpsosErrorCode());
        } catch (Exception ex) {
            epsosErrorMessage = getString("error.epsos.unknown");
        }
        if (actionDescription != null) {
            error.setErrorMessage(actionDescription + ": " + epsosErrorMessage);
        } else {
            error.setErrorMessage(epsosErrorMessage);
        }
        error.setException(ncpExc);
        displayError(error);
    }

    /**
     * Handle a known NcpServiceException. Displays the error code, when it
     * happened and the cause.
     *
     * @param ncpExc The exception
     * @param actionDescription Text describing what went wrong. Eg. "Failed to
     * retreive receipt".
     */
    public void handleKnownNcpExceptionAfterAjax(NcpServiceException ncpExc, String actionDescription, AjaxRequestTarget target) {
        list.clear();
        ErrorFeedback error = new ErrorFeedback();
        error.setErrorCode(ncpExc.getEpsosErrorCode());
        error.setSeverityType(getLocalizer().getString("error.failure", this));
        String epsosErrorMessage;
        try {
            epsosErrorMessage = getString("error.epsos." + ncpExc.getEpsosErrorCode());
        } catch (Exception ex) {
            epsosErrorMessage = getString("error.epsos.unknown");
        }
        if (actionDescription != null) {
            error.setErrorMessage(actionDescription + ": " + epsosErrorMessage);
        } else {
            error.setErrorMessage(epsosErrorMessage);
        }
        error.setException(ncpExc);
        displayErrorAfterAjax(error, target);
    }

    /**
     *
     * @param error Feedback, about the error, that should be displayed to the
     * user.
     */
    public void displayError(ErrorFeedback error) {
        list.add(error);
        errorFeedbackPanel.setVisible(true);
    }

    /**
     *
     * @param error Feedback, about the error, that should be displayed to the
     * user.
     * @param target Ajax request target to update the displayed text on.
     */
    public void displayErrorAfterAjax(ErrorFeedback error, AjaxRequestTarget target) {
        list.add(error);
        errorFeedbackPanel.setVisible(true);
        target.addComponent(errorFeedbackPanel);
    }

    public void displayListOfWarnings(List<ErrorFeedback> error) {
        list.addAll(error);
        showWarningsLink.setVisible(true);
    }

    public FeedbackPanel getFeedback() {
        return this.feedback;
    }
    
    public ErrorFeedbackPanel getErrorFeedback() {
    	return this.errorFeedbackPanel;
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
    	return ajaxIndicator.getMarkupId();
    }

    public SpecialBreadCrumbPanel getBreadcrumbPanel() {
        return bcPanel;
    }

    public void stepBackToPage(Class<? extends WebPage> c) {
        List<BreadCrumbVO<?>> breadCrumbs =  getSession().getBreadCrumbList();
        for (BreadCrumbVO<?> bc : breadCrumbs) {
            if (bc.getWebPage() != null && bc.getWebPage().getClass() == c) {
                 getSession().removeAllAfterBc(bc);
                if (c == QueryDocumentsPage.class) {
                    setResponsePage(new QueryDocumentsPage(bc.getWebPage().getPageParameters()));
                } else {
                    setResponsePage(bc.getWebPage());
                }
                break;
            }
        }
    }

    private void setupPage() {
        if (getSession().isSignedIn()) {
            bcPanel = new SpecialBreadCrumbPanel("breadcrumb");
            bcPanel.setMarkupId("breadcrumb");
            bcPanel.setOutputMarkupId(true);
            add(new UserinfoPanel("userinfo", getSession().getUserDetails()));
            add(bcPanel);
        } else {
            add(new Label("userinfo", getString("message.notloggedin")));
            add(new Label("breadcrumb", ""));
        }

        showWarningsLink = new AjaxLink<Object>("showWarningsIcon") {
			private static final long serialVersionUID = -1836446128156428692L;

			@Override
            public void onClick(AjaxRequestTarget target) {
                errorFeedbackPanel.setVisible(!errorFeedbackPanel.isVisible());
                target.addComponent(errorFeedbackPanel);
            }
        };
        add(showWarningsLink);
        showWarningsLink.setVisible(false);

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        errorFeedbackPanel = new ErrorFeedbackPanel("errorFeedback", new ListModel<ErrorFeedback>(list));
        errorFeedbackPanel.setOutputMarkupId(true);
        errorFeedbackPanel.setVisible(false);
        errorFeedbackPanel.setOutputMarkupPlaceholderTag(true);
        add(errorFeedbackPanel);

        add(new ExternalLink("epsosLink", "http://www.epsos.eu"));
        add(new ExternalLink("evaluationLink", "http://vserver.gen.auth.gr/epsos/"));
        add(new ExternalLink("evaluationLinkPatient", "http://www.epsos.eu/sverige/start/wwwepsoseuevaluation-for-health-professionals-sv/patient-questionnaires-sverige-2.html"));
        add(new BookmarkablePageLink<Object>("helpLink", HelpPage.class));

        Properties properties = new Properties();
        URL url = getClass().getClassLoader().getResource("application.properties");
        try {
            properties.load(new FileInputStream(url.getFile()));
        } catch (IOException e) {
            LOGGER.error("Could not load properties", e);
            error("Could not load properties");
        }
        add(new Label("application.artifactId", properties.get("application.artifactId").toString()));
        String version = "Version " + properties.get("application.version").toString() + " #" + properties.get("application.buildnumber").toString()
                + " " + properties.get("application.buildid").toString();
        add(new Label("application.version", version));
        
        ajaxIndicator = new AjaxIndicatorAppender() {
			private static final long serialVersionUID = 8067547979954117769L;

			@Override
            protected String getSpanClass() {
                return "ajax-indicator";
            }

            @Override
            protected CharSequence getIndicatorUrl() {
                return RequestCycle.get().urlFor(AJAX_INDICATOR);
            }

            @Override
            public String getMarkupId() {
                return "spinner";
            }
        };
        add(ajaxIndicator);
    }

	@Override
	protected void configureResponse() {
		super.configureResponse();
		WebResponse response = getWebRequestCycle().getWebResponse();
		response.setHeader("Cache-Control", "no-cache, max-age=1, must-revalidate, no-store, private");
		response.setHeader("Pragma","no-cache, private");
		
//		only add the "accept-ranges" header if the "user-agent" header indicates Internet Explorer
		WebRequest request = getWebRequestCycle().getWebRequest();
		String userAgent = request.getHttpServletRequest().getHeader("User-Agent");
		if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
			response.setHeader("Accept-Ranges", "bytes");
		}
	}  
}
