package com.cowave.commons.client.dtm.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@Getter
public class SagaParam extends DtmParam {

    @JsonProperty("steps")
    private final List<Map<String, String>> steps;

    @JsonProperty("payloads")
    private final List<String> payloads;

    @JsonProperty("custom_data")
    private final String customData;

    @JsonProperty("wait_result")
    private final boolean waitResult;

    @JsonProperty("timeout_to_fail")
    private final long timeoutToFail;

    @JsonProperty("retry_interval")
    private final long retryInterval;

    @JsonProperty("passthrough_headers")
    private final List<String> passThroughHeaders;

    @JsonProperty("branch_headers")
    private final Map<String, String> branchHeaders;

    public SagaParam(String gid, Dtm.Type type, List<Map<String, String>> steps,
                     List<String> payloads, String customData,
                     boolean waitResult, long timeoutToFail, long retryInterval,
                     List<String> passThroughHeaders, Map<String, String> branchHeaders) {
        super(gid, type);
        setSubType(type.getValue());
        this.steps = steps;
        this.payloads = payloads;
        this.customData = customData;
        this.waitResult = waitResult;
        this.timeoutToFail = timeoutToFail;
        this.retryInterval = retryInterval;
        this.passThroughHeaders = passThroughHeaders;
        this.branchHeaders = branchHeaders;
    }
}
