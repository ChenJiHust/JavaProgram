package base;

import java.util.Vector;

public class element {
	public Vector<data> V;
	public element(){
		V=new Vector<data>();
	}
	public String toString(){
		return V.toString();
	}
	public boolean is(int t){
		boolean result=true;
		for(int i=0;i<V.size();i++){
			if(V.elementAt(i).get_mark()!=t){
				result=false;
				break;
			}
		}
		return result;
	}
}
