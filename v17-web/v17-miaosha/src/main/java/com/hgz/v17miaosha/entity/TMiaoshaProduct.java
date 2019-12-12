package com.hgz.v17miaosha.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class TMiaoshaProduct implements Serializable {
    private Long id;

    private Long productId;

    private String productName;

    private String productImages;

    private Integer productTypeId;

    private String productTypeName;

    private Long productPrice;

    private Long salePrice;

    private Integer count;

    private Date startTime;

    private Date endTime;

    private String status;

    private Boolean check;

    private Boolean flag;

    private Date createTime;

    private Date updateTime;

    private Long createUser;

    private Long updateUser;
}