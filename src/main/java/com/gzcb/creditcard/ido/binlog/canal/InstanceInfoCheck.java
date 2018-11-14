package com.gzcb.creditcard.ido.binlog.canal;

import com.gzcb.creditcard.ido.binlog.utils.PropertiesUtil;
import com.gzcb.creditcard.ido.binlog.utils.ZkUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Canal Instance 信息校验类
 * @author lwj
 * @date 2018/7/5
 */
public class InstanceInfoCheck {
    private static PropertiesUtil pps = new PropertiesUtil("/canal.properties");
    /**
     * 添加instance时，对instance的信息进行校验
     * @param instance
     * @param mysqlConnect
     * @param slaveId
     * @param kafkaTopic
     * @return 合法则返回 true
     */
    public static boolean createInstanceCheck(String instance, String mysqlConnect, String slaveId, String kafkaTopic){
        Connection connection = null;
        try {
            /**
             * 对mysql的连接进行校验
             */
            if (StringUtils.isEmpty(mysqlConnect)){
                System.out.println(instance + "对应的mysql:" + mysqlConnect + "为空");
                return false;
            }
            String[] mysqlIpAndPort = mysqlConnect.split(":");
            if (mysqlIpAndPort.length != 2){
                System.out.println(instance + "对应的mysql:" + mysqlConnect + "格式有问题");
                return false;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + mysqlConnect, "canal", "canal");
            //判断是否开启了binlog
            ResultSet resultSet = connection.prepareStatement("SHOW VARIABLES LIKE 'log_bin'").executeQuery();
            if (!(resultSet.next() && "ON".equals(resultSet.getString(2).toUpperCase()))){
                System.out.println(instance + "对应的mysql:" + mysqlConnect + "没有开启binlog");
                return false;
            }
            //判断是否为 ROW 模式
            resultSet = connection.prepareStatement("SHOW VARIABLES LIKE 'binlog_format'").executeQuery();
            if (!(resultSet.next() && "ROW".equals(resultSet.getString(2).toUpperCase()))){
                System.out.println(instance + "对应的mysql:" + mysqlConnect + "开启的binlog非ROW模式");
                return false;
            }
            //判断 slaveId 是否跟 master 的一样
            resultSet = connection.prepareStatement("SHOW VARIABLES LIKE 'server_id'").executeQuery();
            if (!(resultSet.next() && !slaveId.equals(resultSet.getString(2).toUpperCase()))){
                System.out.println(instance + "对应的mysql:" + mysqlConnect + "的slaveId和master的一样");
                return false;
            }

            /**
             * 判断是否已经有正在运行的instance存在
             */
            if (StringUtils.isEmpty(instance)){
                System.out.println("instance:"+ instance +"为空");
                return false;
            }
            String zkInstancePath = pps.getString("canal.destinations.zk.path");
            List<String> zkRunInstances = ZkUtil.getChildren(zkInstancePath);
            if (zkRunInstances.contains(instance)){
                String instancePath = zkInstancePath + "/" + instance;
                if (ZkUtil.getChildren(instancePath).contains("running")){
                    System.out.println("instance:"+ instance +"已经在运行中");
                    return false;
                }
            }

            /**
             * 判断topic是否存在，并将topic信息保存到zk中
             * 查看之前是否已经有运行过的现在没有运行的instance
             * 如果有，查看当前传递过来的topic是否存在，如果不存在，报错
             * 如果没有，查看是否topic已经存在，如果存在，报错
             */
            if (StringUtils.isEmpty(kafkaTopic)){
                System.out.println(instance + "对应的kafkaTopic:" + kafkaTopic + "为空");
                return false;
            }
            List<String> zkInstanceChildren = ZkUtil.getChildren(zkInstancePath);
            String zkTopicPath = new PropertiesUtil("/kafka.properties").getString("kafka.topic.zk.path");
            //topic列表
            List<String> zkTopics = ZkUtil.getChildren(zkTopicPath);
            //topic是否存在
            if (!zkTopics.contains(kafkaTopic)){
                System.out.println(instance + "对应的kafkaTopic:" + kafkaTopic + "不存在");
                return false;
            }
        } catch (Exception e) {
            System.out.println(instance + "#" + mysqlConnect + "#" + slaveId + "#" + kafkaTopic + "异常");
            e.printStackTrace();
            return false;
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * 删除instance
     * @param instance
     * @return
     */
    public static boolean deleteInstance(String instance){
        try {
            if (StringUtils.isEmpty(instance)){
                return false;
            }
            String zkInstanceToKafkaPath = pps.getString("canal.info.zk.path");
            if (!ZkUtil.exists(zkInstanceToKafkaPath)){
                return false;
            }
            String zkRunInstancePath = zkInstanceToKafkaPath + "/" + instance;
            if (!ZkUtil.exists(zkRunInstancePath)){
                return false;
            }
            ZkUtil.deleteNode(zkRunInstancePath);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}