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
    private Integer id;

    @ApiModelProperty(value = "终端id，这个在设备id下面", example = "1")
    private Integer gasDeviceId;

    @ApiModelProperty(value = "硬件方设备id，相当于终端id的父级", example = "1")
    private Integer gasDeviceParentId;
//    @ApiModelProperty(value = "客流量", example = "12")
//    private String passengerFlow;

    /**
     *  <Radio value={0}>大厅</Radio>
     *             <Radio value={1}>女厕</Radio>
     *             <Radio value={2}>男厕</Radio>
     *             <Radio value={3}>无障碍</Radio>
     */
    @ApiModelProperty(value = "{0：大厅|1：女厕|2：男厕|3：无障碍}")
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
        return gasDeviceId != null && gasDeviceId.equals(((DeviceGas) o).gasDeviceId);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
