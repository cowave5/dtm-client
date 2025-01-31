package com.cowave.commons.client.dtm.impl;

/**
 *
 * @author shanhuiming
 *
 */
@FunctionalInterface
public interface BarrierOperator<T extends Dtm> {

    boolean accept(T t) throws Exception;
}
