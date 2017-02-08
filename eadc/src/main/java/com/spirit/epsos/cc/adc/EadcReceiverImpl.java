package com.spirit.epsos.cc.adc;

import org.slf4j.Logger;
import com.spirit.epsos.cc.adc.extractor.AutomaticDataCollector;

import eu.epsos.pt.eadc.util.EadcFactory;
import org.slf4j.LoggerFactory;

/**
 * the EadcReceiverImpl is instantiated from the EADCFactory as a singleton
 * instance
 *
 */
public class EadcReceiverImpl implements EadcReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EadcReceiverImpl.class);
    AutomaticDataCollector automaticDataCollectorInstance = EadcFactory.INSTANCE.createAutomaticDataCollector();

    /**
     * this method is called from the NCP - called from multiple threads in
     * parallel *
     */
    @Override
    public void process(EadcEntry entry) throws Exception {
        LOGGER.debug("process entry start");
        if (entry == null) {
            throw new Exception("EADCEntry == null");
        }
        if (entry.getData() == null) {
            throw new Exception("EADCEntry.data xml == null");
        }
        if (entry.getDsName() == null | entry.getDsName().isEmpty()) {
            throw new Exception("Null or Empty dsName");
        }
        automaticDataCollectorInstance.processTransaction(entry.getDsName(), entry.getData());

        LOGGER.debug("process entry end");
    }
}
