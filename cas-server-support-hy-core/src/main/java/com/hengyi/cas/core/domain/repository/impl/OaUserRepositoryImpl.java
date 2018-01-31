package com.hengyi.cas.core.domain.repository.impl;

import com.hengyi.cas.core.domain.OaUser;
import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;

/**
 * @author jzb 2018-01-30
 */
@Component
public class OaUserRepositoryImpl implements OaUserRepository {
    @Autowired
    @Qualifier("hyLdapTemplate")
    private LdapTemplate ldapTemplate;

    private static LdapQueryBuilder query() {
        return LdapQueryBuilder.query().base("ou=oa");
    }


    @Override
    public OaUser find(String oaId) {
        ContainerCriteria criteria = query().where("cn").is(oaId);
        return ldapTemplate.findOne(criteria, OaUser.class);
    }

    @Override
    public OaUser findByHrId(String hrId) {
        ContainerCriteria criteria = query().where("employeeid").is(hrId);
        return ldapTemplate.findOne(criteria, OaUser.class);
    }
}
