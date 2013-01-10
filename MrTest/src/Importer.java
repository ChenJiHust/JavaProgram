import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.NullOutputFormat;

import org.apache.hadoop.util.*;

public class Importer extends Configured implements Tool {

	@SuppressWarnings({ "deprecation", "deprecation" })
	public static class Map extends MapReduceBase implements
			Mapper<Text, Text, Text, IntWritable> {
		private HTable table;
		public void map(Text key, Text value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			byte[] row1 = Bytes.toBytes(key.toString());
			Put p1 = new Put(row1);
			byte[] databytes = Bytes.toBytes("data");
			p1.add(databytes, Bytes.toBytes(value.toString()), Bytes
					.toBytes(value.toString()));
			table.put(p1);

		}

		public void configure(JobConf job) {
			super.configure(job);
			try {
				this.table = new HTable(new HBaseConfiguration(), "CITED");
			} catch (IOException e) {
				throw new RuntimeException("failed htable construction", e);
			}

		}

		public void close() throws IOException {
			super.close();
			table.close();
		}
	}

	@SuppressWarnings("deprecation")
	public int run(String[] args) throws Exception {

		JobConf conf = new JobConf(Importer.class);
		conf.setJobName("TmporterToHBase");

		conf.setMapperClass(Map.class);
		conf.setNumReduceTasks(0);

		conf.setInputFormat(KeyValueTextInputFormat.class);
		conf.setOutputFormat(NullOutputFormat.class);
		conf.set("key.value.separator.in.input.line", ",");

		FileInputFormat.setInputPaths(conf, new Path(
				"/user/hadoop/cite75_99.txt"));

		JobClient.runJob(conf);
		return 0;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		try {
			HBaseConfiguration config = new HBaseConfiguration();
			HBaseAdmin admin = new HBaseAdmin(config);
			HTableDescriptor htd = new HTableDescriptor("CITED");
			HColumnDescriptor hcd = new HColumnDescriptor("data");
			htd.addFamily(hcd);
			admin.createTable(htd);
			byte[] tablename = htd.getName();
			HTableDescriptor[] tables = admin.listTables();

			if (tables.length != 1
					&& Bytes.equals(tablename, tables[0].getName())) {
				throw new IOException("Failed create of table");
			}

		} catch (Exception e) {

		}
		int res = ToolRunner.run(new Configuration(), new Importer(), args);
		System.exit(res);
	}
}