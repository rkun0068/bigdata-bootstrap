<img src="https://raw.githubusercontent.com/apache/hbase/master/src/site/resources/images/hbase_logo_with_orca_large.png" width = "150" height = "50">

- 基于本项目Ansible构建的Hbase集群
- 表数据集来源 [Google Play Store Apps](https://www.kaggle.com/datasets/gauthamp10/google-playstore-apps/data)

## Apache HBase APIs
- [Examples](https://hbase.apache.org/book.html#_examples)

### 创建表
创建一张名为`GOOGLE_PLAY_STORE`的表，列族为`INFO`
- [TableService.java](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/hbase/demo/src/main/java/com/example/service/TableService.java)

### [Bulk load](https://hbase.apache.org/book.html#arch.bulk.load)
- 数据集获取
```
git clone https://github.com/gauthamp10/Google-Playstore-Dataset.git
cd Google-Playstore-Dataset/dataset/
for f in *.tar.gz; do tar -xvf "$f"; done
cat Part?.csv > Googple-Playstore-Dataset.csv
sed -i '1d' Googple-Playstore-Dataset.csv
# 上传文件到hdfs
hadoop fs -put Googple-Playstore-Dataset.csv /hbase/input/
```
- [Hadoop Shell命令](https://hadoop.apache.org/docs/r1.0.4/cn/hdfs_shell.html)
- 通过MapRduce准备StoreFiles
  - [BulkloadDriver.java](https://github.com/rkun0068/bigdata-bootstrap/tree/main/projects/hbase/demo/src/main/java/com/example/service/BulkloadDriver.java)
  - [StoreFile (HFile)](https://hbase.apache.org/book.html#hfile)

- [加载数据文件到HBase](https://hbase.apache.org/book.html#completebulkload)

```
hbase org.apache.hadoop.hbase.tool.LoadIncrementalHFiles /hbase/output/ GOOGLE_PLAY_STORE
```

## Mechanism
### Read
- 从zookeeper找到[meta](https://hbase.apache.org/book.html#arch.catalog.meta)表的region的位置,然后读取meta表中的数据。而meta中又存储了用户表的region信息
  - [zookeeperCLI](https://zookeeper.apache.org/doc/r3.9.3/zookeeperCLI.html)
```
bin/zkCli.sh
[zk: localhost:2181(CONNECTED) 6] ls /hbase
[backup-masters, draining, flush-table-proc, hbaseid, master, master-maintenance, meta-region-server, namespace, online-snapshot, rs, running, splitWAL, table]

```
`/hbase/meta-region-server`,该节点保存了meta表的region server数据
```
hbase:003:0> scan 'hbase:meta', {FILTER => "PrefixFilter('GOOGLE_PLAY_STORE')"}  
```
- 找到对应的regionserver，查找对应的region
- 从MemStore找数据，再去BlockCache中找，如果没有，再到StoreFile上读
- [Write/Read](https://hbase.apache.org/book.html#regionserver.offheap.overview)

### Write
HBase 的数据存储过程与 [LSM（Log-Structured Merge-Tree）](https://www.scylladb.com/glossary/log-structured-merge-tree/) 结构对应，主要包括以下几个阶段：

1. 写入 MemStore（内存）
数据先写入 MemStore（即 HBase 的内存存储结构），并同时记录到 WAL（Write-Ahead Log） 以保证数据安全性。

HBase 2.0 之后，MemStore 也会进行 Compaction（合并） 以优化内存使用。

2. MemStore 刷写（Flush）到 HDFS
当 MemStore 达到阈值（默认 128MB），触发 Flush 机制。

MemStore 数据会持久化 到 HDFS，形成 StoreFile（HFile），然后清空 MemStore。

3. StoreFile 合并（Compaction）
Minor Compaction：合并多个小的 StoreFile 以减少存储碎片，提高查询效率。

Major Compaction：定期合并所有 StoreFile，进一步优化存储结构。

#### HBase 数据写入流程
- Client 访问 ZooKeeper，从 ZK 中找到 META 表 的 Region 位置。

- 读取 META 表，根据 namespace、表名、rowkey 获取对应 Region 信息。

- 通过 Region 信息，找到数据存储的 RegionServer。

- 向 RegionServer 发送写请求，找到对应的 Region 和 列族。

- 先将数据写入 MemStore，同时写入 WAL（保证数据持久化）。

- 当 MemStore 满时，数据会 Flush 到 HDFS，形成 StoreFile。