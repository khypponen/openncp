package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.rest.model.valueset.ValueSet;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.ValueSetEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class ValueSetConverter implements Converter<ValueSet, ValueSetEntity> {

    @Override
    public ValueSetEntity convert(ValueSet source) {
        if (source == null) {
            return null;
        }

        ValueSetEntity target = new ValueSetEntity();
        target.setOid(StringUtils.substring(source.getId(), 0, 255));
        target.setName(StringUtils.substring(source.getName(), 0, 255));
        target.setDescription(StringUtils.substring(source.getDescription(), 0, 255));
        return target;
    }
}
