package eu.esens.abb.nonrep;

import org.w3c.dom.Document;

public interface ObligationHandler {
	public void discharge() throws ObligationDischargeException;
	public Document getMessage();
}
