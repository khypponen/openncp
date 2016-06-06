package epsos.ccd.posam.tm.util;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;

public class NoNsXpath extends BaseXPath{

	
	
    public NoNsXpath(String xpathExpr) throws JaxenException
    {
        super( xpathExpr, new NoNsNavigator() );
    }

}
