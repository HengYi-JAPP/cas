package com.hengyi.cas.webflow;

import com.google.common.collect.Maps;
import com.hengyi.cas.webflow.provider.HyWebflowAuthenticationAction;
import com.hengyi.cas.webflow.provider.HyWebflowAuthenticationHandler;
import com.hengyi.cas.webflow.provider.HyWebflowConfigurer;
import com.hengyi.cas.webflow.util.HyWebflowCredentialBuilder;
import com.hengyi.cas.webflow.util.HyWeixinQyCredentialBuilder;
import com.hengyi.cas.webflow.util.HyWeixinYtCredentialBuilder;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.adaptive.AdaptiveAuthenticationPolicy;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.resolver.CasDelegatingWebflowEventResolver;
import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author jzb 2018-01-30
 */
@Configuration("hyWebflowAuthenticationWebflowConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class HyWebflowAuthenticationConfiguration {
    @NotNull
    @Value("${hy_weixin_qy.corpid}")
    private String hy_weixin_qy_corpid;
    @NotNull
    @Value("${hy_weixin_qy.corpsecret}")
    private String hy_weixin_qy_corpsecret;

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("hyPrincipalFactory")
    private PrincipalFactory principalFactory;

    @Autowired
    @Qualifier("hyPrincipalResolver")
    private PrincipalResolver principalResolver;

    @Autowired
    @Qualifier("loginFlowRegistry")
    private FlowDefinitionRegistry loginFlowDefinitionRegistry;

    @Autowired
    private FlowBuilderServices flowBuilderServices;

    @Autowired
    @Qualifier("adaptiveAuthenticationPolicy")
    private AdaptiveAuthenticationPolicy adaptiveAuthenticationPolicy;

    @Autowired
    @Qualifier("serviceTicketRequestWebflowEventResolver")
    private CasWebflowEventResolver serviceTicketRequestWebflowEventResolver;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("initialAuthenticationAttemptWebflowEventResolver")
    private CasDelegatingWebflowEventResolver initialAuthenticationAttemptWebflowEventResolver;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Bean
    @DependsOn("defaultWebflowConfigurer")
    @ConditionalOnMissingBean(name = "hyWebflowConfigurer")
    public CasWebflowConfigurer hyWebflowConfigurer() {
        final CasWebflowConfigurer w = new HyWebflowConfigurer(flowBuilderServices, loginFlowDefinitionRegistry, applicationContext, casProperties);
        w.initialize();
        return w;
    }

    @Bean
    @ConditionalOnMissingBean(name = "hyWebflowDefaultBuilder")
    public HyWebflowCredentialBuilder hyWebflowDefaultBuilder() {
        return new HyWeixinYtCredentialBuilder();
    }

    @Bean
    @ConditionalOnMissingBean(name = "hyWeixinQyCredentialBuilder")
    public HyWebflowCredentialBuilder hyWeixinQyCredentialBuilder() {
        return new HyWeixinQyCredentialBuilder(hy_weixin_qy_corpid, hy_weixin_qy_corpsecret);
    }

    /**
     * 新的验证方式可以在这里添加
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "hyWebflowBuilderMap")
    public Map<String, HyWebflowCredentialBuilder> hyWebflowBuilderMap() {
        Map<String, HyWebflowCredentialBuilder> builderMap = Maps.newConcurrentMap();
        Stream.of(
                hyWebflowDefaultBuilder(),
                hyWeixinQyCredentialBuilder()
        ).forEach(builder ->
                builder.types()
                        .forEach(type ->
                                builderMap.compute(type, (k, v) -> v == null ? builder : v)
                        )
        );
        return builderMap;
    }

    @Bean
    @ConditionalOnMissingBean(name = "hyWebflowAuthenticationAction")
    public Action hyWebflowAuthenticationAction() {
        return new HyWebflowAuthenticationAction(initialAuthenticationAttemptWebflowEventResolver,
                serviceTicketRequestWebflowEventResolver,
                adaptiveAuthenticationPolicy,
                servicesManager,
                hyWebflowDefaultBuilder(),
                hyWebflowBuilderMap()
        );
    }

    @Bean
    @ConditionalOnMissingBean(name = "hyWebflowAuthenticationHandler")
    public AuthenticationHandler hyWebflowAuthenticationHandler() {
        return new HyWebflowAuthenticationHandler(servicesManager, principalFactory);
    }

    @Bean
    @ConditionalOnMissingBean(name = "hyWebflowAuthenticationEventExecutionPlanConfigurer")
    public AuthenticationEventExecutionPlanConfigurer hyWebflowAuthenticationEventExecutionPlanConfigurer() {
        return plan -> plan.registerAuthenticationHandlerWithPrincipalResolver(hyWebflowAuthenticationHandler(), principalResolver);
    }
}
