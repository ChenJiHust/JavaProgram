package cj.test.thread;

public class ResponsiveUI extends Thread {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		new unresponsiveUI();
		new ResponsiveUI();
		Thread.sleep(300);
		System.in.read();
		System.out.println(d);

	}

	private static volatile double d = 1;

	public ResponsiveUI() {
		setDaemon(true);
		start();
	}

	public void run() {
		while (true) {
			d = d + (Math.PI + Math.E) / d;
		}
	}

}
