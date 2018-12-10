package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.config.aspect.jwt.TokenUtils;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.dao.specs.RestRoomSpecs;
import com.baymin.restroomapi.entity.Level;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.entity.User;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.RestRoomService;
import com.baymin.restroomapi.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class RestRoomServiceImpl implements RestRoomService {
    @Autowired
    private RestRoomSpecs restRoomSpecs;
    @Autowired
    private RestRoomDao restRoomDao;


//    @Override
//    public Object updateByUsername(String username, String relName, String department,Integer levelId) throws MyException {
//        if(userDao.updateUserByUsername(username,relName,department,levelId)>0)return R.success();
//        else return R.error(ResultEnum.UPDATE_EMPTY);
//    }

    @Override
    public Object updateByRestRoomId(String username, String relName, String department, Integer levelId) throws MyException {
        return null;
    }

    @Override
    public Object save(RestRoom restRoom) throws MyException {
        return R.callBackRet(restRoomDao.findFirstByRestRoomNameAndRegion(restRoom.getRestRoomName(),restRoom.getRegion()), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                return R.error(ResultEnum.FAIL_ADD_RESTROOM_ALLREADY_EXIT);
            }

            @Override
            public Object onFalse() {
                if(restRoomDao.save(restRoom)!=null) return R.success();
                else return R.error(ResultEnum.FAIL_ADD_RESTROOM);
            }
        });
    }

    @Override
    public Object deleteByRestRoomId(Integer restRoomId) throws MyException {
        restRoomDao.deleteById(restRoomId);
        restRoomDao.flush();
        return R.success();
    }

    @Override
    public Object findAll(RestRoom specs, Pageable pageable) throws MyException {
        Page<RestRoom> retPage= restRoomDao.findAll(restRoomSpecs.listSpecsIni(specs),pageable);//userDao.findAll(example,pageable);
        if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
    }
}
