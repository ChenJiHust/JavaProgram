package chenji.interview;

//全排列是将一组数按一定顺序进行排列，如果这组数有n个，那么全排列数为n!个。
public class Permutation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Permutation a = new Permutation();
		a.permutation(0);
		System.out.println(a.getCount());

	}

	private int[] array;
	private int count;

	public Permutation() {
		array = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		count = 0;
	}

	public void permutation(int k) {
		if (k == array.length - 1)
			count++;
		else {
			for (int i = k; i < array.length; i++) {
				swap(k, i);
				permutation(k + 1);
				swap(k, i);
			}
		}
	}

	private void swap(int k, int m) {
		int tmp = array[k];
		array[k] = array[m];
		array[m] = tmp;
	}

	public int getCount() {
		return count;
	}

}
