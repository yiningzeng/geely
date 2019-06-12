package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.InfoGas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by baymin on 17-8-7.
 * 更新所有的气体数据，增加设备类型
 *
 */
public interface InfoGasDao extends JpaRepository<InfoGas, Integer>,JpaSpecificationExecutor<InfoGas> {
    List<InfoGas> findAllByDeviceGas_GasDeviceIdAndCreateTimeBetween(Integer deviceGasId, Date start, Date end);

    @Query(value = "select id, update_time, ROUND((select score from restroom.info_gas where update_time = info.update_time and type =0 and rest_room_id = info.rest_room_id),2) as '大厅'," +
            "ROUND((select score from restroom.info_gas where update_time = info.update_time and type =1 and rest_room_id = info.rest_room_id),2) as '女厕'," +
            "  ROUND((select score from restroom.info_gas where update_time = info.update_time and type =2 and rest_room_id = info.rest_room_id),2) as '男厕'," +
            "  ROUND((select score from restroom.info_gas where update_time = info.update_time and type =3 and rest_room_id = info.rest_room_id),2) as '无障碍'" +
            "from info_gas info where rest_room_id = ?1 and update_time between ?2 and ?3 GROUP BY update_time order by update_time asc", nativeQuery = true)
    List<Map<String, Object>> findAllGasInfo(Integer restRoomId, String startTime, String endTime);

}
