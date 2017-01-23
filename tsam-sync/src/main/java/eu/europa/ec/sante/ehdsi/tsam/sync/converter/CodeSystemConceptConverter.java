package eu.europa.ec.sante.ehdsi.tsam.sync.converter;

import eu.europa.ec.sante.ehdsi.termservice.rest.model.concept.CodeSystemConcept;
import eu.europa.ec.sante.ehdsi.termservice.rest.model.concept.Designation;
import eu.europa.ec.sante.ehdsi.tsam.sync.db.CodeSystemConceptEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class CodeSystemConceptConverter implements Converter<CodeSystemConcept, CodeSystemConceptEntity> {

    private final CodeSystemVersionConverter codeSystemVersionConverter = new CodeSystemVersionConverter();

    private final DesignationConverter designationConverter = new DesignationConverter();

    @Override
    public CodeSystemConceptEntity convert(CodeSystemConcept source) {
        if (source == null) {
            return null;
        }

        CodeSystemConceptEntity target = new CodeSystemConceptEntity();
        target.setCode(StringUtils.substring(source.getCode(), 0, 255));
        target.setDefinition(StringUtils.substring(source.getDescription(), 0, 1000));
        target.setStatus(StringUtils.substring(source.getStatus(), 0, 255));
        target.setStatusDate(source.getStatusDate() == null ? null : LocalDateTime.parse(source.getStatusDate()));
        target.setCodeSystemVersion(codeSystemVersionConverter.convert(source.getCodeSystemVersion()));

        for (Designation designation : source.getDesignations()) {
            target.addDesignation(designationConverter.convert(designation));
        }

        return target;
    }
}
