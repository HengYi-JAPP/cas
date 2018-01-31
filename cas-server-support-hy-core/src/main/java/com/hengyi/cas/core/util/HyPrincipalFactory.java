package com.hengyi.cas.core.util;

import com.google.common.collect.Maps;
import com.hengyi.cas.core.domain.HrUser;
import com.hengyi.cas.core.domain.OaUser;
import com.hengyi.cas.core.domain.repository.HrUserRepository;
import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.authentication.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 描述： 处理所有验证后的 principal 和 attribute map
 * 1. 尽量传 hrId 到 client，把所有的 oaId 全部转换为 hrId
 * 2. 附加公司、部门等信息
 *
 * @author jzb 2018-01-30
 */
@Component
public class HyPrincipalFactory extends DefaultPrincipalFactory {
    @Autowired
    private HrUserRepository hrUserRepository;
    @Autowired
    private OaUserRepository oaUserRepository;

    @Override
    public Principal createPrincipal(String id) {
        if (HyUtils.isAdmin(id)) {
            return createAdminPrincipal(id);
        } else if (HyUtils.isHr(id)) {
            return createHrPrincipal(id);
        } else {
            return createOaPrincipal(id);
        }
    }

    private Principal createOaPrincipal(String id) {
        final OaUser oaUser = oaUserRepository.find(id);
        return Optional.ofNullable(oaUser.getHrId())
                .map(hrUserRepository::find)
                .map(hrUser -> {
                    Map<String, Object> attributes = this.getAttributes(hrUser);
                    extraAttributes(attributes, oaUser);
                    return createPrincipal(hrUser.getHrId(), attributes);
                })
                .orElseGet(() -> {
                    Map<String, Object> attributes = Maps.newHashMap();
                    attributes.put("uid", oaUser.getOaId());
                    attributes.put("displayName", oaUser.getDisplayName());
                    attributes.put("company", oaUser.getCompany());
                    attributes.put("department", oaUser.getDepartment());
                    extraAttributes(attributes, oaUser);
                    return createPrincipal(oaUser.getOaId(), attributes);
                });
    }

    private Map<String, Object> getAttributes(HrUser hrUser) {
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("uid", hrUser.getHrId());
        attributes.put("displayName", hrUser.getDisplayName());
        attributes.put("company", hrUser.getCompany());
        attributes.put("department", hrUser.getDepartment());
        return attributes;
    }

    private Map<String, Object> extraAttributes(Map<String, Object> attributes, OaUser oaUser) {
        attributes.put("oauser", oaUser.getOaId());
        return attributes;
    }

    private Principal createHrPrincipal(String id) {
        final HrUser hrUser = hrUserRepository.find(id);
        Map<String, Object> attributes = getAttributes(hrUser);
        Optional.ofNullable(oaUserRepository.findByHrId(id))
                .ifPresent(oaUser -> extraAttributes(attributes, oaUser));
        return createPrincipal(hrUser.getHrId(), attributes);
    }

    private Principal createAdminPrincipal(String id) {
        return super.createPrincipal(id);
    }
}
