package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.InfoPassengerFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by baymin on 17-8-7.
 */
public interface InfoPassengerFlowDao extends JpaRepository<InfoPassengerFlow, Integer>,JpaSpecificationExecutor<InfoPassengerFlow> {

    @Query(name = "查询所有数据",value = "select number as '人数', DATE_FORMAT(update_time,'%m-%d %H:%i') as 'show_time' from info_passenger_flow info where info.rest_room_id=?1 and info.update_time between ?2 and ?3", nativeQuery = true)
    List<Map<String, Object>> findAll(Integer restRoomId, String startTime, String endTime);
}
