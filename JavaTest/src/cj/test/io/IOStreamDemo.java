package cj.test.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOStreamDemo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(
				new FileReader(
						"D:\\hadoop\\program\\JavaTest\\src\\cj\\test\\io\\IOStreamDemo.java"));
		String s, s2 = new String();
		while ((s = in.readLine()) != null)
			s2 += s + "\n";
		System.out.println(s2);
		in.close();
	}

}
