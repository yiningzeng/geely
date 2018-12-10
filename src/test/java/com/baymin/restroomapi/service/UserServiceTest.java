package com.baymin.restroomapi.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by baymin on 18-8-29 下午11:41
 * email: zengwei@galileo-ai.com
 * -------------------------------------
 * description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void userLogin() throws Exception{
//        String url = "/api/v1/user/login";//访问url
//        String expectedResult = "hello";//预期返回结果
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON))
//                .andReturn();
//        //访问返回状态
//        int status = mvcResult.getResponse().getStatus();
//        //接口返回结果
//        String content = mvcResult.getResponse().getContentAsString();
//        //打印结果和状态
//        //System.out.println(status);
//        //System.out.println(content);
//        //断言预期结果和状态
//        Assert.assertTrue("错误", status == 200);
//        Assert.assertFalse("错误", status != 200);
//        Assert.assertTrue("数据一致", expectedResult.equals(content));
//        Assert.assertFalse("数据不一致", !expectedResult.equals(content));

//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userNumber","0001");
//        map.put("password","e10adc3949ba59abbe56e057f20f883e");
//
//        mvc.perform(MockMvcRequestBuilders.post("/api/v1/user/login",map))
//                .andExpect(MockMvcResultMatchers.status().isOk());//期望通过
//        //.andExpect(MockMvcResultMatchers.content().string("abc"));//期望结果是一个值


    }
}