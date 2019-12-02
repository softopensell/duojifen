package com.yunpian.sdk.util;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * Created by bingone on 15/11/10.
 */
public class LoggerUtil {
    private static boolean isLog = false;
//    private static Logger logger = Logger.getLogger(YunpianClient.class);

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("yunpian_log4j.properties"));
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
