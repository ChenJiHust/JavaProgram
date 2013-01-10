package cj.test.jpg;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

public class writeJpg {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] fStatus = fs.listStatus(new Path("chensi"));
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, new Path("testjpg"),
				Text.class, jpg.class);
		
		for(FileStatus fileStatus:fStatus){
			if(!fileStatus.isDir()){
				FSDataInputStream in = fs.open(fileStatus.getPath());
				byte buffer[] = new byte[in.available()];
				in.read(buffer);
				jpg j = new jpg(buffer);		
				writer.append(new Text(fileStatus.getPath().getName()), j);
			}
		}		
		writer.close();	
		
//		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path("testjpg"), conf);
//		Text chensi = new Text();
//		jpg cs = new jpg();
//		FSDataOutputStream out = fs.create(new Path("chenji.jpeg"));
//		while(reader.next(chensi, cs)){
//			out.write(cs.getbyte());
//		}
//		out.close();
	}
}
