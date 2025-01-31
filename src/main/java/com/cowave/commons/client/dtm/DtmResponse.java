package com.cowave.commons.client.dtm;

import com.cowave.commons.client.http.response.HttpResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author shanhuiming
 *
 */
@Data
@NoArgsConstructor
public class DtmResponse {

    /**
     * 成功
     */
    public static final int HTTP_SUCCESS = 200;

    /**
     * 失败，不再重试
     */
    public static final int HTTP_FAILURE = 409;

    /**
     * 进行中，固定间隔重试
     */
    public static final int HTTP_ONGOING = 425;

    /**
     * 异常，指数退避重试
     */
    public static final int HTTP_ERROR = 500;

    public static final String DTM_SUCCESS = "SUCCESS";

    public static final String DTM_FAILURE = "FAILURE";

    public static final String DTM_ONGOING = "ONGOING";

    public static final String DTM_ERROR = "ERROR";

    /**
     * dtm_result
     */
    @JsonProperty("dtm_result")
    private String dtmResult;

    /**
     * gid
     */
    @JsonProperty("gid")
    private String gid;

    /**
     * detail
     */
    @JsonProperty("detail")
    private String detail;

    public DtmResponse(String dtmResult, String detail){
        this.dtmResult = dtmResult;
        this.detail = detail;
    }

    public boolean isSuccess(){
        return DTM_SUCCESS.equals(dtmResult);
    }

    public static DtmResponse success(){
        return new DtmResponse(DTM_SUCCESS, null);
    }

    public static DtmResponse success(String detail){
        return  new DtmResponse(DTM_SUCCESS, detail);
    }

    public static DtmResponse failure(){
        return  new DtmResponse(DTM_FAILURE, null);
    }

    public static DtmResponse failure(String detail){
        return  new DtmResponse(DTM_FAILURE, detail);
    }

    public static DtmResponse ongoing(){
        return new DtmResponse(DTM_ONGOING, null);
    }

    public static DtmResponse ongoing(String detail){
        return new DtmResponse(DTM_ONGOING, detail);
    }

    public static DtmResponse error(){
        return new DtmResponse(DTM_ERROR, null);
    }

    public static DtmResponse error(String detail){
        return new DtmResponse(DTM_ERROR, detail);
    }

    public static HttpResponse<DtmResponse> httpSuccess(){
        return new HttpResponse<>(HTTP_SUCCESS, null, success());
    }

    public static HttpResponse<DtmResponse> httpSuccess(String detail){
        return new HttpResponse<>(HTTP_SUCCESS, null, success(detail), detail);
    }

    public static HttpResponse<DtmResponse> httpFailure(){
        return new HttpResponse<>(HTTP_FAILURE, null, failure());
    }

    public static HttpResponse<DtmResponse> httpFailure(String detail){
        return new HttpResponse<>(HTTP_FAILURE, null, failure(detail), detail);
    }

    public static HttpResponse<DtmResponse> httpOngoing(){
        return new HttpResponse<>(HTTP_ONGOING, null, ongoing());
    }

    public static HttpResponse<DtmResponse> httpOngoing(String detail){
        return new HttpResponse<>(HTTP_ONGOING, null, ongoing(detail), detail);
    }

    public static HttpResponse<DtmResponse> httpError(){
        return new HttpResponse<>(HTTP_ERROR, null, error());
    }

    public static HttpResponse<DtmResponse> httpError(String detail){
        return new HttpResponse<>(HTTP_ERROR, null, error(detail), detail);
    }
}
