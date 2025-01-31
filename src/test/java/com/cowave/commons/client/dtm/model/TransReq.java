package com.cowave.commons.client.dtm.model;

import lombok.Data;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class TransReq {

    /**
     * 用户id
     */
    private int userId;

    /**
     * 转入/转出金额
     */
    private int amount;

    /**
     * jackson 必须使用无参构造
     */
    public TransReq(){
    }

    public TransReq(int userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
