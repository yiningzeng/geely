package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by baymin on 17-8-7.
 */
public interface LevelDao extends JpaRepository<Level, Integer> {

    Level findLevelByLevelId(Integer levelId);
    Optional<Level> findByLevelName(String name);

}
