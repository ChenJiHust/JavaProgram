package cj.test.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PrintingContainers {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(fill(new ArrayList()));
		System.out.println(fill(new HashSet()));
		HashMap a = new HashMap();
		a.put("dog", "Bosco");
		System.out.println(a);
		Set b = new HashSet();
		b.add(1);
		b.add(2);
		
		Iterator e = b.iterator();
		while(e.hasNext())
			System.out.println(e.next().toString());
		System.out.println(a.get("dog").toString());
		

	}

	@SuppressWarnings("unchecked")
	public static Collection fill(Collection c){
		c.add("dog");
		c.add("dog");
		c.add("cat");
		return c;
	}
	
	@SuppressWarnings("unchecked")
	public static Map fill(Map m){
		m.put("dog","Bosco");
		return m;
	}
}
