package com.huyaoyu.testauthenticator;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by yaoyu on 3/5/18.
 */

public interface HuyaoyuClient {

    @Headers({
            "Content-Type': 'application/x-www-form-urlencoded",
            "Accept: application/json"
    })
    @POST("o/token/")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri
    );
}
