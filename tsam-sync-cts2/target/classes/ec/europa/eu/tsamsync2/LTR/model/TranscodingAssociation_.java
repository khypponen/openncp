package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemConcept;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(TranscodingAssociation.class)
public class TranscodingAssociation_ { 

    public static volatile SingularAttribute<TranscodingAssociation, Long> id;
    public static volatile SingularAttribute<TranscodingAssociation, CodeSystemConcept> targetConceptId;
    public static volatile SingularAttribute<TranscodingAssociation, String> status;
    public static volatile SingularAttribute<TranscodingAssociation, String> quality;
    public static volatile SingularAttribute<TranscodingAssociation, CodeSystemConcept> sourceConceptId;
    public static volatile SingularAttribute<TranscodingAssociation, Date> statusDate;
    public static volatile SingularAttribute<TranscodingAssociation, Long> transcodingAssociationId;

}