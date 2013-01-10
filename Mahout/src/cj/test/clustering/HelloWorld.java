package cj.test.clustering;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.VectorWritable;

/*
 * 测试K-Means算法。
 * 
 * 使用KMeansDriver.run()
 * 
 * 输入格式  <LongWritable.class, VectorWritable.class>
 * 种子点格式<Text.class, Kluster.class>
 * 
 * 输出格式<IntWritable.class, WeightedVectorWritable.class>
 * */
public class HelloWorld {

	public static final double[][] points = { { 1, 1 }, { 2, 1 }, { 1, 2 },
			{ 2, 2 }, { 3, 3 }, { 8, 8 }, { 9, 8 }, { 8, 9 }, { 9, 9 } };

	public static List<Vector> getPoints(double[][] raw) {
		List<Vector> points = new ArrayList<Vector>();
		for (int i = 0; i < raw.length; i++) {
			double[] fr = raw[i];

			/* 向量类型 */
			Vector vec = new RandomAccessSparseVector(fr.length);
			vec.assign(fr);
			points.add(vec);
		}
		return points;
	}

	public static void writePointsToFile(List<Vector> points, String fileName,
			FileSystem fs, Configuration conf) throws IOException {
		Path path = new Path(fileName);

		/* 流式写文件 */
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path,
				LongWritable.class, VectorWritable.class);
		long recNum = 0;
		VectorWritable vec = new VectorWritable();
		for (Vector point : points) {
			vec.set(point);
			writer.append(new LongWritable(recNum++), vec);
		}
		writer.close();
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		// TODO Auto-generated method stub
		int k = 2;
		List<Vector> vectors = getPoints(points);

		File testData = new File("chenjitestdata");
		if (!testData.exists()) {
			testData.mkdir();
		}
		testData = new File("chenjitestdata/points");
		if (!testData.exists()) {
			testData.mkdir();
		}
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		writePointsToFile(vectors, "chenjitestdata/points/file1", fs, conf);

		Path path = new Path("chenjitestdata/clusters/part-00000");
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path,
				Text.class, Kluster.class);
		for (int i = 0; i < k; i++) {
			Vector vec = vectors.get(i);
			Kluster cluster = new Kluster(vec, i,
					new EuclideanDistanceMeasure());
			writer.append(new Text(cluster.getIdentifier()), cluster);
		}
		writer.close();

		KMeansDriver.run(conf, new Path("chenjitestdata/points"), new Path(
				"chenjitestdata/clusters"), new Path("chenjioutput"),
				new EuclideanDistanceMeasure(), 0.001, 10, true, 0.2, false);

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
				"chenjioutput/" + Kluster.CLUSTERED_POINTS_DIR
						+ "/part-m-00000"), conf);
		/* K-Means算法 结果格式 */
		IntWritable key = new IntWritable();
		WeightedVectorWritable value = new WeightedVectorWritable();
		while (reader.next(key, value)) {
			System.out.println(value.toString() + " belongs to cluster "
					+ key.toString());
		}
		reader.close();
	}

}
