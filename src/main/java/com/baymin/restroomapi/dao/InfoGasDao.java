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
 */
public interface InfoGasDao extends JpaRepository<InfoGas, Integer>,JpaSpecificationExecutor<InfoGas> {
    List<InfoGas> findAllByDeviceGas_GasDeviceIdAndCreateTimeBetween(Integer deviceGasId, Date start, Date end);
}
