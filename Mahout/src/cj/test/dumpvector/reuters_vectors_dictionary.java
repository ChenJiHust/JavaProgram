package cj.test.dumpvector;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

public class reuters_vectors_dictionary {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int k = 20;
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
				"reuters-vectors/dictionary.file-0"), conf);
		
		Text key = new Text();
		IntWritable value = new IntWritable();
		while ((k-- > 0) && reader.next(key, value)) {
			System.out.println(key.toString() + "   separate   "
					+ value.toString());
		}
		reader.close();

	}

}
