

public class Main {

	public static void main(String[] args) {
		
		Tests tests = new Tests();
		
		int numTests = 1;
		
		// If a test fails, no further tests will take place
		for (int i=0; i<numTests; i++) {
			tests.test_basic_1();
			tests.test_ur2_1();
			tests.test_ur2_2();
			tests.test_ur3_1();
			tests.test_ur4_1();
			tests.test_ur4_2();
		}
		
		System.exit(0);

	}

}
