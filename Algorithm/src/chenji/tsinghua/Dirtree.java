package chenji.tsinghua;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * D:\数据结构与算法\清华作业 目录树
 */
public class Dirtree {

	private static class dir {
		private String name;
		private int dep;
		private dir child;
		private dir right;

		public dir() {
			name = null;
			dep = -1;
			child = null;
			right = null;
		}

		public dir(String name, int dep) {
			this.child = null;
			this.right = null;
			this.name = name;
			this.dep = dep;
		}

		public int getDep() {
			return dep;
		}

		public String getName() {
			return name;
		}

		public dir getChild() {
			return child;
		}

		public dir getRight() {
			return right;
		}

		public void setChild(dir child) {
			this.child = child;
		}

		public void setRight(dir right) {
			this.right = right;
		}

		public void printDir() {
			for (int i = 0; i < dep*2; i++)
				System.out.print(' ');
			System.out.println(name);
			if (child != null)
				child.printDir();
			if (right != null)
				right.printDir();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int N;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				"D:\\数据结构与算法\\清华作业\\Dirtree.txt"));
		String string = bufferedReader.readLine();
		String[] strings;
		dir root = new dir("/", -1);
		N = Integer.parseInt(string);
		while (N-- != 0) {
			string = bufferedReader.readLine();
			strings = string.split("/");
			int i;
			dir tmp = root.getChild();
			dir preTmp = null;
			if(tmp == null){
				tmp = root;
				for (i = 0; i < strings.length; i++) {
					dir a = new dir(strings[i], i);
					tmp.setChild(a);
					tmp = a;
				}
				continue;
			}
			for (i = 0; i < strings.length; i++) {
				boolean flag = true;
				preTmp = tmp;
				while (tmp != null){
					preTmp = tmp;
					if (tmp.getName().equals(strings[i])) {
						flag = false;
						break;
					}
					tmp = tmp.getRight();
				}
				if (flag)
					break;
				tmp = tmp.getChild();
			}
			if (i < strings.length) {
				dir newDir = new dir(strings[i], i);
				if(preTmp.getRight() != null)
					newDir.setRight(preTmp.getRight());
				preTmp.setRight(newDir);
				i++;
				for (; i < strings.length; i++) {
					tmp = new dir(strings[i], i);
					newDir.setChild(tmp);
					newDir = newDir.getChild();
				}
			}

		}

		root.child.printDir();
	}

}
