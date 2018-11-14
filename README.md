# Canal Server Message解析程序
***
该程序用于将Canal Server获取到的Message信息解析成json格式的Binlog报文，并将报文发送到Kafka消息队列中。
## 功能简介
- 无状态，可任意拓展，无需负载均衡器的支持
- 解析Canal Server Message对象
- 生产报文入指定Kafka集群，供下游系统消费


## 编译
- mvn clean install
- 生成canal-client-X.X.X.tar.gz

## 使用
- 解压tar.gz文件
- 修改conf/下配置文件
- 执行bin/start.sh

## 注意
- 在新增instance之前，需要手动到zookeeper配置该instance的相关信息
比如将解析的binlog信息发送到哪个topic，所以也就需要配置topic信息
详情可以查看 canal.properties 文件的描述。