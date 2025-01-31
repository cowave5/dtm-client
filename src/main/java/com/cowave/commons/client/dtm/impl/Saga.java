package com.cowave.commons.client.dtm.impl;

import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.dtm.DtmService;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.HttpException;
import com.cowave.commons.client.http.response.HttpResponse;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Saga extends Dtm {
    private static final String ORDERS = "orders";
    private static final String CONCURRENT = "concurrent";

    private final List<String> passthroughHeaders = new ArrayList<>();

    private final Map<String, String> branchHeaders = new HashMap<>();

    private final List<Map<String, String>> steps = new ArrayList<>();

    private final List<String> payloads = new ArrayList<>();

    private Map<String, List<Integer>> orders;

    private long timeoutToFail;

    private long retryInterval;

    private boolean concurrent;

    private String customData;

    private DtmService dtmService;

    public Saga(String gid, DtmService dtmService) {
        super(gid, Type.SAGA, false);
        this.concurrent = false;
        this.dtmService = dtmService;
        this.orders = new HashMap<>();
    }

    /**
     * 添加step步骤
     */
    public void step(String action, String compensate, Object data) {
        try {
            payloads.add(toJson(data));
        } catch (Exception e) {
            throw new HttpException(DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, e, "DTM Saga add step failed");
        }
        Map<String, String> stepMap = new HashMap<>();
        stepMap.put("action", action);
        stepMap.put("compensate", compensate);
        steps.add(stepMap);
    }

    /**
     * 提交事务
     */
    public DtmResponse submit() {
        if (StringUtils.isEmpty(gid)) {
            HttpResponse<DtmResponse> httpResponse = dtmService.newGid();
            HttpAsserts.isTrue(httpResponse.isSuccess(),
                    httpResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Saga acquire gid failed, " + httpResponse.getMessage());
            DtmResponse dtmResponse = httpResponse.getBody();
            HttpAsserts.isTrue(dtmResponse != null && dtmResponse.isSuccess(),
                    DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Saga acquire gid failed");
            this.gid = dtmResponse.getGid();
        }

        addConcurrentContext();
        SagaParam sagaParam = new SagaParam(
                this.gid,
                Type.SAGA,
                this.steps,
                this.payloads,
                this.customData,
                this.waitResult,
                this.timeoutToFail,
                this.retryInterval,
                this.passthroughHeaders,
                this.branchHeaders
        );

        HttpResponse<DtmResponse> httpResponse = dtmService.submit(sagaParam);
        HttpAsserts.isTrue(httpResponse.isSuccess(),
                httpResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Saga " + this.gid + " submit failed, " + httpResponse.getMessage());
        DtmResponse dtmResponse = httpResponse.getBody();
        HttpAsserts.isTrue(dtmResponse != null && dtmResponse.isSuccess(),
                DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Saga " + this.gid + " submit failed");
        dtmResponse.setGid(this.gid);
        return dtmResponse;
    }

    private void addConcurrentContext() {
        if (concurrent) {
            Map<String, Object> data = new HashMap<>();
            data.put(ORDERS, orders);
            data.put(CONCURRENT, true);
            try {
                this.customData = toJson(data);
            } catch (Exception e) {
                throw new HttpException(DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, e, "DTM Saga " + this.gid + " submit failed");
            }
        }
    }

    public Saga concurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }

    public Saga waitResult(boolean waitResult) {
        this.waitResult = waitResult;
        return this;
    }

    public Saga timeoutFail(long timeoutFail) {
        this.timeoutToFail = timeoutFail;
        return this;
    }

    public Saga retryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
        return this;
    }

    public Saga branchOrder(Integer branch, List<Integer> preBranches) {
        this.orders.put(branch.toString(), preBranches);
        return this;
    }

    public Saga branchHeader(String headerName, String headerValue) {
        this.branchHeaders.put(headerName, headerValue);
        return this;
    }

    public Saga passThroughHeader(String headerName) {
        this.passthroughHeaders.add(headerName);
        return this;
    }
}
