package com.hgz.commons.base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBean<T> implements Serializable {
    //返回的状态码
    private Integer statusCode;
    //返回的数据
    private T data;
}
