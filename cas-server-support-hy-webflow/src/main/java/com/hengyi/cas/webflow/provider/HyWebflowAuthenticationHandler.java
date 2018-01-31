package com.hengyi.cas.webflow.provider;

import com.hengyi.cas.webflow.util.HyWebflowCredential;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import java.security.GeneralSecurityException;

/**
 * @author jzb 2018-01-31
 */
public class HyWebflowAuthenticationHandler extends AbstractAuthenticationHandler {
    public HyWebflowAuthenticationHandler(ServicesManager servicesManager, PrincipalFactory principalFactory) {
        super(null, servicesManager, principalFactory, null);
    }

    @Override
    public HandlerResult authenticate(Credential credential) throws GeneralSecurityException, PreventedException {
        return new DefaultHandlerResult(this, new BasicCredentialMetaData(credential));
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof HyWebflowCredential;
    }
}
