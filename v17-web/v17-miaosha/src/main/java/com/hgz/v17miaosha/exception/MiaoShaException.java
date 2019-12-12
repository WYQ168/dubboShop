package com.hgz.v17miaosha.exception;

/*
* 一般自定义异常都是继承RuntimeException
* */
public class MiaoShaException extends RuntimeException {

    public MiaoShaException(String message){
        super(message);
    }

}
