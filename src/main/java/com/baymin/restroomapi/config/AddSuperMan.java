package com.baymin.restroomapi.config;

import com.baymin.restroomapi.dao.LevelDao;
import com.baymin.restroomapi.dao.UserDao;
import com.baymin.restroomapi.entity.Level;
import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by baymin on 18-10-12:下午2:47
 * email: zengwei@galileo-ai.com
 * -------------------------------------
 * description:
 */
@Configuration
@Slf4j
public class AddSuperMan {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LevelDao levelDao;


    @Bean
    public int areYouKiddingMe(){

        Level level=new Level();
        if(!levelDao.findByLevelName("普通用户").isPresent()){
            level.setLevelName("普通用户");
            level.setRemark("just so so");
            level.setStatus(1);
            levelDao.save(level);
            log.info("创建普通角色组");
        }

        Optional<Level> a=levelDao.findByLevelName("管理员");
        if(!a.isPresent()){
            level=new Level();
            level.setLevelName("管理员");
            level.setRemark("管理员");
            level.setStatus(1);
            level=levelDao.save(level);
            log.info("创建管理员组");
        }
        else  level=a.get();

        if(userDao.findFirstByUsername("baymin").isPresent())return 1;
        String salt= UUID.randomUUID().toString();
        User user=new User();
        user.setUserStatus(1);
        user.setUsername("baymin");
        user.setDepartment("厕所管理者");
        user.setLevel(level);
        user.setPassword(Utils.sha256("e10adc3949ba59abbe56e057f20f883e",salt+"-"+"baymin"));
        user.setRelName("超级管理员");
        user.setSalt(salt);
        user.setUserHeadUrl("https://upload.jianshu.io/users/upload_avatars/6639127/07e46067-9c7b-4df9-9c1f-b590818e295b.jpg");
        userDao.save(user);
        log.info("创建管理员");
        return 1;
    }
}
