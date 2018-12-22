package com.baymin.restroomapi.service.impl;

import com.baymin.restroomapi.dao.DeviceCameraDao;
import com.baymin.restroomapi.dao.RestRoomDao;
import com.baymin.restroomapi.dao.specs.RestRoomSpecs;
import com.baymin.restroomapi.entity.DeviceCamera;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.service.DeviceCameraService;
import com.baymin.restroomapi.service.RestRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DeviceCameraServiceImpl implements DeviceCameraService {
    @Autowired
    private DeviceCameraDao deviceCameraDao;
    @Autowired
    private RestRoomDao restRoomDao;


    @Override
    public Object updateByDeviceCameraId(Integer deviceCameraId, Optional<Integer> restRoomId, Optional<String> ip,Optional<String> username,Optional<String> password, Optional<String> remark, Optional<Integer> status) throws MyException {
        return R.callBackRet(deviceCameraDao.findById(deviceCameraId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                DeviceCamera deviceCamera=(DeviceCamera)data;
                restRoomId.ifPresent(v->restRoomDao.findById(v).ifPresent(a->deviceCamera.setRestRoom(a)));
//                restRoomId.ifPresent(v->deviceCamera.setRestRoom(v));
                ip.ifPresent(v->deviceCamera.setIp(v));
                username.ifPresent(v->deviceCamera.setUsername(v));
                password.ifPresent(v->deviceCamera.setPassword(v));
                status.ifPresent(v->deviceCamera.setStatus(v));
                remark.ifPresent(v->deviceCamera.setRemark(v));
                if(deviceCameraDao.save(deviceCamera)!=null) return R.success();
                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
            }

            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }


    @Override
    public Object save(Integer restRoomId, DeviceCamera deviceCamera) throws MyException {
        return R.callBackRet(restRoomDao.findById(restRoomId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                deviceCamera.setRestRoom((RestRoom)data);
                if(deviceCameraDao.save(deviceCamera)!=null)return R.success();
                return R.error(ResultEnum.FAIL_ACTION_MESSAGE);
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_RESTROOM);
            }
        });
    }


    @Override
    public Object deleteByDeviceCameraId(Integer deviceCameraId) throws MyException {
        return R.callBackRet(deviceCameraDao.findById(deviceCameraId), new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                deviceCameraDao.deleteById(deviceCameraId);
                deviceCameraDao.flush();
                return R.success();
            }
            @Override
            public Object onFalse() {
                return R.error(ResultEnum.FAIL_DO_NO_DEVICE);
            }
        });
    }

    @Override
    public Object findAll(Integer restRoomId,Optional<Integer> status,Pageable pageable) throws MyException {
        return R.callBackRet(status, new R.OptionalResult() {
            @Override
            public Object onTrue(Object data) {
                Page<DeviceCamera> retPage= deviceCameraDao.findAllByStatus((Integer)data,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
            @Override
            public Object onFalse() {
                Page<DeviceCamera> retPage= deviceCameraDao.findAllByRestRoom_RestRoomId(restRoomId,pageable);//userDao.findAll(example,pageable);
                if(retPage.getSize()>0)return R.success(retPage);else return R.error(ResultEnum.NO_LIST,retPage);
            }
        });

    }
}
