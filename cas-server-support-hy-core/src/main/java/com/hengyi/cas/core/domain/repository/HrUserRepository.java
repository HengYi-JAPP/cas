package com.hengyi.cas.core.domain.repository;

import com.hengyi.cas.core.domain.HrUser;

/**
 * @author jzb 2018-01-30
 */
public interface HrUserRepository {
    HrUser find(String hrId);
}
