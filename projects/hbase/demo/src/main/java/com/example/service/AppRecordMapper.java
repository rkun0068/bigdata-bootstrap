package com.example.service;

import org.apache.hadoop.hbase.util.MapReduceExtendedCell;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import java.lang.reflect.Field;
import com.example.entity.AppRecord;
import com.example.tools.RowKeyUtil;
// https://hadoop.apache.org/docs/r2.8.2/api/org/apache/hadoop/mapreduce/Mapper.html
public class AppRecordMapper extends Mapper<LongWritable, Text,ImmutableBytesWritable, MapReduceExtendedCell> {
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        AppRecord appRecord = AppRecord.parse(value.toString());
        String rowKey = RowKeyUtil.generateRowKey(appRecord.getApp_id(), appRecord.getApp_name());
        byte[]cf = Bytes.toBytes("INFO");
        Class<?> appRecordClass = appRecord.getClass();
        Field[]fields = appRecordClass.getDeclaredFields();
        ImmutableBytesWritable rowKeyWritable = new ImmutableBytesWritable(Bytes.toBytes(rowKey));
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();
                if (fieldName.equals("app_id")) {
                    continue;
                }
                String fieldValue = field.get(appRecord).toString();
                KeyValue kv = new KeyValue(Bytes.toBytes(rowKey), cf, Bytes.toBytes(fieldName), Bytes.toBytes(fieldValue));
                context.write(rowKeyWritable, new MapReduceExtendedCell(kv));
            }catch(IllegalAccessException e) {
                e.printStackTrace();
            }

        }
       



        
        
        

    }

}