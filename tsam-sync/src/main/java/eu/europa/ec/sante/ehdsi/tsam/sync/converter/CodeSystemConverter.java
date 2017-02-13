package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.CodeSystemModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.CodeSystemEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class CodeSystemConverter implements Converter<CodeSystemModel, CodeSystemEntity> {

    private Map<String, CodeSystemEntity> cache = new HashMap<>();

    @Override
    public CodeSystemEntity convert(CodeSystemModel source) {
        if (source == null) {
            return null;
        }

        if (cache.containsKey(source.getId())) {
            return cache.get(source.getId());
        }

        CodeSystemEntity target = new CodeSystemEntity();
        target.setOid(StringUtils.substring(source.getId(), 0, 255));
        target.setName(StringUtils.substring(source.getName(), 0, 255));
        target.setDescription(StringUtils.substring(source.getDescription(), 0, 255));

        cache.put(source.getId(), target);

        return target;
    }
}
