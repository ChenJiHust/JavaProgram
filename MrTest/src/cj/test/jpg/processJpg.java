package cj.test.jpg;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class processJpg extends Configured implements Tool{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
//		Configuration conf = new Configuration();
//		FileSystem fs = FileSystem.get(conf);
//		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path("chensioutput/part-r-00000"), conf);
//		Text chensi = new Text();
//		IntWritable cs = new IntWritable();
//		while(reader.next(chensi, cs)){
//			System.out.println(chensi.toString() + " num " + String.valueOf(cs.get()));
//		}
//		reader.close();
		
		System.exit(ToolRunner.run(new Configuration(),new processJpg(), args));
	}

	public static class inputtest extends  SequenceFileInputFormat<Text, jpg>{
		
	}
	public static class outputtest extends SequenceFileOutputFormat<Text,Text>{
		
	}
	
	public static class chenjimap extends Mapper<Text, jpg, Text, IntWritable> {
		public void map(Text key, jpg value, Context context)
				throws IOException, InterruptedException {
			context.write(key, new IntWritable(1));
		}
	}

	public static class chenjireduce extends Reducer<Text, IntWritable, NullWritable, Text> {
		public void reduce(Text key,Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			NullWritable nullWritable = NullWritable.get();
			for (IntWritable value : values) {
				sum += value.get();
			}
			context.write(nullWritable, new Text(String.valueOf(sum)));
		}
	}
	
	@Override
	public int run(String[] args) throws Exception {		
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		if(conf == null){
			conf = new Configuration();
		}
		Job job = new Job(conf);
		job.setJobName("processJpg");
		job.setJarByClass(processJpg.class);
		
		FileInputFormat.setInputPaths(job, new Path("testjpg"));
		FileOutputFormat.setOutputPath(job, new Path("chensioutput"));
		
		job.setInputFormatClass(inputtest.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
				
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setMapperClass(chenjimap.class);
		job.setReducerClass(chenjireduce.class);
		
		return job.waitForCompletion(true)?0:1;
	}
	
	

}
