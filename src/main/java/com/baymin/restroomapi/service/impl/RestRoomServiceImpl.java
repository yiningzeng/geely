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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RestRoomServiceImpl implements RestRoomService {
    @Autowired
    private RestRoomSpecs restRoomSpecs;
    @Autowired
    private RestRoomDao restRoomDao;


    @Override
    public Object updateByRestRoomId(Integer restRoomId, Optional<String> name,Optional<String> ip, Optional<String> region, Optional<String> address,Optional<Float> longitude,Optional<Float> latitude,Optional<String> cleaner, Optional<String> remark, Optional<Integer> status) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                RestRoom restRoom=(RestRoom)data;
                name.ifPresent(v->restRoom.setRestRoomName(v));
                ip.ifPresent(v->restRoom.setIp(v));
                region.ifPresent(v->restRoom.setRegion(v));
                address.ifPresent(v->restRoom.setAddress(v));
                longitude.ifPresent(v->restRoom.setLongitude(v));
                latitude.ifPresent(v->restRoom.setLatitude(v));
                cleaner.ifPresent(v->restRoom.setCleaner(v));
                remark.ifPresent(v->restRoom.setRemark(v));
                status.ifPresent(v->restRoom.setStatus(v));
                if(restRoomDao.save(restRoom)!=null) return R.success();
                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
            }

            @Override
            public Object onFalse() {
               return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
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
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                restRoomDao.deleteById(restRoomId);
                restRoomDao.flush();
                return R.success();
            }

            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });

    }

    @Override
    public Object findAll(RestRoom specs, Pageable pageable) throws MyException {
        return R.callBackRet(Optional.ofNullable(specs), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                Page<RestRoom> retPage= restRoomDao.findAll(restRoomSpecs.listSpecsIni(specs),pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }

            @Override
            public Object onFalse() {
                Page<RestRoom> retPage= restRoomDao.findAll(pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
        });

    }
}
