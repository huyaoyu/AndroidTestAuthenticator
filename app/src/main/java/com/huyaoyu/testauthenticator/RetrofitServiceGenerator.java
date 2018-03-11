package com.huyaoyu.testauthenticator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yaoyu on 3/11/18.
 */

public class RetrofitServiceGenerator {
    private static Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(CredentialInfo.getHost())
                .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
