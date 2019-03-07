package com.baymin.restroomapi.entity;

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
 * 数据库对应的用户表表
 * oneToMany等参考
 * https://yq.aliyun.com/articles/372728
 * https://www.jianshu.com/p/bc0236f7dc98
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "User:用户信息")
public class User implements Serializable {

    @Transient
    @ApiModelProperty("验证信息")
    private String token;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户存储的key", example = "1")
    private Integer userId;
    @ApiModelProperty(value = "用户登录名", example = "lala")
    private String username;
    @ApiModelProperty(value = "真实姓名", example = "张三")
    private String relName;
    @ApiModelProperty(value = "用户头像-预设值")
    private String userHeadUrl="https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    @ApiModelProperty(value = "所属岗位", example = "技术部")
    private String department;
    @ApiModelProperty(value = "用户类型{0：禁用|1：启用}", example = "1")
    private Integer userStatus;
    //@Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "创建时间")
    private Date createTime = new Date();
    //@Embedded
    //@JsonBackReference
    @ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name="levelId")
    private Level level;
}
