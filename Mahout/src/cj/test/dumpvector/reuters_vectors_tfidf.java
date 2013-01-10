package cj.test.dumpvector;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.VectorWritable;

public class reuters_vectors_tfidf {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int k = 20;
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

//		 SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
//		 "reuters-vectors/tf-vectors/part-r-00000"), conf);
		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
				"reuters-vectors/tf-vectors/part-r-00000"), conf);

		Text key = new Text();
		VectorWritable value = new VectorWritable();
		// IntWritable value = new IntWritable();
		while ((k-- > 0) && reader.next(key, value)) {
			System.out.println(key.toString() + "   separate   "
					+ value.toString());
		}
		reader.close();		
	}

}
