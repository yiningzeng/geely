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
import java.util.Date;
import java.util.Set;

/**
 * Created by baymin on 18-07-08.
 * 技能等级
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Level:用户技能等级")
public class Level implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer levelId;
    @ApiModelProperty(value = "技能等级名称",example = "水平1/4")
    private String levelName;
    @ApiModelProperty(value = "技能等级备注",example = "可干，有速度",notes = "notes")
    private String remark;

    @ApiModelProperty(value = "状态",example = "0：全组禁止|1：开放")
    private Integer status;
    //@Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createTime = new Date();

//    @JsonManagedReference
//    @OneToMany(mappedBy="level",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<User> users;
}
