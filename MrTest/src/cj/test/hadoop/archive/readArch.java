package cj.test.hadoop.archive;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.HarFileSystem;
import org.apache.hadoop.fs.Path;

public class readArch {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		HarFileSystem harFileSystem = new HarFileSystem();
		harFileSystem.initialize(new URI("har:///user/hadoop/output/reuter.har"), new Configuration());
		FSDataInputStream inputStream = harFileSystem.open(new Path("upload/reuters21578/README.txt"));
		byte[] buffer = new byte[1024];
		while((inputStream.read(buffer)) > 0 ){
			System.out.println(buffer);
		}
		inputStream.close();
	}

}
