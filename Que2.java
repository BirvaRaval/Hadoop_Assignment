import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Que2 {
	public class MapperJoin extends Mapper<LongWritable, Text, Text, IntWritable>{
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			String[] fields = value.toString().split(",");
			
			String year = fields[0];
			int temp = Integer.parseInt(fields[1]);
			
			context.write(new Text(year), new IntWritable(temp));
		}}


		public class ReducerJoin extends Reducer<Text, IntWritable, Text, IntWritable>{
			
			public void reduce(Text key, Iterable<IntWritable> value, Context context) throws IOException, InterruptedException {
				int mintemp = Integer.MAX_VALUE;
				
				for (IntWritable val: value) {
					mintemp = Math.min(mintemp, val.get());
				}
				
				context.write(key, new IntWritable(mintemp));
			}

		}
		
			public static void main(String[] args) throws Exception {
			
			Path input = new Path(args[0]);
			Path output = new Path(args[1]);
			
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf,"que-2");
			
			
			job.setJarByClass(Que2.class);
			MultipleInputs.addInputPath(job, input, TextInputFormat.class, MapperJoin.class);
			job.setReducerClass(ReducerJoin.class);
			
			
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			
			job.setOutputFormatClass(TextOutputFormat.class);
			TextOutputFormat.setOutputPath(job, output);
			
			System.exit(job.waitForCompletion(true)?0:1);
			
		
		}	
	
	}
