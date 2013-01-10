package chenji.interview;

import java.util.Random;

public class TopK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TopK x = new TopK(30);
		x.print();
		x.findTopK(3);
		return;
	}

	private int[] array;

	public TopK(int lengh) {
		// TODO Auto-generated constructor stub
		array = new int[lengh];
		Random random = new Random();
		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt() % 100;
		}
	}

	public void findTopK(int k) {
		find(0, this.array.length - 1, k);
		return;
	}

	private void find(int start, int end, int k) {
		int tmp;
		if (k == 0) {
			return;
		}
		if (start == end) {
			print(start, end);
			return;
		}
		tmp = partion_random(start, end);
		// tmp = partion(start, end);
		if ((end - tmp + 1) == k) {
			print(tmp, end);
		} else if ((end - tmp + 1) < k) {
			print(tmp, end);
			find(start, tmp - 1, k - (end - tmp + 1));
		} else {
			find(tmp + 1, end, k);
		}
		return;
	}

	private int partion_random(int start, int end) {
		Random random = new Random();
		int tmp = random.nextInt() % (end - start);
		if (tmp < 0)
			tmp = tmp * (-1);
		swap(start, start + tmp);
		return partion(start, end);
	}

	private int partion(int start, int end) {
		int tmp = array[start];
		int datum = tmp;
		while (start < end) {
			while ((array[end] >= datum) && (start < end)) {
				end--;
			}
			array[start] = array[end];
			while ((array[start] < datum) && (start < end)) {
				start++;
			}
			array[end] = array[start];
		}
		array[start] = tmp;
		return start;
	}

	private void swap(int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
		return;
	}

	private void print(int start, int end) {
		String str = new String();
		for (int i = start; i <= end; i++) {
			str += String.valueOf(array[i]) + "\t";
		}
		System.out.print(str);
		return;
	}

	public void print() {
		String x = new String();
		for (int i = 0; i < array.length; i++) {
			x += (String.valueOf(array[i]) + "\t");
		}
		x += "\n";
		System.out.println(x);
	}

}
