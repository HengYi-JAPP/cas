package com.hengyi.cas.webflow.provider;

import com.google.common.collect.ImmutableMap;
import com.hengyi.cas.webflow.util.HyWebflowCredentialBuilder;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.adaptive.AdaptiveAuthenticationPolicy;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceAccessStrategyUtils;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.flow.actions.AbstractNonInteractiveCredentialsAction;
import org.apereo.cas.web.flow.resolver.CasDelegatingWebflowEventResolver;
import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;
import org.apereo.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.hengyi.cas.webflow.util.HyWebflowCredential.PARAMETER_NAME_TYPE;

/**
 * @author jzb 2018-01-30
 */
public class HyWebflowAuthenticationAction extends AbstractNonInteractiveCredentialsAction {
    private final ServicesManager servicesManager;
    private final HyWebflowCredentialBuilder defaultBuilder;
    private final Map<String, HyWebflowCredentialBuilder> builderMap;

    public HyWebflowAuthenticationAction(final CasDelegatingWebflowEventResolver initialAuthenticationAttemptWebflowEventResolver,
                                         final CasWebflowEventResolver serviceTicketRequestWebflowEventResolver,
                                         final AdaptiveAuthenticationPolicy adaptiveAuthenticationPolicy,
                                         final ServicesManager servicesManager,
                                         final HyWebflowCredentialBuilder defaultBuilder,
                                         final Map<String, HyWebflowCredentialBuilder> builderMap) {
        super(initialAuthenticationAttemptWebflowEventResolver, serviceTicketRequestWebflowEventResolver, adaptiveAuthenticationPolicy);
        this.servicesManager = servicesManager;
        this.defaultBuilder = defaultBuilder;
        this.builderMap = ImmutableMap.copyOf(builderMap);
    }

    @Override
    protected Credential constructCredentialsFromRequest(RequestContext requestContext) {
        final Service service = WebUtils.getService(requestContext);
        if (Objects.isNull(service)) {
            return null;
        }
        final RegisteredService registeredService = this.servicesManager.findServiceBy(service);
        RegisteredServiceAccessStrategyUtils.ensureServiceAccessIsAllowed(service, registeredService);

        final HttpServletRequest request = WebUtils.getHttpServletRequestFromExternalWebflowContext(requestContext);
        return Optional.ofNullable(request.getParameter(PARAMETER_NAME_TYPE))
                .map(builderMap::get)
                .map(builder -> builder.build(requestContext))
                .orElseGet(() -> defaultBuilder.build(requestContext));
    }
}
