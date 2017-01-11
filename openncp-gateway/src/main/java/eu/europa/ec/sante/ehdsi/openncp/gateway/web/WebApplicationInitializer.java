package eu.europa.ec.sante.ehdsi.openncp.gateway.web;

import eu.europa.ec.sante.ehdsi.openncp.gateway.cfg.WebMvcConfig;
import eu.europa.ec.sante.ehdsi.openncp.gateway.cfg.WebSecurityConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{WebSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebMvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }
}
