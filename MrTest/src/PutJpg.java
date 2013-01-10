import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class PutJpg {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

		byte buffer[] = Bytes.toBytes("chenji");
		HTable table = new HTable(new HBaseConfiguration(), "chenjitest");
		byte[] row1 = Bytes.toBytes("rowtest");
		Put p1 = new Put(row1);
		byte[] databytes = Bytes.toBytes("data");
		p1.add(databytes, Bytes.toBytes("jpg"), buffer);
		table.put(p1);		
		table.close();
	}

}
