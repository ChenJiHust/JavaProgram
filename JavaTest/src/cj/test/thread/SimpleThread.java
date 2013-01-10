package cj.test.thread;

public class SimpleThread extends Thread{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0;i<5;i++)
			new SimpleThread();
	}

	private int countDown = 5;
	private static int threadCount = 0;
	
	public SimpleThread(){
		super("" + ++threadCount);
		setDaemon(true);
		start();
	}
	public void run() {
		while(true){
			System.out.println(this);
			if(--countDown == 0){
				return;
			}
			yield();
		}
	}
	public String toString() {
		return "#" + getName()+":" +countDown;
	}
}
