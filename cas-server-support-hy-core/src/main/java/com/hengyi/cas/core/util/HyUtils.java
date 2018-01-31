package com.hengyi.cas.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 描述：工具类
 *
 * @author jzb on 2018-01-30.
 */
public final class HyUtils {
    public static final OkHttpClient OK_CLIENT = new OkHttpClient.Builder().build();
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private static final String ADMIN_ID = "admin";
    /**
     * hrId 正则测试
     * 当前规则： 1 到 9 数字开头，后面 7 位数字，共8位数字
     */
    private static final Pattern hrIdP = Pattern.compile("^[1-9]\\d{7}$");

    public static boolean isAdmin(String s) {
        return StringUtils.isBlank(s) ? false : StringUtils.equalsIgnoreCase(ADMIN_ID, s);
    }

    public static boolean isHr(String s) {
        return StringUtils.isBlank(s) ? false : hrIdP.matcher(s).matches();
    }

    public static JsonNode okGetToJson(String url) throws IOException {
        return MAPPER.readTree(okGetToString(url));
    }

    public static String okGetToString(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body()) {
                return body.string();
            }
        }
    }
}
