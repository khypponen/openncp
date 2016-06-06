package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystem;
import ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(CodeSystemVersion.class)
public class CodeSystemVersion_ { 

    public static volatile SingularAttribute<CodeSystemVersion, String> localName;
    public static volatile SingularAttribute<CodeSystemVersion, String> status;
    public static volatile SingularAttribute<CodeSystemVersion, String> copyright;
    public static volatile SingularAttribute<CodeSystemVersion, Date> statusDate;
    public static volatile SingularAttribute<CodeSystemVersion, Long> id;
    public static volatile SingularAttribute<CodeSystemVersion, Date> releaseDate;
    public static volatile SingularAttribute<CodeSystemVersion, BigInteger> previousVersionId;
    public static volatile SingularAttribute<CodeSystemVersion, String> source;
    public static volatile SingularAttribute<CodeSystemVersion, String> description;
    public static volatile SingularAttribute<CodeSystemVersion, String> fullName;
    public static volatile SingularAttribute<CodeSystemVersion, Date> effectiveDate;
    public static volatile SingularAttribute<CodeSystemVersion, CodeSystem> codeSystemId;
    public static volatile CollectionAttribute<CodeSystemVersion, CodeSystemConcept> codeSystemConceptCollection;

}