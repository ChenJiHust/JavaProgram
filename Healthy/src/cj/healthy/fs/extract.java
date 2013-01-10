package cj.healthy.fs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class extract {

	/**
	 * 从合并文件提取还原
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		FileSystem hdfs = FileSystem.get(conf);
		Path inputPath = new Path("XMLSample");
		Path outputPath = new Path("direct");
		byte[] buffer = new byte[2560];
		int bytesRead = 0;
		long oldtime = System.currentTimeMillis();
		String name = null;
		for (int i = 0; i < 3; i++) {
			name = String.valueOf(i) + ".xml";
			Path input = new Path(inputPath, name);
			FSDataInputStream in = hdfs.open(input);
			Path output = new Path(outputPath, name);
			FSDataOutputStream out = hdfs.create(output);
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
		}
		System.out.println("直接读取耗时" + (System.currentTimeMillis() - oldtime)
				/ 1000 + "秒");

		oldtime = System.currentTimeMillis();

		for (int i = 0; i < 3; i++) {
			name = String.valueOf(i) + ".xml";
			extract ex = new extract();
			outputPath = new Path("indirect");
			Path output = new Path(outputPath, name);
			FSDataOutputStream out = hdfs.create(output);
			if ((buffer = ex.extractFile(name)) == null)
				System.out.println(name + "没有这个文件");
			out.write(buffer);
			out.close();
		}
		System.out.println("间接读取耗时" + (System.currentTimeMillis() - oldtime)
				/ 1000 + "秒");

	}

	private Path mergerPath;
	private Path indexPath;

	public extract() throws IOException {
		mergerPath = new Path("healthy/merger");
		indexPath = new Path("healthy/index");
	}

	public extract(Path mergerPath, Path indexPath) throws IOException {
		if (mergerPath == null)
			mergerPath = new Path("healthy/merger");
		if (indexPath == null)
			indexPath = new Path("healthy/index");
		this.mergerPath = mergerPath;
		this.indexPath = indexPath;
	}

	public byte[] extractFile(String fileName) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		FSDataInputStream merger = fs.open(mergerPath);
		FSDataInputStream index = fs.open(indexPath);
		byte[] buffer = null;
		index.seek(0);
		BufferedReader br = new BufferedReader(new InputStreamReader(index));
		String str = null;
		while ((str = br.readLine()) != null) {
			String[] splitStrings = str.split(" ");
			if (!splitStrings[0].equals(fileName)) {
				continue;
			} else {
				int offset = Integer.parseInt(splitStrings[1]);
				int length = Integer.parseInt(splitStrings[2]) - offset;
				buffer = new byte[length];
				merger.readFully(offset, buffer);
				break;
			}
		}
		merger.close();
		index.close();
		return buffer;
	}

	public void extractAllFile(Path output) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		FSDataInputStream merger = fs.open(mergerPath);
		FSDataInputStream index = fs.open(indexPath);
		index.seek(0);
		BufferedReader br = new BufferedReader(new InputStreamReader(index));
		String str = null;
		if (output == null)
			output = new Path("extractXML");
		while ((str = br.readLine()) != null) {
			String[] splitStrings = str.split(" ");
			Path outputPath = new Path(output, splitStrings[0].toString());
			FSDataOutputStream outputStream = fs.create(outputPath);
			int offset = Integer.parseInt(splitStrings[1]);
			int length = Integer.parseInt(splitStrings[2]) - offset;
			byte[] buffer = new byte[length];
			merger.readFully(offset, buffer);
			outputStream.write(buffer, 0, length);
			outputStream.close();
		}
		merger.close();
		index.close();
	}
}
