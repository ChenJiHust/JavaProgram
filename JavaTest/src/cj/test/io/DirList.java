package cj.test.io;

import java.io.File;
import java.io.IOException;

public class DirList {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File path = new File("D:\\unix编程艺术");
		String[] list;
		if (args.length == 0)
			list = path.list();
		else
			list = path.list(new DirFilter(args[0]));
		System.out.println(path.getAbsolutePath() + path.getAbsoluteFile().getPath());
		for (int i = 0; i < list.length; i++)
			System.out.println(list[i]);
	}
}
