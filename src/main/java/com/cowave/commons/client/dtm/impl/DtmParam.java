package com.cowave.commons.client.dtm.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "subType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TccParam.class, name = "tcc"),
        @JsonSubTypes.Type(value = SagaParam.class, name = "saga")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtmParam {

    /**
     * gid
     */
    @JsonProperty("gid")
    private String gid;

    /**
     * trans type string value
     */
    @JsonProperty("trans_type")
    private String transType;

    /**
     * this attribute just for mark different sub class for encode/decode.
     */
    @JsonProperty("subType")
    private String subType;

    public DtmParam(String gid, Dtm.Type type) {
        this.gid = gid;
        this.transType = type.getValue();
    }
}
