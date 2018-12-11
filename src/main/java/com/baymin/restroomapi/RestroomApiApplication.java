package com.baymin.restroomapi;

import com.baymin.restroomapi.dao.UserDao;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.MultipartConfigElement;

/**
 * jpa属性详解
 * https://www.w3cschool.cn/java/jpa-column-unique-nullable.html
 * 原型 https://pro.modao.cc/app/42d72de617fab9ec148474a4bb696ad029806028#screen=sE1496AC6781533113614692
 */
@Controller
@RestController
@RequestMapping
@SpringBootApplication
@Configuration
@EnableAdminServer
public class RestroomApiApplication {

    @Value("${server.port}")
    private String port;



    public static void main(String[] args) {

        SpringApplication.run(RestroomApiApplication.class, args);
    }
//    @PostMapping("/login.html")
//    public Object post() throws Exception {
//        return port;
//    }
//
//    @GetMapping("/ip")
//    public Object test() throws Exception {
//        return port;
//    }
//
//
//    @GetMapping("/manage")
//    public String index() throws Exception {
//        return "index";
//    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("10MB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("20MB");
        return factory.createMultipartConfig();
    }

}
