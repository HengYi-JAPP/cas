package qy;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StrSubstitutor;

import java.io.IOException;
import java.net.URLEncoder;

public class UrlTest {
    static String weixinUrlTpl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx8d1fcf0d627bf7bb&redirect_uri=${redirect_uri}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    static String casUrlTpl = "http://cas.hengyi.com:8080/cas/login?type=QY_WEIXIN&service=${service}";
    static String url = "http://htms.hengyi.com/contoller.do?operation=Mobile-Login_of_myportal";
    static String cs = "utf-8";

    public static void main(String[] args) throws IOException {
        final String encodeUrl = URLEncoder.encode(url, cs);
        final String casUrl = StrSubstitutor.replace(casUrlTpl, ImmutableMap.of("service", encodeUrl));
        final String encodeCasUrl = URLEncoder.encode(casUrl, cs);
        final String weixinUrl = StrSubstitutor.replace(weixinUrlTpl, ImmutableMap.of("redirect_uri", encodeCasUrl));
        System.out.println(weixinUrl);
    }
}
