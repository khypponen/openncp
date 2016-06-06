/*
 * This file is part of epSOS OpenNCP implementation
 * Copyright (C) 2012 SPMS (Serviços Partilhados do Ministério da Saúde - Portugal)
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
 * Contact email: epsos@iuz.pt
 */
package epsos.ccd.posam.tm.testcases;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the utility method for retrieving the LTR available languages
 * 
 * @author Marcelo Fonseca<code> - marcelo.fonseca@iuz.pt</code>
 */
public class LtrAvailableLanguagesTest extends TBase {
    
    public static final Logger LOG = LoggerFactory.getLogger(LtrAvailableLanguagesTest.class);

    public void testLtrAvailableLanguages() {
        List<String> ltrLanguages = tmService.getLtrLanguages();
        
        for(String s: ltrLanguages)
        {
            LOG.info("Available language: "+s);
        }

        assertEquals(4, ltrLanguages.size());

        assertTrue(ltrLanguages.contains("en"));
    }
}
