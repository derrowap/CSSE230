public class AnalysisHW {
	static long count;
	static long notCount;
	static long yesFor = 0;
	static long noFor = 0;
	static int size = 10000;
	static boolean passedAll;
	
	final static float nanoToMs = 10 * 10 * 10 * 10 * 10 * 10;

	public static void main(String[] args) {
		passedAll = true;
//		testFragment5();
		passedAll = true;
//		testFragment6();
		passedAll = true;
//		testFragment7();
		passedAll = true;
//		testFragment8();
		passedAll = true;
		testFragment9();
	}

	public static void testFragment5() {
		for (int i = 1; i <= size + 1; i++) {
			count = 0;
			final long startTime = System.nanoTime();
			fragment5(i);
			final long endTime = System.nanoTime();
			System.out.println(((i * i * i) == count) + " N: " + i
					+ "  Runtime: "
					+ ((float) (endTime - startTime) / (Math.pow(10, 6)))
					+ "ms");
			System.out.println();
		}

		final long startTime2 = System.currentTimeMillis();
		fragment5(1000);
		final long endTime2 = System.currentTimeMillis();
		System.out.println("N: " + 1000 + "  Runtime: "
				+ (endTime2 - startTime2) + "ms");

		final long startTime3 = System.currentTimeMillis();
		fragment5(10000);
		final long endTime3 = System.currentTimeMillis();
		System.out.println("N: " + 10000 + "  Runtime: "
				+ (endTime3 - startTime3) + "ms");

		System.out.println("-----------------");
		System.out.println("Linear: " + size);
		System.out.println("Log(N): " + Math.log(size) / Math.log(2));
		System.out.println("N * Log(N): " + size
				* (Math.log(size) / Math.log(2)));
		System.out.println("N^2: " + size * size);
		System.out.println("N^3: " + size * size * size);
	}

	public static void testFragment6() {
		int[] cases = {1, 10, 100, 1000, 10000, 100000, 1000000};
		for (int i = 1; i <= size; i++) {
			count = 0;
			fragment6(i);
			if (count > i * i) {
				System.out.println("NOT EQUAL--> Count = " + count
						+ " and N^2 = " + i * i);
				passedAll = false;
			}
		}
		if(passedAll) System.out.println("Passed all values of n from 1 to " + size);
		System.out.println();
		
		for(int i : cases) {
			long startTime = System.nanoTime();
			fragment6(i);
			long endTime = System.nanoTime();
			System.out.println("N = " + i + "   Runtime = " + (float)(endTime - startTime)/nanoToMs + " ms");
		}
	}
	
	public static void testFragment7() {
		int[] cases = {1, 10, 100, 1000, 10000};
		for (int i = 1; i <= size; i++) {
			count = 0;
			fragment7(i);
			if (count > i * i * i * i * i) {
				System.out.println("NOT EQUAL--> Count = " + count
						+ " and N^5 = " + (i * i * i * i * i) + "  Size = " + i);
				passedAll = false;
				break;
			}
		}
		if(passedAll) System.out.println("Passed all values of n from 1 to " + size);
		System.out.println();
		
		for(int i : cases) {
			long startTime = System.nanoTime();
			fragment7(i);
			long endTime = System.nanoTime();
			System.out.println("N = " + i + "   Runtime = " + (float)(endTime - startTime)/nanoToMs + " ms");
		}
	}
	
	public static void testFragment8() {
		long[] cases = new long[5];
		int index = 19;
		for(int i = 0; i < 5; i++) {
			long addTo = 2;
			for(int j = 0; j < index; j++) {
				addTo = addTo * 2;
			}
			System.out.println(addTo);
			index+=10;
			cases[i] = addTo;
		}
		for (int i = 1; i <= size; i++) {
			count = 0;
			fragment8(i);
			if (count > Math.log(i) / Math.log(2) + 1) {
				System.out.println("NOT EQUAL--> Count = " + count
						+ " and log2(N) = " + (Math.log(i) / Math.log(2) + 1) + "  Size = " + i);
				passedAll = false;
				break;
			}
		}
		if(passedAll) System.out.println("Passed all values of n from 1 to " + size);
		System.out.println();
		
		for(long i : cases) {
			count = 0;
			long startTime = System.nanoTime();
			fragment8(i);
			System.out.println("Count: " + count);
			long endTime = System.nanoTime();
			System.out.println("N = " + i + "   Runtime = " + (float)(endTime - startTime) + " ns");
			System.out.println();
		}
	}
	
	public static void testFragment9() {
		int[] cases = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100, 1000, 10000};
		for (int i = 1; i <= size; i++) {
			count = 0;
			notCount = 0;
			fragment9(i);
			if (count > i * i * i * i) {
				System.out.println("NOT EQUAL--> Count = " + count
						+ " and N^4 = " + (i * i * i * i) + "  Size = " + i);
				passedAll = false;
				break;
			}
		}
		if(passedAll) System.out.println("Passed all values of n from 1 to " + size);
		System.out.println();
		
		for(int i : cases) {
			count =0;
			notCount = 0;
			yesFor = 0;
			noFor = 0;
			long startTime = System.nanoTime();
			System.out.println("largest inside: " + fragment9(i));
			long endTime = System.nanoTime();
			System.out.println("yesFor: " + yesFor + "   noFor: " + noFor);
			System.out.println("Count: " + count + "   notCount: " + notCount);
			System.out.println("N = " + i + "   Runtime = " + (float)(endTime - startTime)/nanoToMs + " ms");
			System.out.println();
		}
	}

	public static int fragment5(int n) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n * n; j++) {
				count++;
			}
		}
		return -1;
	}

	public static int fragment6(int n) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < i; j++) {
				count++;
			}
		}
		return -1;
	}

	public static int fragment7(int n) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n * n; j++) {
				for (int k = 0; k < j; k++) {
					count++;
				}
			}
		}
		return -1;
	}

	public static int fragment8(long n) {
		for(long i = 1; i < n; i = i * 2) {
			count++;
		}
		return -1;
	}
	
	public static long fragment9(int n) {
		long largestInside = 0;
		for(int i = 1; i <= n; i++) {
			for(int j = 1; j <= i * i; j++) {
				if(j % i == 0) {
					yesFor++;
					for(int k = 0; k < j; k++) {
						count++;
						notCount++;
					}
					if(notCount > largestInside) largestInside = notCount;
					notCount = 0;
				} else noFor++;
			}
		}
		
		return largestInside;
	}

}
