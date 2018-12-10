package com.baymin.restroomapi.ret.model;

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
public class ResultNoData {


    /**
     * 错误码.
     * 成功：0
     * 失败：-1
     */
    private Integer code;

    /**
     * 请求状态码
     */
    private Integer status;
    /**
     * 提示信息.
     */
    private String msg;
}
