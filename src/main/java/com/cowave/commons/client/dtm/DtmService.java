package com.cowave.commons.client.dtm;

import com.cowave.commons.client.dtm.impl.DtmGid;
import com.cowave.commons.client.dtm.impl.DtmParam;
import com.cowave.commons.client.http.annotation.*;
import com.cowave.commons.client.http.response.HttpResponse;

import java.util.Map;

/**
 *
 * @author shanhuiming
 *
 */
@HttpClient(url = "${spring.dtm.address}", readTimeoutStr = "${spring.dtm.readTimeout:10000}", connectTimeoutStr = "${spring.dtm.connectTimeout:60000}")
public interface DtmService {

    @HttpLine("GET /api/dtmsvr/newGid")
    HttpResponse<DtmGid> newGid();

    @HttpHeaders("Content-Type: application/json")
    @HttpLine("POST /api/dtmsvr/submit")
    HttpResponse<DtmResponse> submit(DtmParam dtmParam);

    @HttpHeaders("Content-Type: application/json")
    @HttpLine("POST /api/dtmsvr/prepare")
    HttpResponse<DtmResponse> prepare(DtmParam dtmParam);

    @HttpHeaders("Content-Type: application/json")
    @HttpLine("POST /api/dtmsvr/abort")
    HttpResponse<DtmResponse> abort(DtmParam dtmParam);

    @HttpHeaders("Content-Type: application/json")
    @HttpLine("POST /api/dtmsvr/registerBranch")
    HttpResponse<DtmResponse> registerBranch(DtmParam dtmParam);

    @HttpHeaders("Content-Type: application/json")
    @HttpLine("POST ")
    HttpResponse<String> businessPost(@HttpHost String host, @HttpParamMap Map<String, String> paramMap, Object body);
}
