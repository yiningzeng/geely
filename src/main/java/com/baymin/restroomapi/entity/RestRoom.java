package com.baymin.restroomapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by baymin on 18-07-08.
 * 技能等级
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "restroom:厕所类")
public class RestRoom implements Serializable {
    @Id
    @GeneratedValue
    private Integer restRoomId;
    @ApiModelProperty(value = "厕所名称",example = "碧海蓝天5星级公厕")
    private String restRoomName;
    @ApiModelProperty(value = "备注",example = "国内一流",notes = "notes")
    private String remark;
    @ApiModelProperty(value = "状态",example = "0：厕所关闭|1：厕所对外开放")
    private Integer status;
    //@Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createTime = new Date();

    @OneToMany(mappedBy = "restRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<DeviceCamera> deviceCameras= new ArrayList<>();


    @OneToMany(mappedBy = "restRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<DeviceBulletinBoard> deviceBulletinBoards= new ArrayList<>();

    @OneToMany(mappedBy = "restRoom",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<DeviceGas> deviceGases= new ArrayList<>();

    public void addDeviceCamera(DeviceCamera comment) {
        deviceCameras.add(comment);
        comment.setRestRoom(this);
    }

    public void removeDeviceCamera(DeviceCamera comment) {
        deviceCameras.remove(comment);
        comment.setRestRoom(null);
    }
    public void addDeviceBulletinBoard(DeviceBulletinBoard comment) {
        deviceBulletinBoards.add(comment);
        comment.setRestRoom(this);
    }

    public void removeDeviceBulletinBoard(DeviceBulletinBoard comment) {
        deviceBulletinBoards.remove(comment);
        comment.setRestRoom(null);
    }
    public void addDeviceGas(DeviceGas comment) {
        deviceGases.add(comment);
        comment.setRestRoom(this);
    }

    public void removeDeviceGas(DeviceGas comment) {
        deviceGases.remove(comment);
        comment.setRestRoom(null);
    }

//    @JsonManagedReference
//    @OneToMany(mappedBy="level",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<User> users;
}
