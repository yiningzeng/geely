package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baymin on 17-8-7.
 */
public interface LevelDao extends JpaRepository<Level, Integer> {

    Level findLevelByLevelId(Integer levelId);

}
