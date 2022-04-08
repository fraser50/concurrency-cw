


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
		if (!bookingConds.containsKey(bookersName)) {
			Condition cond = lock.newCondition();
			bookingConds.put(bookersName, cond);
		}
		
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
			System.out.println("YAY (" + bookersName + ")");
			for (int i=0; i<b.getRequiredPlayers(); i++) {
				cond.signal();
				System.out.println("signal");
			}
			
			waitingCount.put(bookersName, wc-b.getRequiredPlayers());
			
			bookingList.remove(b);
		}
		
		lock.unlock();
	}; 

	public void playerLogin(String bookersName) {
		// TODO: Allow login when booking doesn't already exist (at the moment bookings have to exist first)
		lock.lock();
		List<Booking> bookingList = bookings.get(bookersName);
		
		Condition cond = bookingConds.get(bookersName);
		if (cond == null) {
			cond = lock.newCondition();
			bookingConds.put(bookersName, cond);
		}
		
		// This line was previously: waitingCount.put(waitingCount.get(bookersName)+1);
		// The previous version above caused a weird issue where instead of n+1 being put there, null was put there.
		// Getting the value first, then incrementing and putting it fixes the issue
		// Not 100% sure what is going on...
		
		int val = 0;
		if (waitingCount.containsKey(bookersName)) {
			val = waitingCount.get(bookersName);
		}
		
		waitingCount.put(bookersName, val+1);
		
		if (bookingList == null || bookingList.size() == 0) {
			try {
				cond.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lock.unlock();
			return;
		}
		
		Booking b = bookingList.get(0);
		
		// If there are enough threads waiting for this booking to start
		if (waitingCount.get(bookersName) >= b.getRequiredPlayers()) {
			val = waitingCount.get(bookersName);
			waitingCount.put(bookersName, val-b.getRequiredPlayers());
			for (int i=0; i<b.getRequiredPlayers()-1; i++) {
				cond.signal();
			}
			
			// Remove the booking after player requirement is met
			bookings.get(bookersName).remove(b);
			
		} else {
			try {
				cond.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		lock.unlock();
		
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
