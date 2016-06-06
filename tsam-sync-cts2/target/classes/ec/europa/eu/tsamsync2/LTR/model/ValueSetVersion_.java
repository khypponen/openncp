package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept;
import ec.europa.eu.tsamsync2.LTR.model.ValueSet;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(ValueSetVersion.class)
public class ValueSetVersion_ { 

    public static volatile SingularAttribute<ValueSetVersion, Long> id;
    public static volatile SingularAttribute<ValueSetVersion, Date> releaseDate;
    public static volatile SingularAttribute<ValueSetVersion, ValueSet> previousVersionId;
    public static volatile SingularAttribute<ValueSetVersion, String> status;
    public static volatile SingularAttribute<ValueSetVersion, String> description;
    public static volatile SingularAttribute<ValueSetVersion, String> versionName;
    public static volatile SingularAttribute<ValueSetVersion, ValueSet> valueSetId;
    public static volatile SingularAttribute<ValueSetVersion, Date> effectiveDate;
    public static volatile SingularAttribute<ValueSetVersion, Date> statusDate;
    public static volatile CollectionAttribute<ValueSetVersion, CodeSystemConcept> codeSystemConceptCollection;

}