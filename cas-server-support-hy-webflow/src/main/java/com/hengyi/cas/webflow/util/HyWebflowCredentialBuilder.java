package com.hengyi.cas.webflow.util;

import org.springframework.webflow.execution.RequestContext;

import java.util.stream.Stream;

/**
 * 在 URL 的 type 参数中设置验证方法
 * 可以对应多种 type
 * 推荐在 {@link #build(RequestContext)} 方法中验证，并获得最终的 principal
 * 在 {@link com.hengyi.cas.webflow.provider.HyWebflowAuthenticationHandler} 中不做验证，因为无法知道具体一种 type 中该如何来验证
 *
 * @author jzb 2018-01-31
 */
public interface HyWebflowCredentialBuilder {
    Stream<String> types();

    HyWebflowCredential build(RequestContext context);
}
