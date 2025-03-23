package com.example.service;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.HColumnDescriptor;
import java.io.IOException;
import org.apache.hadoop.hbase.util.Bytes;
public class TableService {
    private static final String TABLE_NAME = "GOOGLE_PLAY_STORE";
    private static final String COLUMN_FAMILY = "INFO";
    
    public static void createOrOverwrite(Admin admin, HTableDescriptor table, byte[][] splitKeys) throws IOException {
        if (admin.tableExists(table.getTableName())) {
          admin.disableTable(table.getTableName());
          admin.deleteTable(table.getTableName());
        }
        admin.createTable(table, splitKeys);
    }

    public static void create(Configuration conf) throws IOException {
        try (Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin()) {
            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
            table.addFamily(new HColumnDescriptor(COLUMN_FAMILY).setCompressionType(Compression.Algorithm.NONE));
            System.out.println("Creating table...");
            byte[][] splitKeys = { Bytes.toBytes("8") };
            createOrOverwrite(admin, table, splitKeys);
            System.out.println("Table created successfully");

        }
    }
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        create(conf);
    }


}
