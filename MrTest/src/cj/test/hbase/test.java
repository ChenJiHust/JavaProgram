package cj.test.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class test {
	/**
	 * @param args
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HBaseConfiguration hbaseConf = null;
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "ngn76");
		conf.set("hbase.zookeeper.property.clientPort", "9102");
		hbaseConf = new HBaseConfiguration(conf);

		String tableName = "chenji";
		HBaseAdmin admin = new HBaseAdmin(hbaseConf);
		if (!admin.tableExists(tableName)) {
			// 若表不存在
			System.out.println("creating table: " + tableName);

			HTableDescriptor desc = new HTableDescriptor(tableName);
			desc.addFamily(new HColumnDescriptor("req"));
			desc.addFamily(new HColumnDescriptor("http"));
			admin.createTable(desc);
			
		} else {
			System.out.println("table " + tableName + "already exists!");
		}

	}

}
