package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(Designation.class)
public class Designation_ { 

    public static volatile SingularAttribute<Designation, Long> id;
    public static volatile SingularAttribute<Designation, Boolean> isPreferred;
    public static volatile SingularAttribute<Designation, String> languageCode;
    public static volatile SingularAttribute<Designation, String> status;
    public static volatile SingularAttribute<Designation, String> designation;
    public static volatile SingularAttribute<Designation, String> type;
    public static volatile SingularAttribute<Designation, Date> statusDate;
    public static volatile SingularAttribute<Designation, CodeSystemConcept> codeSystemConceptId;

}