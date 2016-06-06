package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion;
import ec.europa.eu.tsamsync2.LTR.model.Designation;
import ec.europa.eu.tsamsync2.LTR.model.TranscodingAssociation;
import ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(CodeSystemConcept.class)
public class CodeSystemConcept_ { 

    public static volatile SingularAttribute<CodeSystemConcept, Long> id;
    public static volatile CollectionAttribute<CodeSystemConcept, TranscodingAssociation> transcodingAssociationCollection1;
    public static volatile SingularAttribute<CodeSystemConcept, String> definition;
    public static volatile SingularAttribute<CodeSystemConcept, String> status;
    public static volatile CollectionAttribute<CodeSystemConcept, Designation> designationCollection;
    public static volatile CollectionAttribute<CodeSystemConcept, TranscodingAssociation> transcodingAssociationCollection;
    public static volatile SingularAttribute<CodeSystemConcept, String> code;
    public static volatile SingularAttribute<CodeSystemConcept, Date> statusDate;
    public static volatile SingularAttribute<CodeSystemConcept, CodeSystemVersion> codeSystemVersionId;
    public static volatile CollectionAttribute<CodeSystemConcept, ValueSetVersion> valueSetVersionCollection;

}