package com.baymin.restroomapi.service;

import com.baymin.restroomapi.entity.DeviceGas;
import com.baymin.restroomapi.ret.exception.MyException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Created by baymin
 * 2018-08-08 11:43
 */
public interface DeviceGasService {


    /**
     * 更新厕所
     * @param deviceCameraId
     * @param restRoomId
     * @param ip
     * @param status
     * @return
     * @throws MyException
     */
    //Object updateByDeviceCameraId(Integer deviceCameraId, Optional<Integer> restRoomId, Optional<String> ip, Optional<String> username, Optional<String> password, Optional<String> remark, Optional<Integer> status)throws MyException;

    /**
     * 新增气体检测设备
     * @param restRoomId
     * @param deviceGas
     * @return
     * @throws MyException
     */
    Object save(Integer restRoomId, DeviceGas deviceGas) throws MyException;

    /**
     * 删除
     * @param deviceGasId
     * @return
     * @throws MyException
     */
    Object deleteByDeviceGasId(Integer deviceGasId) throws MyException;

    /**
     * 查找全部
     * @param pageable
     * @return
     * @throws MyException
     */
    Object findAll(Integer restRoomId,Optional<Integer> status, Pageable pageable)throws MyException;

    Object findAllGasListHome(Integer restRoomId,Integer startTm,Integer endTm)throws MyException;
    Object findAllGasList(Integer restRoomId,Integer startTm,Integer endTm)throws MyException;
}
