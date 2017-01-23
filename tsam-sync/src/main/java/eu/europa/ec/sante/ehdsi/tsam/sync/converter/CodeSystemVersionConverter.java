package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.rest.model.codesystem.CodeSystemVersion;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.CodeSystemVersionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CodeSystemVersionConverter implements Converter<CodeSystemVersion, CodeSystemVersionEntity> {

    private Map<String, CodeSystemVersionEntity> cache = new HashMap<>();

    private CodeSystemConverter codeSystemConverter = new CodeSystemConverter();

    @Override
    public CodeSystemVersionEntity convert(CodeSystemVersion source) {
        if (source == null) {
            return null;
        }

        if (cache.containsKey(source.getId())) {
            return cache.get(source.getId());
        }

        CodeSystemVersionEntity target = new CodeSystemVersionEntity();
        target.setUid(source.getId());
        target.setFullName(StringUtils.substring(source.getFullName(), 0, 255));
        target.setLocalName(StringUtils.substring(source.getLocalName(), 0, 255));
        target.setEffectiveDate(source.getEffectiveDate() == null ? null : LocalDateTime.parse(source.getEffectiveDate()));
        target.setReleaseDate(source.getReleaseDate() == null ? null : LocalDateTime.parse(source.getReleaseDate()));
        target.setStatus(StringUtils.substring(source.getStatus(), 0, 255));
        target.setStatusDate(source.getStatusDate() == null ? null : LocalDateTime.parse(source.getStatusDate()));
        target.setDescription(StringUtils.substring(source.getDescription(), 0, 255));
        target.setCopyright(StringUtils.substring(source.getCopyright(), 0, 255));
        target.setSource(StringUtils.substring(source.getSource(), 0, 255));
        target.setCodeSystem(codeSystemConverter.convert(source.getCodeSystem()));

        cache.put(source.getId(), target);

        return target;
    }
}
