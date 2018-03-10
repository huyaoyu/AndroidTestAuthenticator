package com.huyaoyu.testauthenticator;

/**
 * Created by yaoyu on 3/7/18.
 */

public final class CredentialInfo {
    private static final String mClientId     = "SpP2NNkG1sbc2lAJ8r9zQ17jPlsINsCi0CAp8nyj";
    private static final String mClientSecret = "lrKXaSlOlYgalXYXeUVeEv98hhdyV3SGtYCm3UOe03mGvwCOM35xbN8QnAbrvlfBpwaFKsqI1CiLGTUubvQTllps031IWcuk74U1BcEnTm8D2HKIARGK89kbbIKtoEZi";
    private static final String mRedirectUri  = "huyaoyuauth://callback";

    private static final String mHost = "http://192.168.123.96:8080/";

    public static String getClientId() {
        return mClientId;
    }

    public static String getClientSecret() {
        return mClientSecret;
    }

    public static String getRedirectUri() {
        return mRedirectUri;
    }

    public static String getHost() {
        return mHost;
    }
}
