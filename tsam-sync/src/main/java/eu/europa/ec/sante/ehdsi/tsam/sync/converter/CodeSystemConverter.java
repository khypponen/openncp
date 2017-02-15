package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.CodeSystemModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.domain.CodeSystem;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class CodeSystemConverter implements Converter<CodeSystemModel, CodeSystem> {

    private Map<String, CodeSystem> cache = new HashMap<>();

    @Override
    public CodeSystem convert(CodeSystemModel source) {
        if (source == null) {
            return null;
        }

        if (cache.containsKey(source.getId())) {
            return cache.get(source.getId());
        }

        CodeSystem target = new CodeSystem();
        target.setOid(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());

        cache.put(source.getId(), target);

        return target;
    }
}
