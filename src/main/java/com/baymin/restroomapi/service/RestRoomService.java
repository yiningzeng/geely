package com.baymin.restroomapi.service;

import com.baymin.restroomapi.dao.specs.RestRoomSpecs;
import com.baymin.restroomapi.entity.RestRoom;
import com.baymin.restroomapi.ret.exception.MyException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Created by baymin
 * 2018-08-08 11:43
 */
public interface RestRoomService {


    /**
     * 更新厕所
     * @param restRoomId
     * @param name
     * @param region
     * @param address
     * @param remark
     * @param status
     * @return
     * @throws MyException
     */
    Object updateByRestRoomId(Integer restRoomId, Optional<String> name,Optional<String> ip,Optional<String> region,Optional<String> address,Optional<Float> longitude,Optional<Float> latitude,Optional<String> cleaner,Optional<String> remark,Optional<Integer> status)throws MyException;

    /**
     * 新增公厕
     * @param restRoom
     * @return
     * @throws MyException
     */
    Object save(RestRoom restRoom) throws MyException;

    /**
     * 删除公厕
     * @param restRoomId
     * @return
     * @throws MyException
     */
    Object deleteByRestRoomId(Integer restRoomId) throws MyException;

    /**
     * 查找全部公厕
     * @param specs 查询的条件
     * @param pageable
     * @return
     * @throws MyException
     */
    Object findAll(RestRoom specs, Pageable pageable)throws MyException;

}
