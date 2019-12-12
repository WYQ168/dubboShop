package com.hgz.vo;
import	java.util.Date;

import com.hgz.entity.TProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TProductVO implements Serializable {

    private TProduct product;
    private String productDesc;

}
