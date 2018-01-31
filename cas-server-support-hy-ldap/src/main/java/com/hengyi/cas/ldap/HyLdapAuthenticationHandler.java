package com.hengyi.cas.ldap;

import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.apereo.cas.authentication.*;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author jzb 2018-01-30
 */
public class HyLdapAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
    private OaUserRepository oaUserRepository;

    public HyLdapAuthenticationHandler(ServicesManager servicesManager, PrincipalFactory principalFactory, OaUserRepository oaUserRepository) {
        super(null, servicesManager, principalFactory, null);
        this.oaUserRepository = oaUserRepository;
    }

    /**
     * oaId 登录验证，MD5 验证
     */
    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential upc, final String originalPassword) throws GeneralSecurityException, PreventedException {
        return Optional.ofNullable(upc)
                .map(UsernamePasswordCredential::getId)
                .map(oaUserRepository::find)
                .filter(Objects::nonNull)
                .filter(oaUser -> oaUser.checkPassword(originalPassword))
                .map(it -> new DefaultHandlerResult(this, new BasicCredentialMetaData(upc)))
                .orElseThrow(() -> new GeneralSecurityException());
    }

}
