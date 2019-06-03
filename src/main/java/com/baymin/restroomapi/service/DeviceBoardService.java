package com.baymin.restroomapi.service;

import com.baymin.restroomapi.entity.DeviceBoard;
import com.baymin.restroomapi.ret.exception.MyException;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * Created by baymin
 * 2018-08-08 11:43
 */
public interface DeviceBoardService {


    /**
     * 更新厕所
     * @param deviceBoardId
     * @param restRoomId
     * @param ip
     * @param status
     * @return
     * @throws MyException
     */
    Object updateByDeviceBoardId(Integer deviceBoardId, Optional<Integer> restRoomId, Optional<String> ip, Optional<Integer> status)throws MyException;

    /**
     * 新增公告屏
     * @param restRoomId
     * @param deviceBoard
     * @return
     * @throws MyException
     */
    Object save(Integer restRoomId, DeviceBoard deviceBoard) throws MyException;

    /**
     * 删除公厕
     * @param deviceBoardId
     * @return
     * @throws MyException
     */
    Object deleteByDeviceBoardId(Integer deviceBoardId) throws MyException;

    /**
     * 查找全部摄像头
     * @param pageable
     * @return
     * @throws MyException
     */
    Object findAll(Integer restRoomId, Optional<Integer> status, Pageable pageable)throws MyException;

    /**
     * 公厕公告屏主动获取信息
     * @param ip
     * @return
     * @throws MyException
     */
    Object giveMeFive(String ip) throws MyException;

    /**
     * 公厕公告屏主动刷新客流
     * @param ip
     * @return
     * @throws MyException
     */
    Object getOnlyFuckFlow(String ip) throws MyException;

}
