package com.gzcb.creditcard.ido.binlog.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.gzcb.creditcard.ido.binlog.bean.BinLogBean;
import com.gzcb.creditcard.ido.binlog.bean.ColumnField;
import com.gzcb.creditcard.ido.binlog.utils.DateUtil;
import com.gzcb.creditcard.ido.binlog.utils.KafkaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Canal处理消息客户端
 * @author lwj
 * @date 2018/2/5
 */
public class CanalClient extends Thread{
    private static Logger log = LoggerFactory.getLogger(CanalClient.class);
    /**
     * 线程控制
     */
    private volatile boolean stop = false;
    /**
     * canal中对应的instance的名字
     */
    private String instance;
    private String topic;
    private String canalZk;
    private String filterRegex;

    public CanalClient() {
    }
    public CanalClient(String instance, String topic, String canalZk, String filterRegex) {
        this.instance = instance;
        this.topic = topic;
        this.canalZk = canalZk;
        this.filterRegex = filterRegex;
    }

    @Override
    public void run() {
        collect(instance, topic, canalZk, filterRegex);
    }

    /**
     * 将数据生产到Kafka
     * @param instance
     */
    public void collect(String instance, String topic, String canalZk, String filterRegex){
        // 创建链接(HA):基于Zookeeper的集群模式
        CanalConnector connector = CanalConnectors.newClusterConnector(canalZk, instance, "", "");
        int batchSize = 2000;
        try {
            connector.connect();
            //指定监听库表的规则
            connector.subscribe(filterRegex);
            connector.rollback();
            while (!isStop()) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("sleep失败", e);
                    }
                } else {
                    try {
                        parseEntry(message.getEntries(), topic);
                    } catch (InvalidProtocolBufferException e) {
                        //处理失败, 回滚数据
                        connector.rollback(batchId);
                        log.error("处理数据失败，回滚，batchId={}", batchId, e);
                    }
                }
                // 提交确认
                connector.ack(batchId);
            }
        } finally {
            connector.disconnect();
        }
    }

    /**
     * 封装每次获取到的一批数据
     * @param entrys
     */
    private void parseEntry(List<Entry> entrys, String topic) throws InvalidProtocolBufferException {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChage = RowChange.parseFrom(entry.getStoreValue());

            EventType eventType = rowChage.getEventType();
            Header header = entry.getHeader();

            BinLogBean binLogBean = new BinLogBean();
            binLogBean.setInstance(instance);
            binLogBean.setServerId(header.getServerId());
            binLogBean.setSourceType(header.getSourceType());
            binLogBean.setVersion(header.getVersion());
            binLogBean.setExecuteTime(DateUtil.converteDate(header.getExecuteTime()+""));
            binLogBean.setLogfileName(header.getLogfileName());
            binLogBean.setLogfileOffset(header.getLogfileOffset());
            binLogBean.setSchemaName(header.getSchemaName());
            binLogBean.setTableName(header.getTableName());
            binLogBean.setEventType(eventType.toString());

            for (RowData rowData : rowChage.getRowDatasList()) {
                List<ColumnField> fields;
                if (eventType == EventType.DELETE) {
                    fields = getColumnFileds(rowData.getBeforeColumnsList());
                } else {
                    fields = getColumnFileds(rowData.getAfterColumnsList());
                }
                binLogBean.setColumnFieldsList(fields);
                //将信息生产到Kafka
                KafkaUtil.produce(topic, JSONObject.toJSON(binLogBean).toString());
            }
        }
    }

    /**
     * 对每一行的字段进行封装
     * @param columns
     * @return
     */
    private List<ColumnField> getColumnFileds(List<Column> columns) {
        List fields = new ArrayList<ColumnField>();
        for (Column column : columns) {
            ColumnField field = new ColumnField();
            field.setName(column.getName());
            field.setMysqlType(column.getMysqlType());
            field.setValue(column.getValue());
            field.setNullAble(column.getIsNull());
            field.setUpdated(column.getUpdated());
            fields.add(field);
        }
        return fields;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isStop() {
        return stop;
    }
}