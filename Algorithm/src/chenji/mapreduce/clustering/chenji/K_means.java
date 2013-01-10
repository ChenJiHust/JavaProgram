package chenji.mapreduce.clustering.chenji;



import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class K_means extends Configured implements Tool{

	
	
	private Path inputDir = new Path("testdata");
	private Path input = new Path(inputDir,"testdata");
	private Path output = new Path("output");
	private Path seed = new Path(inputDir,"seed");
	private int K = 5;
	
	/**
	 * @param args
	 * @throws Exception 
	 */	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int res = ToolRunner.run(new Configuration(), new K_means(), args);
		System.exit(res);
	}
	
	public static class Point implements Writable{
		
		private final int DIMENTION=60;
		private double arr[];
		
		public Point(){
			arr = new double[DIMENTION];
		}
		
		public Point(double [] arr){
			this.arr = new double[DIMENTION];
			for(int i = 0;i<arr.length;i++)
				this.arr[i] = arr[i];
		}
		
		public Point(String [] arr){
			this.arr = new double[DIMENTION];
			for (int i =0;i < arr.length;i++)
				this.arr[i] = Double.parseDouble(arr[i]);
		}

		public double getDist(Point another){
			double dist = 0;
			for(int i = 0;i<this.DIMENTION;i++){
				dist += (this.arr[i]-another.arr[i])*(this.arr[i]-another.arr[i]);
			}			
			return dist;
		}
		
		public String toString(){
			String str = new String();
			for(int i = 0;i< DIMENTION;i++)
				str += String.valueOf(arr[i]) + "\t";
			return str;			
		}
		
		@Override
		public void readFields(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			String[] str = in.readUTF().split("\\s+");
			for(int i = 0;i<DIMENTION;i++)
				this.arr[i] = Double.parseDouble(str[i]);
		}

		@Override
		public void write(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			out.writeUTF(this.toString());
		}
		
	}

	public static class k_map extends Mapper<LongWritable,Text,Text,Point>{
		private ArrayList<Point> seedarray = new ArrayList<Point>();
		public void setup(Context context) throws IOException, InterruptedException {
			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);
			FSDataInputStream  in = hdfs.open(new Path("testdata/seed"));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str = null;
			while((str = br.readLine()) != null){
				String[] d = str.split("\\s+");
				seedarray.add(new Point(d));
			}
			in.close();
			br.close();
			// NOTHING
		}
		public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
			String [] d = value.toString().split("\\s+");
			Point x = new Point(d);
			double mindist = Double.MAX_VALUE;
			int index = 0;
			for(int i = 0;i < seedarray.size();i++){
				double dist = x.getDist(seedarray.get(i));
				if(mindist < dist){
					mindist = dist;
					index = i;
				}
			}
			context.write(new Text(String.valueOf(index)), x);
		}
	}
	
	public static class k_reduce extends Reducer<Text,Point,NullWritable,Point>{
		public void reduce(Text key,Iterable<Point> values,Context context) throws IOException, InterruptedException{

			int sum = 0;
			double[] arr = new double[60];
			NullWritable a = null;
			for(int i = 0;i<arr.length;i++){
				arr[i] = 0.0;
			}
			for(Point val:values){
				for(int i = 0;i<arr.length;i++){
					arr[i] += val.arr[i];
				}
				sum++;
			}
			for(int i = 0 ;i<arr.length;i++){
				arr[i] = arr[i]/sum;
			}
			Point x = new Point(arr);
			context.write(a, x);
		}
	}
	
	public void generateSeed() throws IOException{
		Configuration conf = getConf();
		FileSystem hdfs = FileSystem.get(conf);
		FSDataOutputStream out = hdfs.create(seed, true);
		FSDataInputStream  in = hdfs.open(input);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str = null;
		int k = 0;
		while((str = br.readLine()) != null){
			out.writeBytes(str + "\n");
			k++;
			if(k > K)
				break;
		}
		in.close();
		out.close();
		br.close();
	}
	
	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		Job job = new Job(conf);
		
		FileInputFormat.setInputPaths(job, input);
		FileOutputFormat.setOutputPath(job,output);
		job.setJobName("K_Means");
		job.setJarByClass(K_means.class);
		job.setMapperClass(k_map.class);
		job.setReducerClass(k_reduce.class);
//		job.setCombinerClass(k_reduce.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Point.class);
		
		job.setMapOutputValueClass(Point.class);
		job.setMapOutputKeyClass(Text.class);
		generateSeed();
		int res = job.waitForCompletion(true)?0:1;
		return res;
	}

	
}

