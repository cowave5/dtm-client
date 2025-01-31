package com.cowave.commons.client.dtm;

import com.cowave.commons.client.dtm.impl.sql.MysqlProvider;
import com.cowave.commons.client.dtm.impl.sql.SqlProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;
import javax.sql.DataSource;

/**
 *
 * @author shanhuiming
 *
 */
public class DtmConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public SqlProvider sqlProvider(){
        return new MysqlProvider();
    }

    @Bean
    public DtmClient dtmClient(DtmService dtmService, SqlProvider sqlProvider, @Nullable DataSource dataSource){
        return new DtmClient(dtmService, dataSource, sqlProvider);
    }
}
