package com.baymin.restroomapi.ret.handle;


import com.baymin.restroomapi.ret.R;
import com.baymin.restroomapi.ret.enums.ResultEnum;
import com.baymin.restroomapi.ret.exception.MyException;
import com.baymin.restroomapi.ret.model.ResultNoData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Created by baymin
 * 2017-07-10 13:59
 */
@ControllerAdvice
@Component
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler(value = {MyException.class})
    @ResponseBody
    public ResultNoData handle(MyException e) {
        e.printStackTrace();
        log.debug(e.getMessage());
        if (e instanceof MyException) {
            return R.error(e.getCode(), e.getMessage());
        } else {
            log.error("【系统异常】{}", e);
            return R.error(ResultEnum.UNKONW_ERROR);
        }
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultNoData handle(Exception e) {
        e.printStackTrace();
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            return R.error(myException.getCode(), myException.getMessage());
        } else if (e instanceof ServletException) {
            //e.printStackTrace();
            //logger.debug("errrrrrrr",e.getMessage());
            //System.out.println(e.getMessage());
            return R.error(ResultEnum.FAIL_PARS);
        } else if (e instanceof ConstraintViolationException) {

            ConstraintViolationException myException = (ConstraintViolationException) e;
            // build constraint error
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<?> violation : myException.getConstraintViolations()) {

                sb.append(violation.getMessage() + "|");
                // 获得验证失败的类 constraintViolation.getLeafBean()
                // 获得验证失败的值 constraintViolation.getInvalidValue()
                // 获取参数值 constraintViolation.getExecutableParameters()
                // 获得返回值 constraintViolation.getExecutableReturnValue()
            }
            return R.error(ResultEnum.getResultEnumWithParsErr(sb.toString()));
        } else if (e instanceof NumberFormatException) {
            return R.error(ResultEnum.FAIL_COMPANY_ID);
        } else {
            //logger.error("【系统异常】{}", e);
            return R.error(ResultEnum.UNKONW_ERROR);
        }
    }


    /**
     * url参数验证
     *
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
