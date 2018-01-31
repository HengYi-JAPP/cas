package com.hengyi.cas.core.util;

import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.PrincipalResolver;
import org.apereo.services.persondir.IPersonAttributeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 验证结束后的 principal 抓取，统一交给 hyPrincipalFactory 来处理
 * TODO 可以把这里的转换结果缓存，这样可以减少转换的次数和 AD 访问的次数
 *
 * @author jzb 2018-01-30
 */
@Component
public class HyPrincipalResolver implements PrincipalResolver {
    @Autowired
    @Qualifier("hyPrincipalFactory")
    private PrincipalFactory principalFactory;

    @Override
    public Principal resolve(Credential credential, Principal principal, AuthenticationHandler handler) {
        return principalFactory.createPrincipal(credential.getId());
    }

    @Override
    public boolean supports(Credential credential) {
        return Objects.nonNull(credential.getId());
    }

    @Override
    public IPersonAttributeDao getAttributeRepository() {
        return null;
    }

}
