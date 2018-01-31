package com.hengyi.cas.webflow.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.hengyi.cas.core.util.HyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StrSubstitutor;
import org.apereo.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author jzb 2018-01-31
 */
public class HyWeixinYtCredentialBuilder implements HyWebflowCredentialBuilder {
    private static final Logger log = LoggerFactory.getLogger(HyWeixinYtCredentialBuilder.class);
    private static String URL_TPL = "http://supplier.hengyi.com:8133/Account/SSLValidate?sessionID=${sessionID}&accountName=${accountName}";

    @Override
    public Stream<String> types() {
        return Stream.of("OLD_WEIXIN");
    }

    @Override
    public HyWebflowCredential build(RequestContext requestContext) {
        final HttpServletRequest request = WebUtils.getHttpServletRequestFromExternalWebflowContext(requestContext);
        final String w_user = request.getParameter("w_user");
        if (StringUtils.isBlank(w_user)) {
            return null;
        }
        final String w_pass = request.getParameter("w_pass");
        if (StringUtils.isBlank(w_pass)) {
            return null;
        }
        try {
            check(w_user, w_pass);
            return new HyWebflowCredential(w_user);
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }

    private void check(String w_user, String w_pass) throws IOException {
        final String url = StrSubstitutor.replace(URL_TPL, ImmutableMap.of("sessionID", w_pass, "accountName", w_user));
        final JsonNode res = HyUtils.okGetToJson(url);
        JsonNode errcode = res.get("ErrCode");
        if (errcode.asInt() != 0) {
            throw new RuntimeException();
        }
    }
}
