package com.cowave.commons.client.dtm.controller;

import com.cowave.commons.client.dtm.DtmClient;
import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.dtm.impl.Saga;
import com.cowave.commons.client.http.asserts.HttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 *
 * @author shanhuiming
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(("/api"))
public class SagaController {

    private final DtmClient dtmClient;

    @RequestMapping("/saga")
    public DtmResponse saga() throws HttpException {
        Saga saga = dtmClient.saga(UUID.randomUUID().toString()).waitResult(true);
        saga.step("http://localhost:8081/api/TransOut", "http://localhost:8081/api/TransOutCompensate", "");
        saga.step("http://localhost:8081/api/TransIn", "http://localhost:8081/api/TransInCompensate", "");
        return saga.submit();
    }
}
