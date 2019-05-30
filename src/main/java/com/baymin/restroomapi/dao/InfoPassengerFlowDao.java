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

    @Query(name = "查询总人数",value = "select sum(number) from info_passenger_flow info where info.rest_room_id=?1 and info.update_time between ?2 and ?3", nativeQuery = true)
    Integer findAllSumNumber(Integer restRoomId, String startTime, String endTime);
    @Query(name = "查询总人数",value = "select sum(number) from info_passenger_flow info where info.rest_room_id=?1", nativeQuery = true)
    Integer findAllSumNumberNoTime(Integer restRoomId);

    @Query(name = "查询所有数据",value = "select number, DATE_FORMAT(update_time,'%H:%i') as 'show_time' from info_passenger_flow info where info.rest_room_id=?1 and info.update_time between ?2 and ?3 order by show_time asc", nativeQuery = true)
    List<Map<String, Object>> findAll(Integer restRoomId, String startTime, String endTime);


    @Query(name = "查询所有数据show days",value = "SELECT DATE_FORMAT(update_time,'%Y-%m-%d') show_time, sum(number) number from info_passenger_flow where rest_room_id=?1 and update_time between ?2 and ?3 group by show_time order by show_time asc", nativeQuery = true)
    List<Map<String, Object>> findAllOnlyShowDays(Integer restRoomId, String startTime, String endTime);
}
