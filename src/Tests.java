
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
