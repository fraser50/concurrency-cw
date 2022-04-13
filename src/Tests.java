
/*
 * Test Java classes module
 * 
 * Contains one example test
 * 
 * You must write your other tests in this file
 * 
 * Note that you can use any libraries here for your tests that are available in the standard HWU CS Java version
 * 
 * For instance, this example uses a 'thread safe' AtomicInteger.
 * 
 *  NOTE: you are NOT allowed to use any thread safe libraries in TenPinManager.java
 *  
 */



import java.util.concurrent.atomic.AtomicInteger;
//Note that you can use thread safe classes in your Tests.java


public class Tests {
	final int  testTimeout = 10; //mS
	AtomicInteger nThreadsReturned = new AtomicInteger(0);


	public void test_basic_1 (){	
		System.out.println("This test books 1 lane for 6 players in the name of 'Jane' and then creates 7 player threads that try to login.");
		System.out.println("Expected behaviour: 6 players return from tenPinManager.playerLogin, 1 player indefinitely waits");

		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jane", 6);
		
		for (int i=0; i < 7; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jane");
			player.start();
		}
		//Now wait for player threads to do their thing:
		try {Thread.sleep(testTimeout);} catch (InterruptedException e) {e.printStackTrace();} 
		
		//Test result
		if(nThreadsReturned.get() == 6) System.out.println ("Test = SUCCESS");
		else System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
	}
	
	public void test_ur2_1() {
		System.out.println("This test books 2 lanes, the firs for 5 players, and the second for 1 player, the booking is done with the name 'Jane', 1 player thread will try to login");
		System.out.println("Expected behaviour: 1 thread is waiting after logging in");
		
		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jane", 5);
		tenPinManager.bookLane("Jane", 1);
		
		PlayerThread player = new PlayerThread(tenPinManager, "Jane");
		player.start();
		
		try {
			Thread.sleep(testTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (nThreadsReturned.get() == 0) {
			System.out.println("Test = SUCCESS");
			
		} else {
			System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
		}
	}
	
	public void test_ur2_2() {
		System.out.println("This test book 2 lanes just like test_ur2_1(), and creates 6 player threads, and checks that all of them finish");
		
		TenPinManager tenPinManager = new TenPinManager();
		tenPinManager.bookLane("Jane", 5);
		tenPinManager.bookLane("Jane", 1);
		
		for (int i=0; i<6; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jane");
			player.start();
		}
		
		try {
			Thread.sleep(testTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (nThreadsReturned.get() == 6) {
			System.out.println("Test = SUCCESS");
			
		} else {
			System.out.println ("Test = FAIL: " + nThreadsReturned.get() +" returned.");
		}
	}
	
	public void test_ur3_1() {
		System.out.println("This test creates 2 booking: Jane and Bob, the 2 bookings have 3 and 5 players required respectively.");
		TenPinManager tenPinManager = new TenPinManager();
		
		tenPinManager.bookLane("Jane", 3);
		tenPinManager.bookLane("Bob", 5);
		
		for (int i=0; i<3; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jane");
			player.start();
		}
		
		for (int i=0; i<5; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Bob");
			player.start();
		}
		
		try {
			Thread.sleep(testTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (nThreadsReturned.get() == 8) {
			System.out.println("Test = SUCCESS");
			
		} else {
			System.out.println("Test = FAIL: " + nThreadsReturned.get() + " returned.");
		}
	}
	
	public void test_ur4_1() {
		System.out.println("This test books 3 threads in for Jane, and 2 for Bob, then waites, and then creates the necessary bookings.");
		
		TenPinManager tenPinManager = new TenPinManager();
		
		for (int i=0; i<3; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Jane");
			player.start();
		}
		
		for (int i=0; i<2; i++) {
			PlayerThread player = new PlayerThread(tenPinManager, "Bob");
			player.start();
		}
		
		tenPinManager.bookLane("Jane", 3);
		tenPinManager.bookLane("Bob", 2);
		
		try {
			Thread.sleep(testTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (nThreadsReturned.get() == 5) {
			System.out.println("Test = SUCCESS");
			
		} else {
			System.out.println("Test = FAIL: " + nThreadsReturned.get() + " returned.");
		}
		
	}
	
	public void test_ur4_2() {
		System.out.println("This test logs 5 threads into Jane, and creates 2 bookings for Jane, 3 and 2 players each");
		TenPinManager tenPinManager = new TenPinManager();
		
		for (int i=0; i<5; i++) {
			PlayerThread p = new PlayerThread(tenPinManager, "Jane");
			p.start();
		}
		
		try {
			Thread.sleep(testTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tenPinManager.bookLane("Jane", 3);
		tenPinManager.bookLane("Jane", 2);
		
		try {
			Thread.sleep(testTimeout);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (nThreadsReturned.get() == 5) {
			System.out.println("Test = SUCCESS");
			
		} else {
			System.out.println("Test = FAIL: " + nThreadsReturned.get() + " returned.");
		}
	}
	
	private class PlayerThread extends Thread {
		TenPinManager manager;
		String bookersName;
		PlayerThread (TenPinManager manager, String bookersName){
			this.manager = manager;
			this.bookersName = bookersName;
		};
	    public void run(){
	        manager.playerLogin(bookersName);
	        nThreadsReturned.incrementAndGet();
	    };	
	};
	
	// WRITE YOUR OTHER TESTS HERE ...

}
