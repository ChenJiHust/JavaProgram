package chenji.tsinghua;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * D:\数据结构与算法\清华作业 股票
 */
public class Stock {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int N;
		int start = 0;
		int end = 0;
		int sumDay = 0;
		int sumStock = 0;
		int current = 0;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				"D:\\数据结构与算法\\清华作业\\stock.txt"));
		String string = bufferedReader.readLine();
		String[] strings;
		N = Integer.parseInt(string);
		int[] stock = new int[N];        //保存股票的购买量  start 到 end 中间的表示活跃股票
		while ((string = bufferedReader.readLine()) != null) {
			strings = string.split(" ");
			int tmp = Integer.parseInt(strings[0]);
			if (strings.length == 2) {
				stock[end++] = Integer.parseInt(strings[1]);   //新的股票上市
			} else {
				start++;                                       //有股票退市
			}
			sumDay += tmp;
			sumStock += tmp * current;
			if(start < end)                                    //计算新的购买的股票量
				current = stock[start];
			for (int i = start + 1; i < end; i++) {
				if (current < stock[i])
					current = stock[i];
			}
		}
		System.out.println(sumStock / sumDay);

	}

}
