package chenji.clustering;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class K_means {

	private Vector<Point> pointv;
	private Vector<Point> seed;

	public class Point implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private float x;
		private float y;
		private int group;

		public String toString() {
			return x + "   " + y + "\n";
		}

		public Point() {
			Random random = new Random();
			x = random.nextFloat() * 100;
			y = random.nextFloat() * 100;
			group = 0;
		}

		public Point(float data) {
			x = data;
			y = 1;
			group = 0;
		}

		public Point(float datax, float datay) {
			x = datax;
			y = datay;
			group = 0;
		}

		public int getgroup() {
			return group;
		}

		public float getx() {
			return x;
		}

		public float gety() {
			return y;
		}

		public void setxy(float datax, float datay) {
			x = datax;
			y = datay;
		}

		public void setgroup(int data) {
			group = data;
		}

	}

	public K_means() {
		pointv = new Vector<Point>();
		seed = new Vector<Point>();
	}

	public K_means(float[] a) {
		pointv = new Vector<Point>();
		seed = new Vector<Point>();
		for (int i = 0; i < a.length; i++) {
			pointv.add(new Point(a[i]));
		}
	}

	public void GenerateTestData() {
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			pointv.add(new Point(random.nextFloat() * 100,
					random.nextFloat() * 100));
		}
	}

	private void GenerateSeedInGernal(int seednum) {
		Random random = new Random();
		int tmp;
		for (int i = 0; i < seednum; i++) {
			tmp = random.nextInt() % pointv.size();
			if (tmp < 0)
				tmp = tmp * (-1);
			seed.add(new Point(pointv.elementAt(tmp).getx(), pointv.elementAt(
					tmp).gety()));
		}
	}

	private float distance(Point a, Point b) {
		float x, y;
		x = a.getx() - b.getx();
		y = a.gety() - b.gety();
		return x * x + y * y;
	}

	private int nearest(Point a) {
		float min = distance(a, seed.elementAt(0));
		float dist;
		int group = 0;
		for (int i = 1; i < seed.size(); i++) {
			dist = distance(a, seed.elementAt(i));
			if (dist < min) {
				group = i;
				min = dist;
			}
		}
		return group;
	}

	public void printResult() {

		Vector<Point> p;
		System.out.println(pointv);
		for (int i = 0; i < seed.size(); i++) {
			p = new Vector<Point>();
			for (int j = 0; j < pointv.size(); j++) {
				if (i == pointv.elementAt(j).getgroup())
					p.add(pointv.elementAt(j));
			}
			System.out.println("以下是第" + i + "组输出");
			System.out.println(p);
		}
	}

	public void processInGrenal(int seednum) {
		GenerateSeedInGernal(seednum);
		process();
	}

	public void processInKpp(int seednum) {
		GenerateSeedInGernal(seednum);
		process();
	}

	private void process() {
		int change, group;
		float x, y;
		int tmp;
		do {
			change = 0;
			for (int i = 0; i < pointv.size(); i++) {
				group = nearest(pointv.elementAt(i));
				if (group != pointv.elementAt(i).getgroup()) {
					change++;
					pointv.elementAt(i).setgroup(group);
				}
			}
			for (int i = 0; i < seed.size(); i++) {
				seed.elementAt(i).setxy(0, 0);
				seed.elementAt(i).setgroup(0);
			}
			for (int i = 0; i < pointv.size(); i++) {
				group = pointv.elementAt(i).getgroup();
				x = seed.elementAt(group).getx() + pointv.elementAt(i).getx();
				y = seed.elementAt(group).gety() + pointv.elementAt(i).gety();
				seed.elementAt(group).setxy(x, y);
				tmp = seed.elementAt(group).getgroup() + 1;
				seed.elementAt(group).setgroup(tmp);
			}
			for (int i = 0; i < seed.size(); i++) {
				x = seed.elementAt(i).getx() / seed.elementAt(i).getgroup();
				y = seed.elementAt(i).gety() / seed.elementAt(i).getgroup();
				seed.elementAt(i).setxy(x, y);
			}

		} while (change > (pointv.size() * 0.1));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// float[] a = { (float) 33.424, (float) 49.611675, (float) 57.160217,
		// (float) 25.07562, (float) 87.36992, (float) 48.088985,
		// (float) 5.4146647, (float) 71.356766, (float) 97.27912,
		// (float) 50.43108 };
		K_means k = new K_means();
		k.GenerateTestData();
		// System.out.println(k.pointv);
		k.processInGrenal(3);
		k.printResult();
	}

}
