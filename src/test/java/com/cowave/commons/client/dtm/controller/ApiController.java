package com.cowave.commons.client.dtm.controller;

import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    // saga
    @RequestMapping("TransOut")
    public HttpResponse<DtmResponse> transOut() {
        return DtmResponse.httpSuccess();
    }

    // saga
    @RequestMapping("TransIn")
    public HttpResponse<DtmResponse> transIn() {
        return DtmResponse.httpSuccess();
    }

    // saga
    @RequestMapping("TransOutCompensate")
    public HttpResponse<DtmResponse> transOutCompensate() {
        return DtmResponse.httpSuccess();
    }

    // saga
    @RequestMapping("TransInCompensate")
    public HttpResponse<DtmResponse> transInCompensate() {
        return DtmResponse.httpSuccess();
    }

    // tcc
    @RequestMapping("TransOutTry")
    public String transOutTry() {
        return "succ";
    }

    // tcc
    @RequestMapping("TransOutConfirm")
    public HttpResponse<DtmResponse> transOutConfirm() {
        return DtmResponse.httpSuccess();
    }

    // tcc
    @RequestMapping("TransOutCancel")
    public HttpResponse<DtmResponse> transOutCancel() {
        return DtmResponse.httpSuccess();
    }

    // tcc
    @RequestMapping("TransInTry")
    public HttpResponse<DtmResponse> transInTry() {
        return DtmResponse.httpSuccess();
    }

    // tcc
    @RequestMapping("TransInConfirm")
    public HttpResponse<DtmResponse> transInConfirm() {
        return DtmResponse.httpSuccess();
    }

    // tcc
    @RequestMapping("TransInCancel")
    public HttpResponse<DtmResponse> transInCancel() {
        return DtmResponse.httpSuccess();
    }
}
