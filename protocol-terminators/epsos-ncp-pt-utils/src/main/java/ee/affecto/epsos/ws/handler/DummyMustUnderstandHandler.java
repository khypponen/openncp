/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 Affecto EE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package ee.affecto.epsos.ws.handler;

import java.util.Iterator;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;

public class DummyMustUnderstandHandler extends AbstractHandler {

    public InvocationResponse invoke(MessageContext ctx) throws AxisFault {

        SOAPHeader header = ctx.getEnvelope().getHeader();
        if (header != null) {
            Iterator<?> blocks = header.examineAllHeaderBlocks();
            while (blocks.hasNext()) {
                SOAPHeaderBlock block = (SOAPHeaderBlock) blocks.next();
//				if( ... some check to see if this is one of your headers ... )            
                block.setProcessed();
            }
        }
        return InvocationResponse.CONTINUE;
    }
}