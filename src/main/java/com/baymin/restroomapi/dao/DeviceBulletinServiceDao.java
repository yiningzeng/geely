package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.DeviceBulletinBoard;
import com.baymin.restroomapi.entity.RestRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Created by baymin on 17-8-7.
 */
public interface DeviceBulletinServiceDao extends JpaRepository<DeviceBulletinBoard, Integer>,JpaSpecificationExecutor<RestRoom> {

    Optional<DeviceBulletinBoard> findFirstByRestRoomNameAndRegion(String restRoomName, String region);
}
