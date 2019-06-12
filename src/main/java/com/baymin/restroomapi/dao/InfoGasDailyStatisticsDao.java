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

}
