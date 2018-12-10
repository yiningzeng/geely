package com.baymin.restroomapi.service;

import com.baymin.restroomapi.entity.Level;
import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.ret.exception.MyException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Created by baymin
 * 2018-08-08 11:43
 */
public interface UserService {


    /**
     * 更新密码操作
     * @param username
     * @param password
     * @return
     * @throws MyException
     */
    Object updatePasswordByUsername(String username,String password)throws MyException;

    /**
     * 更新用户byUserNumber
     * @param username
     * @return
     * @throws MyException
     */

    Object updateByUsername(String username,String relName,String department,Integer levelId)throws MyException;

    /**
     * 用户登录操作
     *
     * @return
     * @throws Exception
     */
    Object userLogin(String username,String password) throws MyException;

    /**
     * 保存用户
     * @param u
     * @return
     * @throws MyException
     */
    Object save(User u) throws MyException;
    Object delete(Integer userId) throws MyException;

    /**
     * 查找全部用户
     * @param userConditions 查询的条件
     * @param pageable
     * @return
     * @throws MyException
     */
    Object findAll(Integer userType,User userConditions,Pageable pageable)throws MyException;


    /**
     * 查询用户等级
     * @param levelId
     * @return
     * @throws MyException
     */
    Level findLevelById(Integer levelId)throws MyException;

}
