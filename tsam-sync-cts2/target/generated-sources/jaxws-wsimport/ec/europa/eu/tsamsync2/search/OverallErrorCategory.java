
package ec.europa.eu.tsamsync2.search;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for overallErrorCategory.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="overallErrorCategory">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INFO"/>
 *     &lt;enumeration value="WARN"/>
 *     &lt;enumeration value="ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "overallErrorCategory")
@XmlEnum
public enum OverallErrorCategory {

    INFO,
    WARN,
    ERROR;

    public String value() {
        return name();
    }

    public static OverallErrorCategory fromValue(String v) {
        return valueOf(v);
    }

}
