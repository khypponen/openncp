package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.ValueSetVersionModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.domain.ValueSetVersion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class ValueSetVersionConverter implements Converter<ValueSetVersionModel, ValueSetVersion> {

    private final ValueSetConverter valueSetConverter = new ValueSetConverter();

    @Override
    public ValueSetVersion convert(ValueSetVersionModel source) {
        if (source == null) {
            return null;
        }

        ValueSetVersion target = new ValueSetVersion();
        target.setName(source.getName());
        target.setEffectiveDate(source.getEffectiveDate() == null ? null : LocalDateTime.parse(source.getEffectiveDate()));
        target.setReleaseDate(source.getReleaseDate() == null ? null : LocalDateTime.parse(source.getReleaseDate()));
        target.setStatus(StringUtils.substring(source.getStatus(), 0, 255));
        target.setStatusDate(source.getStatusDate() == null ? null : LocalDateTime.parse(source.getStatusDate()));
        target.setDescription(StringUtils.substring(source.getDescription(), 0, 255));
        target.setValueSet(valueSetConverter.convert(source.getValueSet()));
        return target;
    }
}
