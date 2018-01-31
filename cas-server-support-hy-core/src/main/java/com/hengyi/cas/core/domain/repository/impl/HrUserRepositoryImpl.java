package com.hengyi.cas.core.domain.repository.impl;

import com.hengyi.cas.core.domain.HrUser;
import com.hengyi.cas.core.domain.repository.HrUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;

/**
 * TODO 可以修改成 spring-data 形式，简化 AD 查询的复杂语法
 *
 * @author jzb 2018-01-30
 */
@Component
public class HrUserRepositoryImpl implements HrUserRepository {
    @Autowired
    @Qualifier("hyLdapTemplate")
    private LdapTemplate ldapTemplate;

    private static LdapQueryBuilder query() {
        return LdapQueryBuilder.query().base("ou=hr");
    }

    @Override
    public HrUser find(String hrId) {
        ContainerCriteria criteria = query().where("cn").is(hrId);
        return ldapTemplate.findOne(criteria, HrUser.class);
    }
}
