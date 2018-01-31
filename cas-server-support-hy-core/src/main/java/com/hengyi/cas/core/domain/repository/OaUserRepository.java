package com.hengyi.cas.core.domain.repository;

import com.hengyi.cas.core.domain.OaUser;

/**
 * @author jzb 2018-01-30
 */
public interface OaUserRepository {
    OaUser find(String oaId);

    OaUser findByHrId(String hrId);
}
