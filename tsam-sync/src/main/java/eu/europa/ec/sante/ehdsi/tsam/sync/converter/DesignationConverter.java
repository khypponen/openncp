package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.DesignationModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.DesignationEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class DesignationConverter implements Converter<DesignationModel, DesignationEntity> {

    @Override
    public DesignationEntity convert(DesignationModel source) {
        if (source == null) {
            return null;
        }

        DesignationEntity target = new DesignationEntity();
        target.setDesignation(StringUtils.substring(source.getName(), 0, 255));
        target.setLanguageCode(StringUtils.substring(source.getLanguage(), 0, 255));
        target.setType(StringUtils.substring(source.getType(), 0, 255));
        target.setStatus(StringUtils.substring(source.getStatus(), 0, 255));
        target.setStatusDate(source.getStatusDate() == null ? null : LocalDateTime.parse(source.getStatusDate()));
        return target;
    }
}
