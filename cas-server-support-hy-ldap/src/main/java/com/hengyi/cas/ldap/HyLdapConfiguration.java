package com.hengyi.cas.ldap;

import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.LdapAuthenticationHandler;
import org.apereo.cas.authentication.handler.PrincipalNameTransformer;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.config.LdapAuthenticationConfiguration;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * @author jzb 2018-01-30
 */
@Configuration("hyLdapAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class HyLdapConfiguration extends LdapAuthenticationConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HyLdapConfiguration.class);

    @Autowired
    private OaUserRepository oaUserRepository;

    @Autowired
    @Qualifier("hyPrincipalNameTransformer")
    private PrincipalNameTransformer principalNameTransformer;

    @Autowired
    @Qualifier("hyPrincipalResolver")
    private PrincipalResolver principalResolver;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Autowired
    @Qualifier("hyPrincipalFactory")
    private PrincipalFactory principalFactory;

    @Bean("ldapPrincipalFactory")
    @Override
    public PrincipalFactory ldapPrincipalFactory() {
        return principalFactory;
    }

    /**
     * 注意返回的顺序，先进行标准的 AD 验证
     * 失败后才是 oaId 的 MD5 验证
     */
    @Bean
    public Collection<AuthenticationHandler> hyLdapAuthenticationHandlers() {
        final Collection<AuthenticationHandler> handlers = super.ldapAuthenticationHandlers();
        final HyLdapAuthenticationHandler handler = new HyLdapAuthenticationHandler(servicesManager, principalFactory, oaUserRepository);
        handlers.add(handler);
        return handlers;
    }

    /**
     * 注意 Bean name，需要和 {@link LdapAuthenticationConfiguration} 中的一致
     * 这样可以覆盖，用自己的方式生成，并设置 principalNameTransformer
     *
     * @return
     */
    @Bean(name = "ldapAuthenticationEventExecutionPlanConfigurer")
    public AuthenticationEventExecutionPlanConfigurer hyLdapAuthenticationEventExecutionPlanConfigurer() {
        return plan -> hyLdapAuthenticationHandlers().forEach(handler -> {
            log.info("Registering LDAP authentication for [{}]", handler.getName());
            if (handler instanceof LdapAuthenticationHandler) {
                LdapAuthenticationHandler ldapAuthenticationHandler = (LdapAuthenticationHandler) handler;
                /**
                 * 默认的 id 需要转换
                 * 如果用 oaId 登录，需要转换为 hrId
                 */
                ldapAuthenticationHandler.setPrincipalNameTransformer(principalNameTransformer);
            }
            plan.registerAuthenticationHandlerWithPrincipalResolver(handler, principalResolver);
        });
    }

}
