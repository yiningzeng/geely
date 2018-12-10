package com.baymin.restroomapi.ret;


import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.model.Result;
import com.baymin.restroomapi.ret.model.ResultNoData;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created by baymin
 * 2017-07-10 13:39
 */
@Slf4j
public class R {

    /**
     * 回调接口
     */
    public interface OptionalResult {
        /**
         * 指示Optional类型的数据不为空
         * @param data
         * @return
         */
        Object onTrue(Object data);
        /**
         * 指示Optional类型的数据是 null
         * @return
         */
        Object onFalse();
    }

    /**
     * 回调接口
     */
    public interface OptionalResultTrue {
        /**
         * 指示Optional类型的数据不为空
         * @param data
         * @return
         */
        Object onTrue(Object data);

    }


    /**
     * 自定义函数回调返回
     * @param optional Optional类型的数据
     * @param optionalResult
     * @return
     */
    public static Object callBackRet(Optional optional,OptionalResult optionalResult){
        return optional.map(a->optionalResult.onTrue(a)).orElseGet(()->optionalResult.onFalse());
    }
    /**
     * 自定义带结果是否空值判断返回
     * @param optional 判断是否为空的变量
     * @return
     */
    public static Object ret(Optional optional, ResultEnum resultEnum) {
        return optional.map(a->success(optional.get())).orElseGet(()->error(resultEnum));
    }
    public static Object ret(Optional optional, ResultEnum resultEnum,OptionalResultTrue optionalResultTrue) {
        return optional.map(a->optionalResultTrue.onTrue(a)).orElseGet(()->error(resultEnum));
    }
    /**
     * 自定义带结果是否空值判断返回
     * @param optional 判断是否为空的变量
     * @param resultEnum 错误信息
     * @param isShowData 是否需要显示数据
     * @return
     */
    public static Object ret(Optional optional,ResultEnum resultEnum, boolean isShowData) {
        return optional.map(a->{
            if(isShowData) return success(optional.get());
            else return success();
        }).orElseGet(()->{
            //if(isShowData)return error(resultEnum,optional.get());
            return error(resultEnum);
        });
    }

    /**
     * 成功带data
     *
     * @param object
     * @return
     */
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    /**
     * 成功带data，并带当前的状态值
     * 分页查询 status 表示当前页数
     *
     * @param object
     * @return
     */
    public static Result success(Object object, Integer status) {
        Result result = new Result();
        result.setCode(0);
        result.setStatus(status);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    /**
     * 成功返回,自定义信息+数据
     *
     * @param resultEnum
     * @return
     */
    public static Result success(ResultEnum resultEnum, Object object) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg(resultEnum.getMsg());
        result.setStatus(resultEnum.getCode());
        result.setData(object);
        return result;
    }

    /**
     * 成功无返回值
     *
     * @param
     * @return
     */
    public static ResultNoData success(ResultEnum resultEnum) {
        ResultNoData result = new ResultNoData();
        result.setStatus(resultEnum.getCode());
        result.setCode(0);
        result.setMsg(resultEnum.getMsg());
        return result;
    }



    /**
     * 成功无数据输出
     *
     * @return
     */
    public static Result success() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        return result;
    }

    /**
     * @param resultEnum
     * @return
     */
    public static Result error(ResultEnum resultEnum, Object object) {
        Result result = new Result();
        result.setCode(-1);
        result.setMsg(resultEnum.getMsg());
        result.setStatus(resultEnum.getCode());
        result.setData(object);
        return result;
    }

    /**
     * @param resultEnum
     * @return
     */
    public static ResultNoData error(ResultEnum resultEnum) {

        ResultNoData result = new ResultNoData();
        result.setCode(-1);
        result.setStatus(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        log.info("~~~~~~~~~~~~~error~~~~~~~~~code:{}~msg:{}~~~~~~~~~~~~~~~~~~~~~",result.getStatus(),result.getMsg());
        return result;
    }


    public static ResultNoData error(Integer code, String msg) {
        ResultNoData result = new ResultNoData();
        result.setCode(-1);
        result.setStatus(code);
        result.setMsg(msg);
        log.info("~~~~~~~~~~~~~error~~~~~~~~~code:{}~msg:{}~~~~~~~~~~~~~~~~~~~~~",result.getStatus(),result.getMsg());
        return result;
    }



}
