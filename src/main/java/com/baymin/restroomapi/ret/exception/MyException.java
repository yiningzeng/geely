package com.baymin.restroomapi.ret.exception;


import com.baymin.restroomapi.ret.enums.ResultEnum;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletException;

/**
 * Created by baymin
 * 2017-07-10 14:05
 */
@Getter
@Setter
public class MyException extends ServletException {

    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
    public MyException() {
    }

    public MyException(String message) {
        super(message);
    }
}
