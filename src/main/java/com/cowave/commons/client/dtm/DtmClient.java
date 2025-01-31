package com.cowave.commons.client.dtm;

import com.cowave.commons.client.dtm.impl.Barrier;
import com.cowave.commons.client.dtm.impl.BarrierParam;
import com.cowave.commons.client.dtm.impl.Saga;
import com.cowave.commons.client.dtm.impl.Tcc;
import com.cowave.commons.client.http.response.HttpResponse;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 *
 * @author shanhuiming
 *
 */
public class DtmClient {

    private final DtmService dtmService;

    private final DataSource dataSource;

    public DtmClient(DtmService dtmService, DataSource dataSource){
        this.dtmService = dtmService;
        this.dataSource = dataSource;
    }

    /**
     * saga
     */
    public Saga saga() {
        return new Saga(null, dtmService);
    }

    /**
     * 创建saga，指定gid
     */
    public Saga saga(String gid) {
        return new Saga(gid, dtmService);
    }

    /**
     * 创建tcc
     */
    public DtmResponse tcc(DtmOperator<Tcc> function) throws Exception {
        return new Tcc("",null, dtmService).prepare(function);
    }

    /**
     * 创建tcc，指定gid
     */
    public DtmResponse tcc(String gid, DtmOperator<Tcc> function) throws Exception {
        return new Tcc("", gid, dtmService).prepare(function);
    }

    /**
     * 创建tcc
     */
    public DtmResponse tcc(String gid, DtmOperator<Tcc> function, String branchPrefix) throws Exception {
        return new Tcc(branchPrefix,null, dtmService).prepare(function);
    }

    public HttpResponse<DtmResponse> barrier(BarrierParam barrierParam, DtmOperator<Barrier> operator) throws Exception {
        new Barrier(barrierParam).call(operator, dataSource);
        return DtmResponse.httpSuccess();
    }

    public HttpResponse<DtmResponse> barrier(BarrierParam barrierParam, DtmOperator<Barrier> operator, DataSource dataSource) throws Exception {
        new Barrier(barrierParam).call(operator, dataSource);
        return DtmResponse.httpSuccess();
    }

    public HttpResponse<DtmResponse> barrier(BarrierParam barrierParam, DtmOperator<Barrier> operator, Connection connection) throws Exception {
        new Barrier(barrierParam).call(operator, connection);
        return DtmResponse.httpSuccess();
    }

    public HttpResponse<DtmResponse> barrierInTransaction(BarrierParam barrierParam, DtmOperator<Barrier> operator, Connection connection) throws Exception {
        new Barrier(barrierParam).callInTransaction(operator, connection);
        return DtmResponse.httpSuccess();
    }
}
