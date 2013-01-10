package cj.test.thread;

public class AlwaysEven {
	private int i = 0;

	synchronized public void next() {
		i++;
		i++;
	}

	synchronized public int getValue() {
		return i;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final AlwaysEven ae = new AlwaysEven();
		new Thread("watcher") {
			public void run() {
				while (true) {
					int val = ae.getValue();
					if (val % 2 != 0) {
						System.out.println(val);
						System.exit(0);
					}
				}

			}
		}.start();
		while (true) {
			ae.next();
		}
	}

}
