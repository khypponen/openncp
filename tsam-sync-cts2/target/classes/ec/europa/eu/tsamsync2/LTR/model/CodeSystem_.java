package ec.europa.eu.tsamsync2.LTR.model;

import ec.europa.eu.tsamsync2.LTR.model.CodeSystemVersion;
import ec.europa.eu.tsamsync2.LTR.model.ValueSet;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-06-04T16:06:43")
@StaticMetamodel(CodeSystem.class)
public class CodeSystem_ { 

    public static volatile SingularAttribute<CodeSystem, Long> id;
    public static volatile CollectionAttribute<CodeSystem, ValueSet> valueSetCollection;
    public static volatile SingularAttribute<CodeSystem, String> oid;
    public static volatile SingularAttribute<CodeSystem, String> description;
    public static volatile SingularAttribute<CodeSystem, String> name;
    public static volatile CollectionAttribute<CodeSystem, CodeSystemVersion> codeSystemVersionCollection;

}