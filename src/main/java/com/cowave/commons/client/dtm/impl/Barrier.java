package com.cowave.commons.client.dtm.impl;

import com.cowave.commons.client.dtm.DtmOperator;
import com.cowave.commons.client.dtm.DtmResponse;
import com.cowave.commons.client.http.asserts.HttpException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 *
 * @author shanhuiming
 *
 */
@Slf4j
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Barrier extends Dtm {

    private static final String SQL = "insert ignore into dtm_barrier(trans_type, gid, branch_id, op, barrier_id, reason)values(?, ?, ?, ?, ?, ?)";

    private int barrierId;

    private String op;

    private String branchId;

    public Barrier(@Nonnull BarrierParam param) {
        this.gid = param.getGid();
        this.op = param.getOp();
        this.branchId = param.getBranch_id();
        this.transactionType = Type.parse(param.getTrans_type());
    }

    public void call(DtmOperator<Barrier> operator, DataSource dataSource) throws Exception {
        ++this.barrierId;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            boolean barrierResult = insertBarrier(connection, barrierId, transactionType.getValue(), gid, branchId, op);
            if (barrierResult) {
                if (!operator.accept(this)) {
                    throw new HttpException(DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Barrier operate failed");
                }
                connection.commit();
                connection.setAutoCommit(true);
            }else{
                log.warn("Dtm skipped, gid=" + gid + ", op=" + op + ", branchId=" + branchId);
            }
        } catch (HttpException e) {
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
            throw e;
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
            throw new HttpException(DtmResponse.HTTP_ERROR, DtmResponse.DTM_ERROR, e, "DTM Barrier operate failed");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void call(DtmOperator<Barrier> operator, Connection connection) throws Exception {
        ++this.barrierId;
        try{
            connection.setAutoCommit(false);
            boolean barrierResult = insertBarrier(connection, barrierId, transactionType.getValue(), gid, branchId, op);
            if (barrierResult) {
                if(!operator.accept(this)){
                    throw new HttpException(DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Barrier operate failed");
                }
                connection.commit();
                connection.setAutoCommit(true);
            }else{
                log.warn("Dtm skipped, gid=" + gid + ", op=" + op + ", branchId=" + branchId);
            }
        } catch (HttpException e){
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        } catch (Exception e) {
            if(connection != null){
                connection.rollback();
                connection.setAutoCommit(true);
            }
            throw new HttpException(DtmResponse.HTTP_ERROR, DtmResponse.DTM_ERROR, e, "DTM Barrier operate failed");
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public void callInTransaction(DtmOperator<Barrier> operator, Connection connection) {
        ++this.barrierId;
        try{
            boolean barrierResult = insertBarrier(connection, barrierId, transactionType.getValue(), gid, branchId, op);
            if (barrierResult) {
                if(!operator.accept(this)){
                    throw new HttpException(DtmResponse.HTTP_FAILURE, DtmResponse.DTM_FAILURE, "DTM Barrier operate failed");
                }
            }else{
                log.warn("Dtm skipped, gid=" + gid + ", op=" + op + ", branchId=" + branchId);
            }
        } catch (HttpException e){
            throw e;
        } catch (Exception e) {
            throw new HttpException(DtmResponse.HTTP_ERROR, DtmResponse.DTM_ERROR, e, "DTM Barrier operate failed");
        }
    }

    protected boolean insertBarrier(Connection connection, int barrierId, String type, String gid, String branchId, String op) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, gid);
            preparedStatement.setString(3, branchId);
            preparedStatement.setString(4, op);
            preparedStatement.setString(5, String.format("%02d", barrierId));
            preparedStatement.setString(6, op);
            // 过滤重复执行
            if (preparedStatement.executeUpdate() == 0) {
                return false;
            }

            // 执行cancel，必须要先执行过try
            if ("cancel".equals(op)) {
                if(transactionType.equals(Type.TCC)){
                    preparedStatement.setString(4, "try");
                }else if(transactionType.equals(Type.SAGA)){
                    preparedStatement.setString(4, "compensate");
                }else{
                    preparedStatement.setString(4, "rollback");
                }
                // 插入成功肯定未执行过
                if (preparedStatement.executeUpdate() > 0) {
                    return false;
                }
            }
        } finally {
            if (Objects.nonNull(preparedStatement)) {
                preparedStatement.close();
            }
        }
        return true;
    }
}
