package chenji.interview;

import java.util.Random;

/**
 * 栈的操作
 * 
 */
public class stack {
	private int[] st;
	private int max;
	private int sp = -1;

	public stack() {
		max = 10;
		st = new int[max];
	}

	public stack(int max) {
		this.max = max;
		st = new int[max];
	}

	public int pop() {
		if (sp == -1)
			return Integer.MIN_VALUE;
		return st[sp--];
	}

	public void push(int value) {
		if (sp == max - 1)
			return;
		st[++sp] = value;
	}

	public void print() {
		int i = sp;
		for (; i >= 0; i--) {
			System.out.print(st[i]);
			System.out.print(" ");
		}
		System.out.println();
	}

	/**
	 * 随机初始化栈
	 * 
	 * @param count
	 *            初试入栈数量
	 */
	public void initRandom(int count) {
		Random random = new Random();
		int tmp;
		if (count > max)
			count = max;
		for (int i = 0; i < count; i++){
			tmp = random.nextInt()% 100;
			push(tmp);
		}
	}
	
	
	public void reverseStack(){
		int tmp = pop();
		if(tmp != Integer.MIN_VALUE)
			reverseStack();
		else 
			return;		
		push(tmp);
	}
	public static void main(String[] args) {
		stack st = new stack();
		st.initRandom(3);
		st.print();
		st.reverseStack();
		st.print();
	}
}
