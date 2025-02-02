package com.cowave.commons.client.dtm.controller;

import com.cowave.commons.client.dtm.DtmClient;
import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.dtm.impl.Tcc;
import com.cowave.commons.client.dtm.model.TransReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping(("/api"))
@RestController
public class TccController {

    private final DtmClient dtmClient;

    @RequestMapping("/tcc")
    public DtmResponse tcc() throws Exception {
        return dtmClient.tcc(UUID.randomUUID().toString(), this::tccBranch);
    }

    public boolean tccBranch(Tcc tcc) throws Exception {
        String outResponse = tcc.branch(
                "http://localhost:8081/api/TransOutTry",
                "http://localhost:8081/api/TransOutConfirm",
                "http://localhost:8081/api/TransOutCancel",
                "");
        log.info("tcc branch out: " + outResponse);

        String inResponse = tcc.branch(
                "http://localhost:8081/api/TransInTry",
                "http://localhost:8081/api/TransInConfirm",
                "http://localhost:8081/api/TransInCancel",
                "");
        log.info("tcc branch in: " + inResponse);
        return false;
    }

    @RequestMapping("tcc/barrier")
    public DtmResponse tccBarrier() throws Exception {
        return dtmClient.tcc(UUID.randomUUID().toString(), this::barrierBranch);
    }

    public boolean barrierBranch(Tcc tcc) throws Exception {
        String outResponse = tcc.branch(
                "http://localhost:8081/api/barrierTransOutTry",
                "http://localhost:8081/api/barrierTransOutConfirm",
                "http://localhost:8081/api/barrierTransOutCancel",
                new TransReq(1, -30));
        log.info("tcc branch out: " + outResponse);

        String inResponse = tcc.branch(
                "http://localhost:8081/api/barrierTransInTry",
                "http://localhost:8081/api/barrierTransInConfirm",
                "http://localhost:8081/api/barrierTransInCancel",
                new TransReq(2, 30));
        log.info("tcc branch in: " + inResponse);
        return true;
    }

    @RequestMapping("tcc/barrier/error")
    public DtmResponse tccBarrierError() throws Exception {
        return dtmClient.tcc(UUID.randomUUID().toString(), this::barrierBranchError);
    }

    public boolean barrierBranchError(Tcc tcc) throws Exception {
        String outResponse = tcc.branch(
                "http://localhost:8081/api/barrierTransOutTry",
                "http://localhost:8081/api/barrierTransOutConfirm",
                "http://localhost:8081/api/barrierTransOutCancel",
                new TransReq(1, -100000));
        log.info("tcc branch out: " + outResponse);

        String inResponse = tcc.branch(
                "http://localhost:8081/api/barrierTransInTry",
                "http://localhost:8081/api/barrierTransInConfirm",
                "http://localhost:8081/api/barrierTransInCancel",
                new TransReq(2, 100000));
        log.info("tcc branch in: " + inResponse);
        return true;
    }
}
