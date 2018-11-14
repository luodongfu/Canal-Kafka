package com.gzcb.creditcard.ido.binlog.canal;

import com.gzcb.creditcard.ido.binlog.utils.CanalConf;
import com.gzcb.creditcard.ido.binlog.utils.CanalUtil;
import com.gzcb.creditcard.ido.binlog.utils.ZkUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 初始化binlog的instance实例
 * @author lwj
 * @date 2018/2/22
 */
public class InitBinLog implements Runnable{
    private static Logger log = LoggerFactory.getLogger(InitBinLog.class);
    private ZooKeeper zk = null;
    private HashMap<String, CanalClient> instanceThreadMap = new HashMap();
    /**
     * 该Map对应 zk 中节点的状态
     * String: 在canalPath下存在的instance名称
     * Boolean： 是否有线程在启动，启动必须满足两个条件
     *      （1）instance必须存在
     *      （2）instance中有running子节点
     */
    private HashMap<String, Boolean> instanceStateMap = new HashMap<>();
    public void initCanalClients(){
        /**
         * 注册监听
         */
        try {
            zk = new ZooKeeper(CanalConf.ZK_SERVERS, 2000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                        try {
                            /**
                             * 判断是否是instance信息发生变动，一般是instance启动或者停止
                             */
                            String[] pathSplit = watchedEvent.getPath().split("/");
                            String instance = pathSplit[pathSplit.length - 1];
                            //已存在的instance节点内容发生变化
                            if (instanceStateMap.containsKey(instance)){
                                instanceChange(instance, watchedEvent.getPath());
                            }
                            /**
                             * 有新的instance +  注册监听
                             */
                             startCanalClient();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            startCanalClient();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * instance信息发生变动
     * 一般是删除了instance
     * @param instance
     * @param eventPath
     * @throws Exception
     */
    private void instanceChange(String instance, String eventPath) throws Exception {
        log.info("## eventPath===>{}", eventPath);
        List<String> instanceChildrensList = getNodesByPath(eventPath);
        //没有在运行
        if (!instanceChildrensList.contains("running") && instanceStateMap.get(instance)){
            instanceThreadMap.get(instance).setStop(true);
            instanceStateMap.put(instance, false);
            instanceThreadMap.remove(instance);
        }
        //启动 添加了，但是还没有运行 的 instance
        if (instanceChildrensList.contains("running") && !instanceStateMap.containsKey(instance)){
            runInstance(instance);
        }
    }

    /**
     * 监听回调的函数，监控instance client
     */
    private void startCanalClient() throws Exception {
        List<String> instances = getNodesByPath(CanalConf.DESTINATIONS_ZK_PATH);
        if (instances != null && instances.size() > 0){
            for (String instance : instances){
                if (!instanceStateMap.containsKey(instance)){
                    instanceStateMap.put(instance, false);
                }
                //对每个instance的子节点进行监听
                List<String> instanceChildrens = getNodesByPath(CanalConf.DESTINATIONS_ZK_PATH + "/" + instance);
                //新增的instance
                if (instanceChildrens.contains("running") && !instanceStateMap.get(instance)){
                    runInstance(instance);
                }
            }
        }
        printMap(instanceStateMap);
        printMap(instanceThreadMap);
    }

    /**
     * 启动instance
     * @param instance
     * @throws InterruptedException
     */
    private void runInstance(String instance) throws Exception {
        if (StringUtils.isEmpty(ZkUtil.getData(CanalConf.INFO_ZK_PATH + "/" + instance))){
            log.error("destination存在，但是{}不存在该instance，instance=>{}", CanalConf.INFO_ZK_PATH, instance);
            return;
        }

        String topic = CanalUtil.getInstanceInfoByType(instance, CanalConf.TOPIC);
        if (StringUtils.isEmpty(topic)){
            log.error("destination=>{}找不到对应的topic", instance);
            return;
        }

        String filterRegex = CanalUtil.getInstanceInfoByType(instance, CanalConf.FILTER_REGEX);
        if (StringUtils.isEmpty(filterRegex)){
            filterRegex = CanalConf.DEFAULT_FILTER_REGEX;
        }

        CanalClient canalClient = new CanalClient(instance, topic, CanalConf.ZK_SERVERS, filterRegex);
        canalClient.setDaemon(true);
        canalClient.start();
        instanceThreadMap.put(instance, canalClient);
        instanceStateMap.put(instance, true);
    }

    /**
     * 通过路径得到子节点，并监听对路径
     * @param zkPath
     * @return
     * @throws Exception
     */
    private List<String> getNodesByPath(String zkPath) throws Exception {
        return zk.getChildren(zkPath, true);
    }

    @Override
    public void run() {
        initCanalClients();
    }

    /**
     * 打印Map
     * @param map
     */
    private void printMap(HashMap map) {
        log.info("## ===>println map info begin");
        Set<String> kyes = map.keySet();
        for (String key : kyes) {
            log.info(key + ":" + map.get(key));
        }
        log.info("## ===>println map info end");
    }
}