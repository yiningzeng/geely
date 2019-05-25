package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.DeviceBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by baymin on 17-8-7.
 */
public interface DeviceBoardDao extends JpaRepository<DeviceBoard, Integer>,JpaSpecificationExecutor<DeviceBoard> {

    //弃用不适用
    Page<DeviceBoard> findAllByStatus(Integer status, Pageable pageable);

    Page<DeviceBoard> findAllByRestRoom_RestRoomId(Integer restRoomId, Pageable pageable);

    List<DeviceBoard> findAllByRestRoom_RestRoomId(Integer restRoomId);
}
