package com.baymin.restroomapi;

import com.baymin.restroomapi.dao.UserDao;
import com.baymin.restroomapi.entity.FuckFlow;
import com.baymin.restroomapi.service.RestRoomService;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.MultipartConfigElement;
import java.util.Date;

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
@Slf4j
@EnableCaching
@EnableScheduling
public class RestroomApiApplication {

    @Value("${server.port}")
    private String port;
    @Autowired
    private RestRoomService restRoomService;


    public static void main(String[] args) {

        SpringApplication.run(RestroomApiApplication.class, args);
    }
    @PostMapping(value = "/api/fuck-flow", consumes = { MediaType.APPLICATION_XML_VALUE }, produces = MediaType.APPLICATION_XML_VALUE)
    public Object post(@RequestBody FuckFlow fuckFlow) throws Exception {

        log.info("==============end==============={}",fuckFlow.toString());
        return restRoomService.fuckFlowByOnce(fuckFlow);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "客流",required = true, dataType = "string", paramType = "path"),
    })
    @PostMapping(value = "/api/fuck-flow/test/{num}")
    public Object post(@PathVariable(value = "num") Integer num) throws Exception {
        FuckFlow fuckFlow=new FuckFlow();
        fuckFlow.setIpAddress("192.168.10.4");
        fuckFlow.setPeopleCounting(new FuckFlow.PeopleCounting(num,0,0));
        log.info("==============end==============={}",fuckFlow.toString());
        return restRoomService.fuckFlowByOnce(fuckFlow);
    }

    @ResponseBody
    @RequestMapping(value = "/test/json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getByJSON(@RequestBody JSONObject jsonParam) {
        // 直接将json信息打印出来
//        System.out.println(jsonParam.toJSONString());
        log.info(jsonParam.toJSONString());
        log.info("==============end===============");
        return "res";
    }
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
