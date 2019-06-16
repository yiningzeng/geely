package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.InfoGasDailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by baymin on 17-8-7.
 * 更新所有的气体数据，增加设备类型
 *
 */
public interface InfoGasDailyStatisticsDao extends JpaRepository<InfoGasDailyStatistics, Integer>,JpaSpecificationExecutor<InfoGasDailyStatistics> {

    @Query(value = "select remark, count(*) as 'num', create_time from info_gas_daily_statistics where rest_room_id =?1 and create_time between ?2 and ?3 group by remark", nativeQuery = true)
    List<Map<String, Object>> findGasStatusOfDayCount(Integer restRoomId, String startTime, String endTime);

    List<InfoGasDailyStatistics> findAllByRestRoom_RestRoomIdAndCreateTimeBetween(Integer restRoomId, Date startTime, Date endTime);
}
