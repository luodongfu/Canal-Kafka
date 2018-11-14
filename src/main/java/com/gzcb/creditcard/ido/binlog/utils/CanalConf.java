package com.gzcb.creditcard.ido.binlog.utils;

/**
 * Canal配置类
 * @author lwj
 * @date 2018/11/14
 */
public class CanalConf {
    private static PropertiesUtil prop = new PropertiesUtil("/canal.properties");
    public static final String ZK_SERVERS = prop.getString("canal.zk.servers");
    public static final String DESTINATIONS_ZK_PATH = prop.getString("canal.destinations.zk.path");
    public static final String INFO_ZK_PATH = prop.getString("canal.info.zk.path");
    public static final String DEFAULT_FILTER_REGEX = prop.getString("canal.filterRegex.default");
    public static final String TOPIC = prop.getString("canal.info.topic");
    public static final String FILTER_REGEX = prop.getString("canal.info.filter");
}