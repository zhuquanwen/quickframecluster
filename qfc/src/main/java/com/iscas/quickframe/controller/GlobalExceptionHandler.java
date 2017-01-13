package com.iscas.quickframe.controller;

import javax.validation.ValidationException;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.iscas.quickframe.entity.ResponseEntity;

/**
*@auhor:zhuquanwen
*@date:2017年1月3日
*@desc:全局异常处理
*/
@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @ExceptionHandler(value=ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handle(ValidationException exception) {
       
        return new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数有误");
    }
    @ExceptionHandler(value=MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handle(MissingServletRequestParameterException exception) {
       
        return new ResponseEntity(HttpStatus.BAD_REQUEST.value(), "请求参数有误");
    }
}
