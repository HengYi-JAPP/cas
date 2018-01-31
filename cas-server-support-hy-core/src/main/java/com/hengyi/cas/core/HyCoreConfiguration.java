package com.hengyi.cas.core;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author jzb 2018-01-30
 */
@Configuration("hyCoreConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@ImportResource("classpath:/applicationContext.xml")
public class HyCoreConfiguration {
    @Autowired
    private CasConfigurationProperties casProperties;
}