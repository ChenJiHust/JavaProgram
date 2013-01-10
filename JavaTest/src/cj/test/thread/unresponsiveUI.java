package cj.test.thread;

public class unresponsiveUI {
	private volatile double d =1;
	public unresponsiveUI() throws Exception{
		while(d > 0){
			d = d+(Math.E+Math.PI)/d;
		}
		System.in.read();
	}

}
