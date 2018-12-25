package com.baymin.restroomapi.ret.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by baymin
 * 2017-07-10 14:23
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    FAIL_ACTION_MESSAGE(101,"操作失败"),
    LIST_EMPTY(102,"数据为空"),
    UPDATE_EMPTY(103,"未找到更新的数据"),

    NO_LIST(1101,"数据为空"),

    FAIL_ADD_USER(1001,"新增用户失败"),
    FAIL_ADD_USER_ALLREADY_EXIT(1002,"用户编号已经存在"),
    FAIL_USER_LOGIN(1102,"选择岗位不存在"),

    FAIL_ADD_RESTROOM(1011,"新增厕所失败"),
    FAIL_ADD_RESTROOM_ALLREADY_EXIT(1012,"该区域已经存在"),
    FAIL_DO_NO_RESTROOM(1013,"该厕所不存在"),
    FAIL_DO_NO_DEVICE(1014,"该设备不存在"),
    FAIL_ALLREADY_HAVE_DEVICE(1015,"该设备已存在"),
    FAIL_DEVICE_ERR(1016,"请确认设备已正确添加"),
    FAIL_DEVICE_CAMERA_PUSHERR(1017,"直播失败，请重新打开"),

    FAIL_INSERT_ERROR_ALLREADY_CARMODEL(1201,"已经存在该车型,更新操作失败"),
    FAIL_INSERT_ERROR_ALLREADY_RULE(1202,"已经存在的命名规则"),
    FAIL_NO_WORKPOST(1203,"选择岗位不存在"),
    FAIL_NO_WORKPOSTTASK(1204,"选择的作业不存在"),
    FAIL_NO_WORKPOSTTASK2(1205,"该车型下作业为空"),
    FAIL_NO_WORKPOSTACTION(1206,"该作业下无可执行的动作"),
    FAIL_NO_MODEL(1207,"未找到识别的模型，请确认提交的参数"),
    FAIL_ALREADY_DONE(1208,"该作业已经执行完成"),


    FAIL_WORK_POST_ACTIONS_NO_CARMODELS(2001,"未找到匹配的车型，请重新选择"),
    FAIL_WORK_POST_ACTIONS_NO_ACTIONS(2002,"作业列表为空，请重新编辑"),

    FAIL_WORKING_NO_ACTION(3001,"未找到动作，请确认参数"),
    FAIL_WORKING_NO_CAR_WORKING(3002,"未找到VIN码的车辆"),
    FAIL_WORKING_NO_HTTP(3003,"算法服务错误"),

    SUCCESS_UPDATE_STATUS(0, "用户状态修改成功"),
    SUCCESS_CODE(10, "校验成功"),
    FQAIL_CODE(11, "验证码有误"),
    USER_UN_REGIST(110, "用户不存在"),
    USER_ERR_PASS(111, "密码错误"),
    FAIL_UPDATE_STATUS(112, "用户状态修改失败"),
    USER_FAIL_FORGOT_PASS(113, "认证失败"),
    USER_FAIL_FORGOT_PASS_MOBILE(114, "手机号不匹配"),
    FAIL_SEND_MSG(115, "验证码发送失败"),

    FAIL_PARS(2001, "参数有误"),
    FAIL_COMPANY_ID(2002, "公司参数有误"),
    FAIL_AUTH(2101, "没有权限|想太多了，同志"),

    //region 3000开始
    FAIL_IM_CONFIG(3001, "通信配置问题"),
    FAIL_IM_NO_ADMIN(3002, "通信管理员账号有误"),
    FAIL_IM_SIG(3003, "更新签名失败"),
    //endregion

    FAIL_AGORA_SAVE(4001, "更新记录失败"),


    FAIL_HEAD_SAVE(5001, "头像更新失败"),;


    private Integer code;

    private String msg;

    public static ResultEnum getResultEnumWithParsErr(String msg) {
        FAIL_PARS.msg = msg;
        return FAIL_PARS;
    }

}
