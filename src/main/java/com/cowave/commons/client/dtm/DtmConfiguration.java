package com.cowave.commons.client.dtm;

import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 *
 * @author shanhuiming
 *
 */
public class DtmConfiguration {

    @Bean
    public DtmClient dtmClient(DtmService dtmService, @Nullable DataSource dataSource){
        return new DtmClient(dtmService, dataSource);
    }
}
