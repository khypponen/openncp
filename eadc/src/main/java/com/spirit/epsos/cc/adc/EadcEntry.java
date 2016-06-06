package com.spirit.epsos.cc.adc;

import org.w3c.dom.Document;

public interface EadcEntry {

    /**
     * This enum represents the three different transaction types data source names.
     */
    public static enum DsTypes {

        XCPD("jdbc/EADC_XCPD"), XCA("jdbc/EADC_XCA"), XDR("jdbc/EADC_XDR");
        private String value;

        private DsTypes(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * the data xml is including transaction information and the CDA L3 document
     *
     */
    public Document getData();

    /**
     * The DataSetName
     *
     * @return
     */
    public String getDsName();

    public EadcEntry newInstance(String dsName, Document data, Document soapRqData, Document soapRspData);
}
