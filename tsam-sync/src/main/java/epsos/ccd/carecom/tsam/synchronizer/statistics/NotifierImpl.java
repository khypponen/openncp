package epsos.ccd.carecom.tsam.synchronizer.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An implementation of a notifier.
 */
public class NotifierImpl implements Notifier {
	private List<Gatherer> gatherers;
	
	/**
	 * Default constructor.
	 */
	public NotifierImpl() {
		this.gatherers = new ArrayList<Gatherer>();
	}
	
	public void addGatherer(Gatherer gatherer) {
		this.gatherers.add(gatherer);
	}

	public void registerActionStart(String webMethodName, Map<String, Long> filters) {
		for (Gatherer gatherer : this.gatherers) {
			gatherer.registerActionStart(webMethodName, filters);
		}
	}

	public void registerActionEnd(String actionName, int numberOfRecords) {
		for (Gatherer gatherer : this.gatherers) {
			gatherer.registerActionEnd(actionName, numberOfRecords);
		}
	}

	public void registerGatheringComplete() {
		for (Gatherer gatherer : this.gatherers) {
			gatherer.registerGatheringComplete();
		}
	}
}
