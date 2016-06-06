/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * 
 * This file is part of SRDC epSOS NCP.
 * 
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class NamespaceContextImpl implements NamespaceContext{

	private Map namespaceBindings = null;

    public NamespaceContextImpl(Map namespaceBindings) {
        if(namespaceBindings != null){
            this.namespaceBindings = namespaceBindings;
        }
        else
            namespaceBindings = new HashMap();
    }
	
    @Override
	public String getNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return (String) namespaceBindings.get(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		if(namespaceBindings.containsValue(namespaceURI)){
            Iterator itrKeys = namespaceBindings.keySet().iterator();
            Iterator itr = namespaceBindings.values().iterator();
            while(itr.hasNext()){
                if(itr.next().equals(namespaceURI)){
                    return (String)itrKeys.next();
                }
            }
        }
        return null;
	}

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		if(namespaceBindings.containsValue(namespaceURI)){
            Iterator itrKeys = namespaceBindings.keySet().iterator();
            Iterator itr = namespaceBindings.values().iterator();
            while(itr.hasNext()){
                if(itr.next().equals(namespaceURI)){
                    result.add((String)itrKeys.next());
                }
            }
        }                
        return result.iterator();
	}	
}
