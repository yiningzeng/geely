package com.baymin.restroomapi.service;

import com.baymin.restroomapi.entity.DeviceCamera;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Created by baymin
 * 2018-08-08 11:43
 */
public interface DeviceCameraService {


    /**
     * 更新厕所
     * @param deviceCameraId
     * @param restRoomId
     * @param ip
     * @param status
     * @return
     * @throws MyException
     */
    Object updateByDeviceCameraId(Integer deviceCameraId,Optional<Integer> restRoomId, Optional<String> ip,Optional<String> username,Optional<String> password,Optional<String> remark, Optional<Integer> status)throws MyException;

    /**
     * 新增摄像头
     * @param restRoomId
     * @param deviceCamera
     * @return
     * @throws MyException
     */
    Object save(Integer restRoomId, DeviceCamera deviceCamera) throws MyException;

    /**
     * 删除公厕
     * @param deviceCameraId
     * @return
     * @throws MyException
     */
    Object deleteByDeviceCameraId(Integer deviceCameraId) throws MyException;

    /**
     * 查找全部摄像头
     * @param pageable
     * @return
     * @throws MyException
     */
    Object findAll(Integer restRoomId,Optional<Integer> status,Pageable pageable)throws MyException;

    Object push(Integer cameraId) throws MyException;
    Object stop(Integer cameraId) throws MyException;
}
