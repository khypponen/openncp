package eu.esens.abb.nonrep;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class ObligationHandlerFactory {

	private static ObligationHandlerFactory instance = null;
	private static Logger l = Logger.getLogger(ObligationHandlerFactory.class);
	
	public static ObligationHandlerFactory getInstance() {
		if (instance == null) {
			synchronized (ObligationHandlerFactory.class) {
				if (instance == null) {
					try {
						instance = new ObligationHandlerFactory();
					} catch (Exception e) {
						e.printStackTrace();

						throw new IllegalStateException(
								"Unable to instantiate the ObligationHandlerFactory: "
										+ e.getMessage(), e);
					}
				}
			}
		}
		return instance;
	}

	private ObligationHandlerFactory() {
		l.debug("In the ObligationHandlerFactory constructor");
	}

	/**
	 * Dispatch the correct obligation handler based, on the family of message types
	 * @param messageType
	 * @param obligations
	 * @return
	 * @throws ObligationDischargeException 
	 */
	public List<ObligationHandler> createHandler(MessageType messageType,
			List<ESensObligation> obligations, Context context) throws ObligationDischargeException {
		
		if ( messageType == null ) {
			throw new ObligationDischargeException("Message Type is null");
		}
		
		int size = obligations.size();
		
		LinkedList<ObligationHandler> list = new LinkedList<ObligationHandler>();
		for ( int i=0; i<size; i++ ) {
			ESensObligation obligation = obligations.get(i);
			String obligationId = obligation.getObligationID();
			
			// Here it is static, but it will be a factory upon a configuration file
			if ( obligationId.equals("urn:eSENS:obligations:nrr:ATNA") )
			{
				list.add(new ATNAObligationHandler(messageType, obligations, context));
			} else if ( obligationId.equals("urn:eSENS:obligations:nrr:ETSIREM")) {
				list.add(new ETSIREMObligationHandler(messageType, obligations, context));
			} else if ( obligationId.equals("urn:eSENS:obligations:nro:ETSIREM")) {
				list.add(new ETSIREMObligationHandler(messageType, obligations, context));
			} else if ( obligationId.equals("urn:eSENS:obligations:nrd:ETSIREM")) {
				list.add(new ETSIREMObligationHandler(messageType, obligations, context));
			} else {
			
				list.add(new NullObligationHandler());
			}
		}
		
		
		return list;
		
	}
	

}
