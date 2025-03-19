# bigdata-bootstrap
基于三台Rocky8.6服务器部署
## <img src="https://icon.icepanel.io/Technology/svg/Apache-Hadoop.svg" width = "50" height = "50">HDFS
| **HDFS** | **分布式存储** |
| --- | --- |
| 主角色 | NameNode |
| 从角色 | DataNode |
| 主角色辅助角色 | SecondaryNameNode |


| **YARN** | **资源管理调度** |
| --- | --- |
| 主角色 | ResoureManager |
| 从角色 | NodeManager |

- 安装版本：[3.4.1](https://hadoop.apache.org/releases.html) 
- 所有组件安装路径为`/opt/<component-name>`
- [Hadoop分布式文件系统：架构和设计](https://hadoop.apache.org/docs/r1.0.4/cn/hdfs_design.html)
- openjdk version 1.8.0_252
- 集群规划

| **Host** | **Role** |
| --- | --- |
| node01 | NN DN RM NM |
| node02 | SNN DN NM |
| node03 | DN NM |

### 安装
创建`ansible/host.ini`
```
[all]
node01
node02
node03
```
```
# 进入ansible目录,安装二进制软件包
cd ansible
ansible-playbook main.yml -t hdfs_01_download
```

### 配置
 - [Configuring Environment of Hadoop Daemons ｜hadoop-env.sh](https://hadoop.apache.org/docs/r3.4.1/hadoop-project-dist/hadoop-common/ClusterSetup.html#Configuring_Environment_of_Hadoop_Daemons)

- [core-default.xml 参数](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/core-default.xml)

- [hdfs-site.xml 参数](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)
  - `dfs.datanode.data.dir` 基于不同机器规格进行配置,样例使用本地磁盘
- [mapred-site.xml 参数](https://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml)  
- [yarn-site.xml 参数](https://hadoop.apache.org/docs/r2.7.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)
- 最后配置环境变量,修改文件 `/etc/profile`

```
# JAVA
export JAVA_HOME=/usr/lib/jvm/java-1.8.0  # TODO：based on your linux env
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
# HADOOP
export HADOOP_HOME=/opt/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```
以上配置可通过ansible进行自动化
```
ansible-playbook main.yml -t <config file prefix>
# .e.g config hdfs-site.xml
ansible-playbook main.yml -t hdfs-site
```
### 启动
首次启动需要格式化
```
hadoop namenode -format

# 启动hdfs
start-dfs.sh
# 启动yarn
start-yarn.sh
# 访问dfs.namenode.http-address:9870(默认值)进入HDFS WebUI
```
