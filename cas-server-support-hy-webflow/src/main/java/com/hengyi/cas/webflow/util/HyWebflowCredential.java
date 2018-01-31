package com.hengyi.cas.webflow.util;

import org.apereo.cas.authentication.Credential;

/**
 * 默认的 {@link Credential} 只有 id 属性，验证在 build 中做
 * 因为一般都是企业微信或公众号的 code 验证，或其他的跳转自动验证
 *
 * @author jzb 2018-01-31
 */
public class HyWebflowCredential implements Credential {
    public static final String PARAMETER_NAME_TYPE = "type";
    private final String id;

    HyWebflowCredential(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
