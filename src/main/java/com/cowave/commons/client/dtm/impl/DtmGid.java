package com.cowave.commons.client.dtm.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.cowave.commons.client.dtm.DtmResponse.DTM_SUCCESS;

/**
 *
 * @author shanhuiming
 *
 */
@Data
public class DtmGid {

    @JsonProperty("dtm_result")
    private String dtmResult;

    private String gid;

    public boolean dtmSuccess(){
        return DTM_SUCCESS.equals(dtmResult);
    }
}
