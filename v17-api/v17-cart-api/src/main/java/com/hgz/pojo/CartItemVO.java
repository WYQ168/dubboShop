package com.hgz.pojo;

import com.hgz.entity.TProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemVO implements Serializable {
    private TProduct product;

    private Integer count;

    private Date updateTime;

}
