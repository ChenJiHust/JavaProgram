package base;

import java.io.Serializable;

public class data implements Serializable{
	private static final long serialVersionUID = 1L;
	private String txt;
	private int mark;
	public data(){
		txt=null;
		mark=0;
	}
	public String toString(){
		return txt + "  " + mark +"\n";
	}
	public data(String s){
		txt=s;
	}
	public data(String s,int b){
		txt=s;
		mark=b;
	}
	public String get_txt(){
		return txt;
	}
	public int get_mark(){
		return mark;
	}
	public void set_mark(int b){
		mark=b;
	}
}
