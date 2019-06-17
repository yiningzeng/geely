package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.DeviceGas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by baymin on 17-8-7.
 */
public interface DeviceGasDao extends JpaRepository<DeviceGas, Integer>,JpaSpecificationExecutor<DeviceGas> {

    Page<DeviceGas> findAllByStatus(Integer status, Pageable pageable);

    Optional<DeviceGas> findFirstByGasDeviceId(Integer gasDeviceId);

    Optional<DeviceGas> findFirstByGasDeviceParentId(Integer gasDeviceParentId);

    Optional<DeviceGas> findFirstByRestRoom_RestRoomId(Integer restRoomId);

    Page<DeviceGas> findAllByRestRoom_RestRoomId(Integer restRoomId, Pageable pageable);

    List<DeviceGas> findAllByRestRoom_RestRoomId(Integer restRoomId);

    @Query(name = "只查询气体设备的一条最新的温度和气值",value = "select * from device_gas where rest_room_id =?1", nativeQuery = true)
    List<Map<String, Object>> findAllByRestRoomIdWithQuery(Integer restRoomId);

//    List<DeviceGas> findAllByRestRoom_RestRoomIdAndInfoGases_CreateTimeBetween(Integer restRoomId, Date startTime, Date endTime);

    @Transactional
    @Modifying
    Integer deleteAllByGasDeviceId(Integer gasDeviceId);
}
