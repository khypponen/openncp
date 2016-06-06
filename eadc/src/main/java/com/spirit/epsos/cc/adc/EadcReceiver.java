package com.spirit.epsos.cc.adc;

public interface EadcReceiver {
	public void process(EadcEntry entry) throws Exception;
}
