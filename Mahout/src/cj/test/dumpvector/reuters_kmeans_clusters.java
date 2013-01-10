package cj.test.dumpvector;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.clustering.kmeans.Kluster;

public class reuters_kmeans_clusters {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		int k = 20;

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
				"reuters-kmeans-clusters/" + Kluster.CLUSTERED_POINTS_DIR
						+ "/part-m-00000"), conf);

		/* K-Means算法 结果格式 */
		IntWritable key = new IntWritable();
		WeightedVectorWritable value = new WeightedVectorWritable();
		while ((k-- > 0) && reader.next(key, value)) {
			System.out.println(" cluster " + key.toString() + " contains "
					+ value.toString());
		}
		reader.close();
	}

}
