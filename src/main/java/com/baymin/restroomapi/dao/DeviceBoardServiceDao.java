package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.DeviceBoard;
import com.baymin.restroomapi.entity.RestRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by baymin on 17-8-7.
 */
public interface DeviceBoardServiceDao extends JpaRepository<DeviceBoard, Integer>,JpaSpecificationExecutor<RestRoom> {

//    Optional<DeviceBoard> findFirstByRestRoomNameAndRegion(String restRoomName, String region);
}
