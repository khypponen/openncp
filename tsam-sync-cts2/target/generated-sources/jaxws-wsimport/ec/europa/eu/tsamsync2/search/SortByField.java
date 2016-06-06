
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sortByField.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="sortByField">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CODE"/>
 *     &lt;enumeration value="TERM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "sortByField")
@XmlEnum
public enum SortByField {

    CODE,
    TERM;

    public String value() {
        return name();
    }

    public static SortByField fromValue(String v) {
        return valueOf(v);
    }

}
