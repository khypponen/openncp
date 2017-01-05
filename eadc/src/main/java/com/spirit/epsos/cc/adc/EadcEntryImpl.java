package com.spirit.epsos.cc.adc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * the EadcEntryImpl is instantiated from the EADCFactory as a singleton
 * instance
 */
public class EadcEntryImpl implements EadcEntry {

    private static final Logger LOGGER = LoggerFactory.getLogger(EadcEntryImpl.class);
    /**
     * the data xml is including transaction information and the CDA L3 document
     */
    private Document data = null;
    private String dsName = null;

    public EadcEntryImpl() {
    }

    /**
     * if needed, other EADCEntryImpls could extract additional information from
     * the soapRqData or soapRspData
     */
    private EadcEntryImpl(String dsName, Document data) {
        LOGGER.trace("instantiate new EADCEntryImpl..");
        this.data = data;
        this.dsName = dsName;
    }

    public Document getData() {
        return data;
    }

    @Override
    public EadcEntry newInstance(String dsName, Document data, Document soapRqData, Document soapRspData) {
        return new EadcEntryImpl(dsName, data);
    }

    @Override
    public String getDsName() {
        return dsName;
    }
}
