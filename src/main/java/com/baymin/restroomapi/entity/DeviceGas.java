package com.baymin.restroomapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@ApiModel(value = "DeviceGas:空气检测类")
public class DeviceGas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id", example = "1")
    private Integer gasId;

    @ApiModelProperty(value = "硬件方设备id", example = "1")
    private Integer gasDeviceId;
//    @ApiModelProperty(value = "客流量", example = "12")
//    private String passengerFlow;

    @ApiModelProperty(value = "{0：男厕|1：女厕}")
    private Integer type;

    @ApiModelProperty(value = "空气检测类型{0：禁用|1：启用}", example = "1")
    private Integer status=1;
    //@Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "创建时间", example = "1")
    private Date createTime = new Date();

    @OneToMany(mappedBy = "deviceGas",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    @ApiModelProperty(value = "单个气体设备采集的数据")
    private List<InfoGas> infoGases= new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restRoomId")
    @JsonBackReference
    private RestRoom restRoom;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceCamera )) return false;
        return gasId != null && gasId.equals(((DeviceGas) o).gasId);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
