package com.baymin.restroomapi.ret.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * http请求返回的最外层对象
 * Created by baymin
 * 2017-01-10 13:34
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Result<T>：返回数据")
public class Result<T> {

    /**
     * 错误码.
     * 成功：0
     * 失败：-1
     */
    @ApiModelProperty(value = "状态吗{成功：0|失败：-1}")
    private Integer code=-1;

    /**
     * 请求状态码
     */
    @ApiModelProperty(value = "失败错误码")
    private Integer status=0;

    /**
     * 提示信息.
     */
    @ApiModelProperty(value = "提示信息")
    private String msg="";

    /**
     * 具体的内容.
     */
    @ApiModelProperty(value = "数据集T")
    private T data;
}
