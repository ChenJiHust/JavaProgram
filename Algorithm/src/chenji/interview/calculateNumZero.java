package chenji.interview;

/**
 * 求1000的阶乘结果尾部0的个数
 */
public class calculateNumZero {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(calculate(1000));
	}

	/**
	 * 0的个数与2和5有关，因此找出因子为5的总数即可（因子为2的个数比5多）。
	 * 
	 */
	public static int calculate(int num) {
		int sum = 0;
		for (int i = 5; i <= num; i += 5)
			sum += calDivisor(i);
		return sum;
	}

	/**
	 * 计算因子5的个数
	 */
	public static int calDivisor(int num) {
		int sum = 0;
		while(num%5 == 0){
			sum++;
			num/=5;
		}
		return sum;
	}

}
