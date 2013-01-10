package cj.healthy.fs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class generateSampleXML {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		FileSystem local = FileSystem.getLocal(conf);

		Path outputPath = new Path("XMLSample");
		if (!hdfs.exists(outputPath))
			hdfs.mkdirs(outputPath);

		Path inputPath = new Path(
				"D:\\西门子卫生项目\\CDA医疗文档xml\\SampleCDADocument.xml");
		FSDataInputStream inputStream = local.open(inputPath);
		long length = local.getFileStatus(inputPath).getLen();
		byte[] buffer = new byte[(int) length];
		inputStream.readFully(0, buffer);
		inputStream.close();

		long oldtime = System.currentTimeMillis();
		String name = null;
		for (int i = 0; i < 300; i++) {
			name = String.valueOf(i) + ".xml";
			Path output = new Path(outputPath, name);
			FSDataOutputStream outputStream = hdfs.create(output);
			outputStream.write(buffer);
			outputStream.close();
		}
		System.out.println("耗时" + (System.currentTimeMillis() - oldtime) / 1000
				+ "秒");
	}

}
