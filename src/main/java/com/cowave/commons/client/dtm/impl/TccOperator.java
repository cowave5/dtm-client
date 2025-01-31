package com.cowave.commons.client.dtm.impl;

/**
 *
 * @author shanhuiming
 *
 */
@FunctionalInterface
public interface TccOperator<T extends Dtm> {

    void accept(T t) throws Exception;
}
