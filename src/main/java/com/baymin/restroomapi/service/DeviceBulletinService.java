package com.baymin.restroomapi.service;

import com.baymin.restroomapi.entity.DeviceBulletinBoard;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Created by baymin
 * 2018-08-08 11:43
 */
public interface DeviceBulletinService {

    /**
     * 更新公告屏
     * @throws MyException
     */
    Object updateByBulletinBoardId(Integer boardId, Optional<String> ip, Optional<Integer> status)throws MyException;

    /**
     * 新增
     * @param deviceBulletinBoard
     * @return
     * @throws MyException
     */
    Object save(DeviceBulletinBoard deviceBulletinBoard) throws MyException;

    /**
     * 删除
     * @param boardId
     * @return
     * @throws MyException
     */
    Object deleteBydeviceByBulletinBoardId(Integer boardId) throws MyException;

    /**
     * 查找全部公厕
     * @param specs 查询的条件
     * @param pageable
     * @return
     * @throws MyException
     */
    Object findAll(DeviceBulletinBoard specs, Pageable pageable)throws MyException;

}
