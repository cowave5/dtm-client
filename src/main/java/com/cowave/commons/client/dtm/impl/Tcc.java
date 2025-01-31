package com.cowave.commons.client.dtm.impl;

import com.cowave.commons.client.dtm.DtmOperator;
import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.dtm.DtmService;
import com.cowave.commons.client.http.asserts.HttpAsserts;
import com.cowave.commons.client.http.asserts.HttpException;
import com.cowave.commons.client.http.response.HttpResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@NoArgsConstructor
public class Tcc extends Dtm {

    private static final int MAX_BRANCH_ID = 99;

    private String branchPrefix;

    private int subBranchId;

    private DtmService dtmService;

    public Tcc(String branchPrefix, String gid, DtmService dtmService) {
        super(gid, Type.TCC, false);
        this.dtmService = dtmService;
        this.branchPrefix = branchPrefix;
        if(StringUtils.isBlank(branchPrefix)){
            this.branchPrefix = "Tcc-branch-";
        }
    }

    /**
     * 开启Tcc事务
     */
    public DtmResponse prepare(DtmOperator<Tcc> operator) throws Exception {
        // gid
        if (StringUtils.isEmpty(gid)) {
            HttpResponse<DtmResponse> httpResponse = dtmService.newGid();
            HttpAsserts.isTrue(httpResponse.isSuccess(),
                    httpResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Tcc acquire gid failed, " + httpResponse.getMessage());
            DtmResponse dtmResult = httpResponse.getBody();
            HttpAsserts.isTrue(dtmResult != null && dtmResult.isSuccess(),
                    DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Tcc acquire gid failed");
            this.gid = dtmResult.getGid();
        }

        // prepare
        DtmParam tccParam = new DtmParam(gid, Type.TCC);
        HttpResponse<DtmResponse> prepareResponse = dtmService.prepare(tccParam);
        HttpAsserts.isTrue(prepareResponse.isSuccess(),
                prepareResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Tcc " + gid + " prepare failed, " + prepareResponse.getMessage());
        DtmResponse prepareResult = prepareResponse.getBody();
        HttpAsserts.isTrue(prepareResult != null && prepareResult.isSuccess(),
                DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Tcc " + gid + " prepare failed");

        // operate
        if(!operator.accept(this)){
            throw new HttpException(DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Tcc " + gid + " register failed");
        }
        log.info("DTM Tcc " + gid + " register transaction");

        // submit
        HttpResponse<DtmResponse> submitResponse = dtmService.submit(tccParam);
        if(submitResponse.isFailed()){
            dtmService.abort(tccParam);
            throw new HttpException(submitResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Tcc " + gid + " submit failed, " + submitResponse.getMessage());
        }
        DtmResponse submitResult = submitResponse.getBody();
        HttpAsserts.isTrue(submitResult != null && submitResult.isSuccess(),
                DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Tcc " + gid + " submit failed");

        submitResult.setGid(gid);
        return submitResult;
    }

    public String branch(String tryUrl, String confirmUrl, String cancelUrl, Object requestBody) throws Exception {
        String branchId = genBranchId();
        TccParam operatorParam = new TccParam(
                this.gid,
                Type.TCC,
                branchId,
                confirmUrl,
                cancelUrl,
                toJson(requestBody),
                "prepared"
        );

        // register
        HttpResponse<DtmResponse> registerResponse = dtmService.registerBranch(operatorParam);
        HttpAsserts.isTrue(registerResponse.isSuccess(),
                registerResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Tcc " + gid + " branch register failed, " + registerResponse.getMessage());
        DtmResponse registerResult = registerResponse.getBody();
        HttpAsserts.isTrue(registerResult != null && registerResult.isSuccess(),
                DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Tcc " + gid + " branch register failed");

        // try
        Map<String, String> branchParam = new HashMap<>();
        branchParam.put("gid", this.gid);
        branchParam.put("branch_id", branchId);
        branchParam.put("op", "try");
        branchParam.put("trans_type", Type.TCC.getValue());
        HttpResponse<String> httpResponse = dtmService.businessPost(tryUrl, branchParam, requestBody);
        HttpAsserts.isTrue(httpResponse.getStatusCodeValue() < 400,
                httpResponse.getStatusCodeValue(), DtmResponse.DTM_ERROR, "DTM Tcc " + gid + " branch try failed, " + httpResponse.getMessage());

        return httpResponse.getBody();
    }

    private String genBranchId() throws Exception {
        if (this.subBranchId >= MAX_BRANCH_ID) {
            throw new Exception("branch id is larger than 99");
        }
        this.subBranchId++;
        return this.branchPrefix + String.format("%02d", this.subBranchId);
    }
}
