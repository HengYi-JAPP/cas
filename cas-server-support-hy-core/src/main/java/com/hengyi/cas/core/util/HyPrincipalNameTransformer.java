package com.hengyi.cas.core.util;

import com.hengyi.cas.core.domain.OaUser;
import com.hengyi.cas.core.domain.repository.OaUserRepository;
import org.apereo.cas.authentication.handler.PrincipalNameTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 描述： 给后台 AD 验证的时候，全部转换为 hrId，因为 oaId 当前无法自动验证，需要手动 MD5 验证
 *
 * @author jzb 2018-01-30
 */
@Component
public class HyPrincipalNameTransformer implements PrincipalNameTransformer {
    @Autowired
    private OaUserRepository oaUserRepository;

    @Override
    public String transform(String formUserId) {
        if (HyUtils.isAdmin(formUserId) || HyUtils.isHr(formUserId)) {
            return formUserId;
        }
        return Optional.ofNullable(oaUserRepository.find(formUserId))
                .map(OaUser::getHrId)
                .orElse(formUserId);
    }
}
