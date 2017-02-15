package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.common.web.rest.model.CodeSystemVersionModel;
import eu.europa.ec.sante.ehdsi.tsam.sync.domain.CodeSystemVersion;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CodeSystemVersionConverter implements Converter<CodeSystemVersionModel, CodeSystemVersion> {

    private Map<String, CodeSystemVersion> cache = new HashMap<>();

    private CodeSystemConverter codeSystemConverter = new CodeSystemConverter();

    @Override
    public CodeSystemVersion convert(CodeSystemVersionModel source) {
        if (source == null) {
            return null;
        }

        if (cache.containsKey(source.getId())) {
            return cache.get(source.getId());
        }

        CodeSystemVersion target = new CodeSystemVersion();
        target.setFullName(source.getFullName());
        target.setLocalName(source.getLocalName());
        target.setEffectiveDate(source.getEffectiveDate() == null ? null : LocalDateTime.parse(source.getEffectiveDate()));
        target.setReleaseDate(source.getReleaseDate() == null ? null : LocalDateTime.parse(source.getReleaseDate()));
        target.setStatus(source.getStatus());
        target.setStatusDate(source.getStatusDate() == null ? null : LocalDateTime.parse(source.getStatusDate()));
        target.setDescription(source.getDescription());
        target.setCopyright(source.getCopyright());
        target.setSource(source.getSource());
        target.setCodeSystem(codeSystemConverter.convert(source.getCodeSystem()));

        cache.put(source.getId(), target);

        return target;
    }
}
