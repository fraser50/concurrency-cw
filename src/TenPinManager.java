


/*
 * Ten Pin Manager class
 * 
 * You must write code here so that this class satisfies the Coursework User Requirements (see CW specification on Canvas).
 * 
 * You may add private classes and methods to this file 
 * 
 * 
 * 
 ********************* IMPORTANT ******************
 * 
 * 1. You must implement TenPinManager using Java's ReentrantLock class and condition interface (as imported below).
 * 2. Other thread safe classes, e.g. java.util.concurrent MUST not be used by your TenPinManager class.
 * 3. Other thread scheduling classes and methods (e.g. Thread.sleep(timeout), ScheduledThreadPoolExecutor etc.) must not be used by your TenPinManager class..
 * 4. Busy waiting must not be used: specifically, when an instance of your TenPinManager is waiting for an event (i.e. a call to booklane() or playerLogin() ) it must not consume CPU cycles.
 * 5. No other code except that provided by you here (in by your TenPinManager.java file) will be used in the automatic marking.
 * 6. Your code must be reasonably responsive (e.g. no use of sleep methods etc.).
 * 
 * Failure to comply with the above will mean that your code will not be marked and you will receive a mark of 0.
 * 
 */


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class TenPinManager {
	ReentrantLock lock = new ReentrantLock();
	private Map<String, Booking> bookings = new HashMap<>();
	
	void bookLane(String bookersName, int nPlayers) {
		lock.lock();
		Condition cond = lock.newCondition();
		
		// TODO: Make sure booking does not already exist
		Booking b = new Booking(cond, nPlayers);
		bookings.put(bookersName, b);
		lock.unlock();
	}; 

	void playerLogin(String bookersName) {
		// TODO: Allow login when booking doesn't already exist (at the moment bookings have to exist first)
		lock.lock();
		Booking b = bookings.get(bookersName);
		b.addPlayer();
		if (b.getCurrentPlayers() == b.getRequiredPlayers()) {
			b.getCond().signalAll();
			
		} else {
			try {
				b.getCond().await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		lock.unlock();
		
	}; 
	

	
	
	
	// You may add private classes and methods as below:
	
	private class Booking {
		private Condition cond;
		private int requiredPlayers;
		private int currentPlayers = 0;
		
		public Booking(Condition cond, int requiredPlayers) {
			this.cond = cond;
			this.requiredPlayers = requiredPlayers;
		}
		
		public int getRequiredPlayers() {
			return requiredPlayers;
		}
		
		public int getCurrentPlayers() {
			return currentPlayers;
		}
		
		public void addPlayer() {
			currentPlayers++;
		}
		
		public Condition getCond() {
			return cond;
		}
	}

	private void privateMethod() {
		//Your code here
	}
}
