package com.cowave.commons.client.dtm.controller;

import com.cowave.commons.client.dtm.DtmClient;
import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.dtm.impl.BarrierParam;
import com.cowave.commons.client.dtm.model.TransReq;
import com.cowave.commons.client.http.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BarrierController {

    private final DtmClient dtmClient;

    @RequestMapping("barrierTransOutTry")
    public HttpResponse<DtmResponse> transOutTry(BarrierParam barrierParam, @RequestBody TransReq transReq) throws Exception {
        return dtmClient.barrier(barrierParam, (barrier) -> this.transOutPrepare(transReq));
    }

    @RequestMapping("barrierTransOutConfirm")
    public HttpResponse<DtmResponse> transOutConfirm(BarrierParam barrierParam, @RequestBody TransReq transReq) throws Exception {
        return dtmClient.barrier(barrierParam, (barrier) -> this.transOutSubmit(transReq));
    }

    @RequestMapping("barrierTransOutCancel")
    public HttpResponse<DtmResponse> transOutCancel(BarrierParam barrierParam, @RequestBody TransReq transReq) throws Exception {
        return dtmClient.barrier(barrierParam, (barrier) -> this.transOutCancel(transReq));
    }

    @RequestMapping("barrierTransInTry")
    public HttpResponse<DtmResponse> transInTry(BarrierParam barrierParam, @RequestBody TransReq transReq) throws Exception {
        return dtmClient.barrier(barrierParam, (barrier) -> this.transInPrepare(transReq));
    }

    @RequestMapping("barrierTransInConfirm")
    public HttpResponse<DtmResponse> transInConfirm(BarrierParam barrierParam, @RequestBody TransReq transReq) throws Exception {
        return dtmClient.barrier(barrierParam, (barrier) -> this.transInSubmit(transReq));
    }

    @RequestMapping("barrierTransInCancel")
    public HttpResponse<DtmResponse> transInCancel(BarrierParam barrierParam, @RequestBody TransReq transReq) throws Exception {
        return dtmClient.barrier(barrierParam, (barrier) -> this.transInCancel(transReq));
    }

    private boolean transOutPrepare(TransReq transReq) {
        log.info("user[{}] 转出准备 {}", transReq.getUserId(), transReq.getAmount());
        return true;
    }

    public boolean transOutSubmit(TransReq transReq) {
        log.info("user[{}] 转出提交 {}", transReq.getUserId(), transReq.getAmount());
        return true;
    }

    public boolean transOutCancel(TransReq transReq) {
        log.info("user[{}] 转出回滚 {}", transReq.getUserId(), transReq.getAmount());
        return true;
    }

    private boolean transInPrepare(TransReq transReq) {
        log.info("user[{}] 转入准备 {}", transReq.getUserId(), transReq.getAmount());
        return true;
    }

    private boolean transInSubmit(TransReq transReq) {
        log.info("user[{}] 转入提交 {}", transReq.getUserId(), transReq.getAmount());
        return transReq.getAmount() <= 1000;
    }

    private boolean transInCancel(TransReq transReq) {
        log.info("user[{}] 转入回滚 {}", transReq.getUserId(), transReq.getAmount());
        return true;
    }
}
