package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystem;
import ec.europa.eu.tsamsync2.LTR.model.ValueSetVersion;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(ValueSet.class)
public class ValueSet_ { 

    public static volatile SingularAttribute<ValueSet, Long> id;
    public static volatile SingularAttribute<ValueSet, String> epsosName;
    public static volatile SingularAttribute<ValueSet, String> oid;
    public static volatile SingularAttribute<ValueSet, String> description;
    public static volatile SingularAttribute<ValueSet, CodeSystem> parentCodeSystemId;
    public static volatile CollectionAttribute<ValueSet, ValueSetVersion> valueSetVersionCollection1;
    public static volatile CollectionAttribute<ValueSet, ValueSetVersion> valueSetVersionCollection;

}