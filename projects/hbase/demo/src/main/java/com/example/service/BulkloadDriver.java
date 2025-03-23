package com.example.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Connection;
import java.io.IOException;
import org.apache.hadoop.mapreduce.Job;
import com.example.service.AppRecordMapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.MapReduceExtendedCell;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;

public class BulkloadDriver {
   public static void main(String[] args) throws IOException ,ClassNotFoundException,InterruptedException{
      // Load the configuration
      Configuration conf = HBaseConfiguration.create();

      // Config a Job
      Job instance = Job.getInstance(conf);
      instance.setJarByClass(BulkloadDriver.class);
      instance.setMapperClass(AppRecordMapper.class);

      // COnfig input
      instance.setInputFormatClass(TextInputFormat.class);
      // the path of the input file
      FileInputFormat.setInputPaths(instance, new Path("hdfs://node01:8020/hbase/input/"));

      // Config output
      FileOutputFormat.setOutputPath(instance, new Path("hdfs://node01:8020/hbase/output/"));
      instance.setOutputKeyClass(ImmutableBytesWritable.class);
      instance.setOutputValueClass(MapReduceExtendedCell.class);

      // Config HfileoutputFormat2
      Connection connection = ConnectionFactory.createConnection(conf);
      Table table = connection.getTable(TableName.valueOf("GOOGLE_PLAY_STORE"));
      // Get RegionLocator
      RegionLocator regionLocator = connection.getRegionLocator(TableName.valueOf("GOOGLE_PLAY_STORE"));
      HFileOutputFormat2.configureIncrementalLoad(instance, table, regionLocator);

      // Run the job
      if (instance.waitForCompletion(true)) {
         System.out.println("Job completed successfully");
      } else {
         System.out.println("Job failed");
         System.exit(1);
      }
   } 
}
