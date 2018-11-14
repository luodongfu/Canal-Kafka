package com.gzcb.creditcard.ido.binlog.canal;

/**
 * Canal Client启动类
 * @author lwj
 * @date 2018/11/14
 */
public class CanalClientStater {
    /**
     *在项目启动的时候执行该方法
     * @throws Exception
     */
    public void start() {
        /**
         *调用线程对象的start方法
         */
        InitBinLog initBinLog= new InitBinLog();
        Thread t = new Thread(initBinLog);
        t.start();
    }
}
