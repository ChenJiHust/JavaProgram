package chenji.datastructure;

import java.util.Random;

/**
 * 
 * 各种排序算法实现 按照从小到大的顺序
 * 
 * 选择排序 冒泡排序 插入排序 堆排序 希尔排序 快速排序
 */
public class Sort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sort aSort = new Sort();
		aSort.print();
		aSort.quickSort(false);
		aSort.print();
		return;
	}

	private int[] array;

	public Sort(int[] arr) {
		array = arr;
	}

	public Sort() {
		array = new int[10];
		Random random = new Random();
		for (int i = 0; i < array.length; i++) {
			array[i] = random.nextInt() % 100;
		}
	}

	public void print() {
		String x = new String();
		for (int i = 0; i < array.length; i++) {
			x += (String.valueOf(array[i]) + "\t");
		}
		x += "\n";
		System.out.println(x);
	}

	private void swap(int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
		return;
	}

	/**
	 * 希尔排序
	 */
	public void shellSort() {
		int tmp, j;
		int gap = 0;
		do {
			gap = gap * 3 + 1;
		} while (gap < array.length);

		while (gap > 0) {
			for (int i = gap; i < array.length; i++) {
				tmp = array[i];
				j = i - gap;
				while ((j >= 0) && (array[j] > tmp)) {
					array[j + gap] = array[j];
					j -= gap;
				}
				array[j + gap] = tmp;
			}
			gap = (gap - 1) / 3;
		}
		return;
	}

	/**
	 * 选择排序
	 */
	public void selectSort() {
		int max = 0;
		for (int i = array.length - 1; i > 0; i--) {
			max = i;
			for (int j = 0; j < i; j++) {
				if (array[max] < array[j])
					max = j;
			}
			if (max != i)
				swap(i, max);
		}
		return;
	}

	/**
	 * 冒泡排序
	 */
	public void bubbleSort() {
		boolean flag = false;
		for (int j = array.length - 1; j > 0; j--) {
			flag = false;
			for (int i = 1; i <= j; i++) {
				if (array[i - 1] > array[i]) {
					swap(i, i - 1);
					flag = true;
				}
			}
			if (!flag)
				break;
		}
		return;
	}

	/**
	 * 插入排序
	 */
	public void insertionSort() {
		int j, tmp;
		for (int i = 0; i < array.length - 1; i++) {
			j = i + 1;
			tmp = array[j];
			while ((j > 0) && (array[j - 1] > tmp)) {
				array[j] = array[--j];
			}
			array[j] = tmp;
		}
		return;
	}

	/**
	 * 堆排序
	 */
	public void heapsort() {
		creatheap();
		for (int i = array.length - 1; i > 0; i--) {
			swap(i, 0);
			shift(0, i - 1);
		}
		return;
	}

	private void shift(int start, int end) {
		int tmp = start * 2 + 1;
		while (tmp <= end) {
			if (((tmp + 1) < end) && (array[tmp] < array[tmp + 1]))
				tmp++;
			if (array[start] < array[tmp]) {
				swap(start, tmp);
				start = tmp;
				tmp = start * 2 + 1;
			} else {
				break;
			}
		}
		return;
	}

	/**
	 * 初始化堆
	 */
	private void creatheap() {
		for (int i = array.length / 2; i >= 0; i--) {
			shift(i, array.length - 1);
		}
		return;
	}

	/**
	 * 快速排序
	 * 
	 * @param flag
	 *            是否随机选择种子点
	 */
	public void quickSort(boolean flag) {
		quickSort(0, array.length - 1, flag);
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

	private int partion_random(int start, int end) {
		Random random = new Random();
		int tmp = random.nextInt() % (end - start);
		if (tmp < 0)
			tmp = tmp * (-1);
		swap(start, start + tmp);
		return partion(start, end);
	}

	private void quickSort(int start, int end, boolean flag) {
		int tmp;
		if (end == start) {
			return;
		} else {
			if (!flag)
				tmp = partion(start, end);
			else {
				tmp = partion_random(start, end);
			}
			if (tmp > start)
				quickSort(start, tmp - 1, flag);
			if (tmp < end)
				quickSort(tmp + 1, end, flag);
			return;
		}
	}

}
