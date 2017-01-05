package com.spirit.epsos.cc.adc.test;

import com.spirit.epsos.cc.adc.db.EadcDbConnect;
import eu.epsos.pt.eadc.util.EadcFactory;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class will test the EADCReceiver class
 */
public class EADCReceiverTest extends BaseEadcTest {

    @Before
    public void setUp() throws Exception {

        //TODO: Check Logs Jerome
        //DOMConfigurator.configureAndWatch("log4j.xml", 60 * 1000);
        
        /* TEST DATABASE INSTANTIATION */
        EadcDbConnect con = eu.epsos.pt.eadc.util.EadcFactory.INSTANCE.createEadcDbConnect(DS_NAME);
    }

    @Test
    public void dbConnectTest() throws Exception {

        //TODO: Check Logs Jerome
        //DOMConfigurator.configureAndWatch("log4j.xml", 60 * 1000);

        DocumentBuilderFactory document_factory = DocumentBuilderFactory.newInstance();
        Document notNullDoc = document_factory.newDocumentBuilder().getDOMImplementation().createDocument("test", "test", null);
        for (int i = 0; i < 3; i++) {
            EadcFactory.INSTANCE.getReceiver().process(EadcFactory.INSTANCE.getEntry(DS_NAME, notNullDoc, null, null));
        }
    }
}
