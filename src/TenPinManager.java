


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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class TenPinManager implements Manager {
	ReentrantLock lock = new ReentrantLock();
	private Map<String, List<Booking>> bookings = new HashMap<>();
	private Map<String, Condition> bookingConds = new HashMap<>();
	private Map<String, Integer> waitingCount = new HashMap<>();
	
	public void bookLane(String bookersName, int nPlayers) {
		lock.lock();
		
		try {
			// Create a new condition variable if one does not already exist
			if (!bookingConds.containsKey(bookersName)) {
				Condition cond = lock.newCondition();
				bookingConds.put(bookersName, cond);
			}
			
			// If there is no waiting count for this booking name, initialise to zero
			if (!waitingCount.containsKey(bookersName)) {
				waitingCount.put(bookersName, 0);
			}
			
			Condition cond = bookingConds.get(bookersName);
			if (!bookings.containsKey(bookersName)) {
				bookings.put(bookersName, new ArrayList<>());
			}
			List<Booking> bookingList = bookings.get(bookersName);
			Booking b = new Booking(nPlayers);
			bookingList.add(b);
			
			// Now check to see if the first booking can take place
			int wc = waitingCount.get(bookersName);
			if (wc >= b.getRequiredPlayers()) {
				for (int i = 0; i < b.getRequiredPlayers(); i++) {
					cond.signal();
				}

				waitingCount.put(bookersName, wc - b.getRequiredPlayers());

				bookingList.remove(b);
			} 
		} finally {
			lock.unlock();
		}
		
	}; 

	public void playerLogin(String bookersName) {
		lock.lock();
		
		try {
			List<Booking> bookingList = bookings.get(bookersName);
			Condition cond = bookingConds.get(bookersName);
			if (cond == null) {
				cond = lock.newCondition();
				bookingConds.put(bookersName, cond);
			}
			int val = 0;
			if (waitingCount.containsKey(bookersName)) {
				val = waitingCount.get(bookersName);
			}
			waitingCount.put(bookersName, val + 1);
			
			if (bookingList == null || bookingList.size() == 0) {
				try {
					cond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// No need to unlock here, finally will do that when returning
				return;
			}
			Booking b = bookingList.get(0);
			// If there are enough threads waiting for this booking to start
			if (waitingCount.get(bookersName) >= b.getRequiredPlayers()) {
				val = waitingCount.get(bookersName);
				waitingCount.put(bookersName, val - b.getRequiredPlayers());
				for (int i = 0; i < b.getRequiredPlayers() - 1; i++) {
					cond.signal();
				}

				// Remove the booking after player requirement is met
				bookings.get(bookersName).remove(b);

			} else {
				try {
					cond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} finally {
			lock.unlock();
		}
		
		
	}; 
	
	// You may add private classes and methods as below:
	
	private class Booking {
		private int requiredPlayers;
		
		public Booking(int requiredPlayers) {
			this.requiredPlayers = requiredPlayers;
		}
		
		public int getRequiredPlayers() {
			return requiredPlayers;
		}
	}
}
