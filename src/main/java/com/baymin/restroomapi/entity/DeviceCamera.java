package com.baymin.restroomapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
 * 数据库对应的用户表表
 * oneToMany等参考
 * https://yq.aliyun.com/articles/372728
 * https://www.jianshu.com/p/bc0236f7dc98
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DeviceCamera:摄像头类")
public class DeviceCamera implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "摄像头id", example = "1")
    private Integer cameraId;
    @ApiModelProperty(value = "摄像头公网ip", example = "anything")
    private String ip;
//    @ApiModelProperty(value = "客流量", example = "12")
//    private String passengerFlow;

    @ApiModelProperty(value = "摄像头类型{0：禁用|1：启用}", example = "1")
    private Integer status=1;

    //@Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "创建时间", example = "1")
    private Date createTime = new Date();

    @OneToMany(mappedBy = "deviceCamera",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    @ApiModelProperty(value = "单个摄像头采集的人流数据")
    private List<InfoPassengerFlow> infoPassengerFlows= new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restRoomId")
    @JsonBackReference
    private RestRoom restRoom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceCamera )) return false;
        return cameraId != null && cameraId.equals(((DeviceCamera) o).cameraId);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
