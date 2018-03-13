package com.huyaoyu.testauthenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yaoyu on 3/5/18.
 */

public class Authenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public Authenticator(Context context) {
        super(context);

        this.mContext = context;
    }

    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            String accountType,
            String authTokenType,
            String[] strings,
            Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, LoginActivity.class);

        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle getAuthToken(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account,
            String authTokenType,
            Bundle bundle) throws NetworkErrorException {
        final AccountManager am = AccountManager.get(mContext);

        return refreshTokenFunc(am, account);
    }

    public static boolean isAuthTokenExpired(final AccountManager am, Account account, long bufferTimeSpan) {
        // Check the time stamp.
        long currentTimeStamp  = System.currentTimeMillis();
        long originalTimeStamp = Long.parseLong(am.getUserData(account, "TimeStamp"), 10);

        // Change the time span into seconds.
        long timeSpan = ( currentTimeStamp - originalTimeStamp ) / 1000;

        // Get the user-data "ExpiresIn".
        long expiersIn = Long.parseLong(am.getUserData(account, "ExpiresIn"), 10);

        if ( expiersIn - bufferTimeSpan > timeSpan ) {
            return false;
        } else {
            return true;
        }
    }

    private Bundle refreshTokenFunc(AccountManager am, Account account) {
        // Try to obtain a new token.

        String refreshToken = am.getUserData(account, "RefreshToken");

        // Get a new Retrofit client and Call object.
        HuyaoyuClient client = RetrofitServiceGenerator.createService(HuyaoyuClient.class);
        Call<AccessToken> refreshTokenCall = client.refreshAccessToken(
                "refresh_token",
                CredentialInfo.getClientId(),
                CredentialInfo.getClientSecret(),
                refreshToken);

        String accessToken;
        String tokenType;
        String expiresIn;
        long currentTimeStamp;

        try {
            Response<AccessToken> resultRetrofit = refreshTokenCall.execute();

            accessToken      = resultRetrofit.body().getAccessToken();
            tokenType        = resultRetrofit.body().getTokenType();
            expiresIn        = resultRetrofit.body().getExpiresIn();
            refreshToken     = resultRetrofit.body().getRefreshToken();
            currentTimeStamp = System.currentTimeMillis();

            am.setAuthToken(account, tokenType, accessToken);

            am.setUserData(account, "ExpiresIn", expiresIn);
            am.setUserData(account, "RefreshToken", refreshToken);
            am.setUserData(account, "TimeStamp", String.valueOf(currentTimeStamp));

            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, accessToken);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle editProperties(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            String s) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account,
            String[] strings) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle updateCredentials(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account,
            String s,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }
}
