import java.math.BigInteger;
import java.util.ArrayList;


public class Counting {
	static int count;
	
	public static void main(String[] args){
		int size = 128;
		int[] a = new int[size];
		for (int i = 0; i < size; i++){
			a[i] = i+1;
		}
		
		int minComparisons = size;
		int maxComparisons = 0;
		int avgComparisons = 0;
		
		for (int i = 1; i <= size+1; i++){
			count = 0;
			linearSearch(a, i);
			if (count > maxComparisons) maxComparisons = count;
			if (count < minComparisons) minComparisons = count;
			avgComparisons += count;
		}
		
		System.out.println("Best: " + minComparisons);
		System.out.println("Worst: " + maxComparisons);
		System.out.println("Average: " + avgComparisons/(size+1));
		
	}

	public static int linearSearch(int[]a, int e){
		for (int i = 0; i < a.length; i++){
			count++;
			if (a[i] == e) return i;
		}
		return -1;
	}
	
	public static int binarySearch(int[]a, int e){
		int low = 0;
		int high = a.length-1;
		int mid;
		while (low <= high){
			count++;
			mid = (low + high) / 2;
			if (a[mid] < e) low = mid + 1;
			else if (a[mid] > e) high = mid - 1;
			else return mid;
		}
		return -1;
	}

}