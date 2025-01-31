package com.cowave.commons.client.dtm;

import com.cowave.commons.client.dtm.impl.Dtm;

/**
 *
 * @author shanhuiming
 *
 */
@FunctionalInterface
public interface DtmOperator<T extends Dtm> {

    boolean accept(T t) throws Exception;
}
