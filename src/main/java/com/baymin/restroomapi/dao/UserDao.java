package com.baymin.restroomapi.dao;

import com.baymin.restroomapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by baymin on 17-8-7.
 */
public interface UserDao extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {

    Optional<User> findFirstByUsername(String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user u SET u.rel_name=?2,u.department=?3,u.level_id=?4 WHERE u.username=?1", nativeQuery = true)
    int updateUserByUsername(String username,String relName,String department,Integer levelId);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE User u SET u.password=?2 WHERE u.username=?1")
    int updatePasswordByUsername(String username,String password);



    Optional<User> findByUsernameAndPasswordAndUserStatus(String username,String password,Integer userStatus);


    @Query(name = "查询用户加密盐",value = "select salt from user u where u.username=?1", nativeQuery = true)
    String findSaltByUsername(String username);

    @Query(name = "查询用户加密盐",value = "select salt from user u where u.username=?1", nativeQuery = true)
    Optional<String> findSaltByUsernameOp(String username);

}
