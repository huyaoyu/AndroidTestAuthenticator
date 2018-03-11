package com.huyaoyu.testauthenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AccountManager mAccountManager;
    private TextView mTextViewAuthToken;
    private String mAuthToken;
    private String mAccountType;
    private String mUserName;
    private String mRefreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountManager = AccountManager.get(this);

        findViewById(R.id.button_get_auth_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTokenForAccountCreateIfNeeded(getResources().getString(R.string.account_type), "Bearer");
            }
        });

        mTextViewAuthToken = findViewById(R.id.text_main_auth_token);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        String authToken = null;

        try {
            authToken = intent.getExtras().getString("AuthToken");
        } catch (java.lang.NullPointerException e) {
            // Do nothing.
        }

        if ( authToken != null ) {
            mAuthToken = authToken;
            mUserName = intent.getExtras().getString("UserName");
            mAccountType = intent.getExtras().getString("AccountType");
            mRefreshToken = intent.getExtras().getString("RefreshToken");

            showMessage(( (mAuthToken != null) ? "Auth token: " + mAuthToken + "\nRefresh token: " + mRefreshToken: "Failed." ));

            mTextViewAuthToken.setText(
                    "Auth token: " + mAuthToken
                            + "\nRefresh Token: " + mRefreshToken
                            + "\nUser name: " + mUserName);
        } else {
            mTextViewAuthToken.setText("onResume(): Null authToken.");
        }
    }

    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {

        final Account[] accountArray = mAccountManager.getAccountsByType(accountType);

        if ( accountArray.length == 0 ) {
            showMessage("No accounts found.");
        } else {

            final AccountManagerFuture<Bundle> future =
                    mAccountManager.getAuthToken(
                            accountArray[0],
                            authTokenType,
                            null,
                            false,
                            new AccountManagerCallback<Bundle>() {
                                @Override
                                public void run(AccountManagerFuture<Bundle> future) {
                                    Bundle bnd = null;

                                    try {
                                        bnd = future.getResult();
                                        mAuthToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                                        mAccountType = bnd.getString(AccountManager.KEY_ACCOUNT_TYPE);
                                        mUserName  = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                                        mRefreshToken = mAccountManager.getUserData(accountArray[0], "RefreshToken");

                                        final Intent intent = new Intent(MainActivity.this, MainActivity.class);

                                        intent.putExtra("AuthToken", mAuthToken);
                                        intent.putExtra("AccountType", mAccountType);
                                        intent.putExtra("UserName", mUserName);
                                        intent.putExtra("RefreshToken", mRefreshToken);

                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                        showMessage(e.getMessage());
                                    }
                                }
                            },
                            null
                    );

//            if ( true == Authenticator.isAuthTokenExpired(mAccountManager, accountArray[0], 600) ) {
//                mAccountManager.invalidateAuthToken(mAccountType, mAuthToken);
//
//                showMessage("Token expired.");
//
//                final AccountManagerFuture<Bundle> future2 =
//                        mAccountManager.getAuthToken(
//                                accountArray[0],
//                                authTokenType,
//                                null,
//                                false,
//                                new AccountManagerCallback<Bundle>() {
//                                    @Override
//                                    public void run(AccountManagerFuture<Bundle> future) {
//                                        Bundle bnd = null;
//
//                                        try {
//                                            bnd = future.getResult();
//                                            mAuthToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
//                                            mUserName  = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
//                                            mRefreshToken = mAccountManager.getUserData(accountArray[0], "RefreshToken");
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//
//                                            showMessage(e.getMessage());
//                                        }
//                                    }
//                                },
//                                null
//                        );
//            }
        }
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
