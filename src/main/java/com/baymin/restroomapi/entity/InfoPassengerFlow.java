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
@ApiModel(value = "InfoPassengerFlow:客流统计类")
public class InfoPassengerFlow implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "通道名称",example = "男厕")
    private String channelName;

    @ApiModelProperty(value = "设备的ip",example = "关联公厕")
    private String ip;

    @ApiModelProperty(value = "人流总数",example = "1111")
    private Integer number;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    private Date createTime = new Date();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restRoomId")
    @JsonBackReference
    private RestRoom restRoom;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceCamera )) return false;
        return id != null && id.equals(((InfoPassengerFlow) o).id);
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
