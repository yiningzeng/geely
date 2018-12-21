package com.baymin.restroomapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by baymin on 18-07-08.
 * 技能等级
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "InfoGas:气体统计类")
public class InfoGas implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "数据采集终端id")
    private Integer funcId;

    @ApiModelProperty(value = "空气分值",example = "61")
    private Float score;

    @ApiModelProperty(value = "是否及格",example = "0:不及格|1:及格")
    private Integer status;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    private Date createTime = new Date();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restRoomId")
    @JsonBackReference
    private RestRoom restRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasDeviceId")
    @JsonBackReference
    private DeviceGas deviceGas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceCamera )) return false;
        return id != null && id.equals(((InfoGas) o).id);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
