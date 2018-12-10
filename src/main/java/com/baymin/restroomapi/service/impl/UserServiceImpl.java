package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.aspect.jwt.TokenUtils;
import com.baymin.restroomapi.dao.LevelDao;
import com.baymin.restroomapi.dao.UserDao;
import com.baymin.restroomapi.dao.specs.UserSpecs;
import com.baymin.restroomapi.entity.Level;
import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.UserService;
import com.baymin.restroomapi.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserSpecs userSpecs;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LevelDao levelDao;



    @Override
    public Object updatePasswordByUsername(String username, String password) throws MyException {
        Optional salt=userDao.findSaltByUsernameOp(username);

       AtomicInteger ret = new AtomicInteger();
        salt.ifPresent(s->

            ret.set(userDao.updatePasswordByUsername(username, Utils.sha256(password, s + "-" + username)))
            );
        if(ret.get()>0)return R.success();
        else return R.error(ResultEnum.FAIL_ACTION_MESSAGE);


        //return salt.map(a->new Result<>()).orElseGet(()->new Result<>(0,0,"",ResultEnum.FAIL_PARS));
//        salt.ifPresent(s->{
//            userDao.updatePasswordByUserNumber(userNumber,;
//            return null;
//        });
//
//
//
//
//        return R.error(ResultEnum.FAIL_PARS);
    }

    @Override
    public Object updateByUsername(String username, String relName, String department,Integer levelId) throws MyException {
        if(userDao.updateUserByUsername(username,relName,department,levelId)>0)return R.success();
        else return R.error(ResultEnum.UPDATE_EMPTY);
    }

    Object login(String username,String password,String token){
        String salt=userDao.findSaltByUsername(username);
        if(salt==null)return R.error(ResultEnum.USER_UN_REGIST);
        Optional<User>optionalUser= userDao.findByUsernameAndPasswordAndUserStatus(username,Utils.sha256(password,salt+"-"+username),1);
        optionalUser.ifPresent(v->v.setToken(token));
        return R.ret(optionalUser,ResultEnum.USER_ERR_PASS);
    }

    @Override
    public Object userLogin(String userNumber, String password) throws MyException {
       return login(userNumber,password,TokenUtils.getJWTString(userNumber));

        //if(user!=null)return R.success(user);
        //else return R.error();
    }

    @Override
    public Object save(User u) throws MyException {
        return R.callBackRet(userDao.findFirstByUsername(u.getUsername()), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                return R.error(ResultEnum.FAIL_ADD_USER_ALLREADY_EXIT);
            }

            @Override
            public Object onFalse() {
                if(userDao.save(u)!=null) return R.success();
                else return R.error(ResultEnum.FAIL_ADD_USER);
            }
        });
    }

    @Override
    public Object delete(Integer userId) throws MyException {
        userDao.deleteById(userId);
        userDao.flush();
        return R.success();
//        return R.callBackRet(userDao.findById(userId), new R.OptionalResult() {
//            @Override
//            public Object onTrue(Object data) {
//                User user=(User)data;
//                user.setUserType(3);
//                userDao.save(user);
//                return R.success();
//            }
//
//            @Override
//            public Object onFalse() {
//                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
//            }
//        });
    }

    @Override
    public Object findAll(Integer userType,User userConditions, Pageable pageable) throws MyException {
        Page<User> retPage= userDao.findAll(userSpecs.userList(userConditions),pageable);//userDao.findAll(example,pageable);
        if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
    }

    @Override
    public Level findLevelById(Integer levelId) throws MyException {
        return levelDao.findLevelByLevelId(levelId);
    }


}
