package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.DesignationModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.domain.Designation;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class DesignationConverter implements Converter<DesignationModel, Designation> {

    @Override
    public Designation convert(DesignationModel source) {
        if (source == null) {
            return null;
        }

        Designation target = new Designation();
        target.setDesignation(source.getName());
        target.setLanguageCode(source.getLanguage());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setStatusDate(source.getStatusDate() == null ? null : LocalDateTime.parse(source.getStatusDate()));
        return target;
    }
}
