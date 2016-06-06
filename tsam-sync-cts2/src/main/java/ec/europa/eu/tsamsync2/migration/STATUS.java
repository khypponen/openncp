/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.europa.eu.tsamsync2.migration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bernamp
 */
public enum STATUS {

    CURRENT("Current"), NONCURRENT("Non-Current"), LIMITED("Limited"), RETIRED("Retired"), DUPLICATED("Duplicated"),
    OUTDATED("Outdated"), ERRONEOUS("Erroneous"), INAPPROPRIATE("Inappropriate"), CONCEPT_NON_CURRENT("Concept non-current"),
    MOVED_ELSEWHERE("Moved elsewhere"), PENDING_MOVE("Pending move"), NOT_IN_USE("not in use"), VALID("valid"), INVALID("invalid");

    List<STATUS> code_system_concept_status_list = new ArrayList<>();
    List<STATUS> code_system_version = new ArrayList<>();
    List<STATUS> code_system_concept = new ArrayList<>();
    List<STATUS> code_system_entity_version_association = new ArrayList<>();
    List<STATUS> value_set_version = new ArrayList<>();

    private final String name;

    STATUS(String name) {
        this.name = name;
        fillLists();
    }

    public String getName() {
        return name;
    }

    private void fillLists() {
        code_system_concept_status_list.add(STATUS.CURRENT);
        code_system_concept_status_list.add(STATUS.NONCURRENT);
        code_system_concept_status_list.add(STATUS.DUPLICATED);
        code_system_concept_status_list.add(STATUS.OUTDATED);
        code_system_concept_status_list.add(STATUS.ERRONEOUS);
        code_system_concept_status_list.add(STATUS.LIMITED);
        code_system_concept_status_list.add(STATUS.INAPPROPRIATE);
        code_system_concept_status_list.add(STATUS.CONCEPT_NON_CURRENT);
        code_system_concept_status_list.add(STATUS.MOVED_ELSEWHERE);
        code_system_concept_status_list.add(STATUS.PENDING_MOVE);
        code_system_version.add(STATUS.CURRENT);
        code_system_version.add(STATUS.RETIRED);
        code_system_version.add(STATUS.NOT_IN_USE);
        code_system_concept.add(STATUS.CURRENT);
        code_system_concept.add(STATUS.RETIRED);
        code_system_entity_version_association.add(STATUS.VALID);
        code_system_entity_version_association.add(STATUS.INVALID);
        value_set_version.add(STATUS.CURRENT);
        value_set_version.add(STATUS.RETIRED);
        value_set_version.add(STATUS.NOT_IN_USE);

    }
}
