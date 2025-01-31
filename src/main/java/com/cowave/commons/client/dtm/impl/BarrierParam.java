package com.cowave.commons.client.dtm.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
