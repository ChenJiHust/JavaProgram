package cj.test.jpg;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class jpg implements Writable {
	private byte buffer[];
	private int bytes;
	public jpg(byte buffer[]) throws IOException {
		// TODO Auto-generated constructor stub
		this.buffer = buffer;
		this.bytes = buffer.length;
	}
	public jpg() {
		this.buffer = null;
		this.bytes = 0;
		// TODO Auto-generated constructor stub
	}
	public byte[] getbyte() {
		return buffer;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		bytes = in.readInt();
		buffer = new byte[bytes];
		int num = 0;		
		while(num < bytes){
			buffer[num] = in.readByte();
			num++;
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		out.writeInt(bytes);
		out.write(buffer);		
	}

}
