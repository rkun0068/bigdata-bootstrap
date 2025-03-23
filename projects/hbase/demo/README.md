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
