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

        String authToken = am.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            // TODO: Update existing account. Not register new one on the device.
            final Intent intent = new Intent(mContext, LoginActivity.class);

            intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, account.type);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);

            final Bundle result = new Bundle();
            result.putParcelable(AccountManager.KEY_INTENT, intent);
            return result;
        } else {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
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
