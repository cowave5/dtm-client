package com.cowave.commons.client.dtm.impl;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author shanhuiming
 *
 */
@Getter
@Setter
public class BarrierParam {

    /**
     * gid
     */
    private String gid;

    /**
     * branch id
     */
    private String branch_id;

    /**
     * trans type
     */
    private String trans_type;

    /**
     * operator
     */
    private String op;

    private String reason;
}
