package com.gzcb.creditcard.ido.binlog;

import com.gzcb.creditcard.ido.binlog.canal.CanalClientStater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Canal Client程序入口
 * @author lwj
 * @date 2018/7/4
 */
public class BinLogServerApplication {
    private static Logger log = LoggerFactory.getLogger(BinLogServerApplication.class);
    public static void main(String[] args) {
        new CanalClientStater().start();
        log.info("## 成功开启canal client进程...");
        while (Thread.activeCount() > 0){
            Thread.yield();
        }
    }
}