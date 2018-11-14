package com.gzcb.creditcard.ido.binlog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * canal client的工具类
 * @author lwj
 * @date 2018/11/14
 */
public class CanalUtil {
    private static Logger log = LoggerFactory.getLogger(CanalUtil.class);

    /**
     * 通过instance得到zookeeper中配置的对应的信息
     * @param instance
     * @param type 比如topic、filterRegex等...
     * @return 如果不存在返回null
     */
    public static String getInstanceInfoByType(String instance, String type){
        String data;
        try {
            data = ZkUtil.getData(getFullZkPath(CanalConf.INFO_ZK_PATH, instance, type));
        } catch (Exception e) {
            log.error("通过instance名字获取对应的topic失败", e);
            return null;
        }
        return data;
    }

    /**
     * 拼接zk path
     * @param parentPath
     * @param instanceName
     * @param type
     * @return
     */
    private static String getFullZkPath(String parentPath, String instanceName, String type){
        return parentPath + "/" + instanceName + "/" + type;
    }
}