package cj.test.clustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.VectorWritable;

/**
 *测试 Vector
 * 
 * NamedVector
 */
public class testVector {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path("appledata/apples");
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path,
				Text.class, VectorWritable.class);
		VectorWritable vec = new VectorWritable();

		List<NamedVector> apples = new ArrayList<NamedVector>();
		NamedVector apple;
		apple = new NamedVector(new DenseVector(new double[] { 0.11, 510, 5 }),
				"small green round apple");
		apples.add(apple);

		for (NamedVector namedVector : apples) {
			vec.set(namedVector);
			writer.append(new Text(namedVector.getName()), vec);
		}
		writer.close();

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
		Text key = new Text();
		while (reader.next(key, vec)) {
			System.out.println(key.toString() + "   " + vec.get().toString());
		}
		reader.close();
	}
}
