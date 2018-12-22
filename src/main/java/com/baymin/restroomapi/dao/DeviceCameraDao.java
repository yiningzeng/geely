package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.DeviceCamera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by baymin on 17-8-7.
 */
public interface DeviceCameraDao extends JpaRepository<DeviceCamera, Integer>,JpaSpecificationExecutor<DeviceCamera> {

    //弃用不适用
    Page<DeviceCamera> findAllByStatus(Integer status,Pageable pageable);

    Page<DeviceCamera> findAllByRestRoom_RestRoomId(Integer restRoomId,Pageable pageable);
}
