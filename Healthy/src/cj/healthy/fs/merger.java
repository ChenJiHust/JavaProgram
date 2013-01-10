package cj.healthy.fs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class merger {

	/**
	 * 合并测试
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		long oldtime = System.currentTimeMillis();
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		Path inputPath = new Path("XMLSample");
		Path mergerFile = new Path("healthy/merger");
		Path indexFile = new Path("healthy/index");

		FSDataOutputStream merger = hdfs.create(mergerFile);
		FSDataOutputStream index = hdfs.create(indexFile);
		FileStatus[] inputFiles = hdfs.listStatus(inputPath);
		byte[] buffer = new byte[256];
		int bytesRead = 0;
		int sumByte = 0;
		int sum = 0;
		for (int i = 0; i < inputFiles.length; i++) {
			sumByte = 0;
			bytesRead = 0;
			FSDataInputStream in = hdfs.open(inputFiles[i].getPath());
			while ((bytesRead = in.read(buffer)) > 0) {
				merger.write(buffer, 0, bytesRead);
				sumByte += bytesRead;
			}
			String str = inputFiles[i].getPath().getName() + " "
					+ String.valueOf(sum) + " " + String.valueOf(sum + sumByte)
					+ "\n";
			index.writeBytes(str);
			sum += sumByte;
			in.close();
		}
		merger.close();
		index.close();

		System.out.println("耗时" + (System.currentTimeMillis() - oldtime) / 1000
				+ "秒");
	}

}
