package qy;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.hengyi.cas.core.util.HyUtils;
import org.apache.commons.text.StrSubstitutor;

import java.io.IOException;

import static com.hengyi.cas.webflow.util.HyWeixinQyCredentialBuilder.URL_TPL_ACCESSTOKEN;
import static com.hengyi.cas.webflow.util.HyWeixinQyCredentialBuilder.URL_TPL_GETUSERINFO;

public class QyTest {
    static String corpid = "wx8d1fcf0d627bf7bb";
    static String corpsecret = "ytXk7q14hszI-fpqxxAmJ5su97R1d9yrx8eyyIs8ely8EPOR33BXlVrRDrJFZqVW";
    static String code = "zj5rliSlg3rCNuPg4KFU1GS7zOahU-Kf7H8E8QxUiQo";

    public static void main(String[] args) throws IOException {


        System.out.println(getId(code));
    }

    static String getId(String code) throws IOException {
        String url = StrSubstitutor.replace(URL_TPL_ACCESSTOKEN, ImmutableMap.of("corpid", corpid, "corpsecret", corpsecret));
        JsonNode res = HyUtils.okGetToJson(url);
        checkError(res);
        String access_token = res.get("access_token").asText();

        url = StrSubstitutor.replace(URL_TPL_GETUSERINFO, ImmutableMap.of("access_token", access_token, "code", code));
        res = HyUtils.okGetToJson(url);
        checkError(res);
        return res.get("UserId").asText();
    }

    static void checkError(JsonNode res) {
        JsonNode errcode = res.get("errcode");
        if (errcode != null && errcode.asInt() > 0) {
            throw new RuntimeException(res.toString());
        }
    }
}
