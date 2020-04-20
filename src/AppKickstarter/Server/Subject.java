package AppKickstarter.Server;

import java.util.ArrayList;

//======================================================================
// Subject
/**
 * 
 * @author user
 *
 */
public abstract class Subject {
	private ArrayList<Observer> observers = new ArrayList<>();

	// ------------------------------------------------------------
	// addObs
	/**
	 * 
	 * @param observer
	 */
	public final void addObs(Observer observer) {
		System.out.println("Adding observer: " + observer);
		observers.add(observer);
	} // addObs

	// ------------------------------------------------------------
	// removeObs
	/**
	 * 
	 * @param observer
	 */
	public final void removeObs(Observer observer) {
		System.out.println("Removing observer: " + observer);
		observers.remove(observer);
	} // removeObs

	// ------------------------------------------------------------
	// notifyObs
	/**
	 * 
	 */
	public final void notifyObs() {
		for (Observer o : observers) {
			o.update();
		}
	} // notifyObs

	/**
	 * 
	 * @param status
	 */
	public abstract void setStatus(String status);
	
	/**
	 * 
	 * @return
	 */
	public abstract String getStatus();
} // Subject
