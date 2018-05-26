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
public class HyWeixinQyCredentialBuilder implements HyWebflowCredentialBuilder {
    private static final Logger log = LoggerFactory.getLogger(HyWeixinQyCredentialBuilder.class);
    public static String URL_TPL_ACCESSTOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=${corpid}&corpsecret=${corpsecret}";
    public static String URL_TPL_GETUSERINFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=${access_token}&code=${code}";
    private final String corpid;
    private final String corpsecret;

    public HyWeixinQyCredentialBuilder(String corpid, String corpsecret) {
        this.corpid = corpid;
        this.corpsecret = corpsecret;
    }

    @Override
    public Stream<String> types() {
        return Stream.of("QY_WEIXIN");
    }

    @Override
    public HyWebflowCredential build(RequestContext requestContext) {
        final HttpServletRequest request = WebUtils.getHttpServletRequestFromExternalWebflowContext(requestContext);
        final String code = request.getParameter("code");
        if (StringUtils.isBlank(code)) {
            return null;
        }
        try {
            return new HyWebflowCredential(getId(code));
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    private String getId(String code) throws IOException {
        String url = StrSubstitutor.replace(URL_TPL_ACCESSTOKEN, ImmutableMap.of("corpid", corpid, "corpsecret", corpsecret));
        JsonNode res = HyUtils.okGetToJson(url);
        checkError(res);
        String access_token = res.get("access_token").asText();

        url = StrSubstitutor.replace(URL_TPL_GETUSERINFO, ImmutableMap.of("access_token", access_token, "code", code));
        res = HyUtils.okGetToJson(url);
        checkError(res);
        return res.get("UserId").asText();
    }

    private void checkError(JsonNode res) {
        JsonNode errcode = res.get("errcode");
        if (errcode != null && errcode.asInt() > 0) {
            System.out.println(res);
            log.error(res.toString());
            throw new RuntimeException(res.toString());
        }
    }
}
