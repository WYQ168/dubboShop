package com.hgz.v17miaosha.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean<T> implements Serializable {
    private Integer statusCode;
    private T data;
}
