package com.hgz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable,Comparable<CartItem> {
    private Long productId;

    private Integer count;

    private Date updateTime;

    public int compareTo(CartItem o) {
        int result = (int) (o.getUpdateTime().getTime() - this.getUpdateTime().getTime());
        if(result == 0){
            return -1;
        }
        return result;
    }
}
