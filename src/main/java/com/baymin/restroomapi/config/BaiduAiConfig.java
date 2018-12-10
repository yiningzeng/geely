package com.baymin.restroomapi.config;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by baymin on 18-10-12:下午2:47
 * email: zengwei@galileo-ai.com
 * -------------------------------------
 * description:
 */
@Configuration
@Slf4j
public class BaiduAiConfig {

    @Bean
    public AipOcr aipOcrIni(){

        // 初始化一个AipFace
        AipOcr client = new AipOcr("11771510", "5OYU8U03yHbQQvIDl6kvY1su", "MEX9nOCsWsACZTPL4RtrY5A2dKaCGBK2");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        log.info("百度ocr初始化成功");
        return client;
    }
}
