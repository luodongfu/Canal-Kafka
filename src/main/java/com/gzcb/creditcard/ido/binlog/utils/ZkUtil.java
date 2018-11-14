package com.gzcb.creditcard.ido.binlog.utils;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 针对没有注册监听的zk node操作工具类
 * @author lwj
 * @date 2018/2/24
 */
public class ZkUtil {
    private static ZooKeeper zk = null;
    static {
        try {
            PropertiesUtil pps = new PropertiesUtil("/canal.properties");
            String zkList = pps.getString("canal.zk.servers");
            zk = new ZooKeeper(zkList, 2000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定目录下的子节点名称
     * @param path
     * @return
     * @throws Exception
     */
    public static List<String> getChildren(String path) throws Exception {
        return zk.getChildren(path, false);
    }

    /**
     * 判断该路径是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, false);
        return stat == null ? false:true;
    }

    /**
     * 循环创建节点
     * 注意：只是创建节点，值都为null
     * @param path
     * @return
     */
    public static void createNodes(String path) throws KeeperException, InterruptedException {
        String[] split = path.split("/");
        String tmp = "";
        for (int i = 1; i < split.length; i++) {
            tmp += "/" + split[i];
            if (!ZkUtil.exists(tmp)){
                ZkUtil.createNodeWithValue(tmp, null);
            }
        }
    }

    /**
     * 创建节点
     * @param path
     * @param value
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void createNodeWithValue(String path, String value) throws KeeperException, InterruptedException {

        zk.create(path, value==null ? null : value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 通过路径得到值
     * 如果该节点不存在，则返回 null
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public static String getData(String path) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        if (!ZkUtil.exists(path)){
            return null;
        }
        byte[] value = zk.getData(path, false, null);
        if (value!=null && value.length > 0){
            return new String(value, "utf-8");
        }
        return null;
    }

    /**
     * 删除节点
     * @param path
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void deleteNode(String path) throws KeeperException, InterruptedException {
        zk.delete(path, -1);
    }
}