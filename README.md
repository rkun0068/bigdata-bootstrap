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
- `hadoop-env.sh` 
- [Configuring Environment of Hadoop Daemons](https://hadoop.apache.org/docs/r3.4.1/hadoop-project-dist/hadoop-common/ClusterSetup.html#Configuring_Environment_of_Hadoop_Daemons)



