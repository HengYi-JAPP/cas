package com.hengyi.cas.core.domain.repository.impl;

import com.hengyi.cas.core.domain.OaUser;
import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jzb 2018-01-30
 */
@Component
public class OaUserRepositoryImpl implements OaUserRepository {
    private static final Logger log = LoggerFactory.getLogger(OaUserRepository.class);
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
        final List<OaUser> oaUsers = ldapTemplate.find(criteria, OaUser.class);
        if (CollectionUtils.isEmpty(oaUsers)) {
            return null;
        }
        if (oaUsers.size() > 1) {
            final Set<String> oaIds = oaUsers.stream()
                    .map(OaUser::getOaId)
                    .collect(Collectors.toSet());
            log.error("员工号[" + hrId + "],对应有多个oauser[" + oaIds + "]");
        }
        return oaUsers.get(0);
    }
}
