package com.huyaoyu.testauthenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AccountManager mAccountManager;
    private TextView mTextViewAuthToken;
    private String mAuthToken;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountManager = AccountManager.get(this);

        findViewById(R.id.button_get_auth_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, getResources().getString(R.string.account_type));
                bundle.putString("AuthTokenType", "Bearer");

                new GetAuthTokenTask().execute(bundle);
            }
        });

        mTextViewAuthToken = findViewById(R.id.text_main_auth_token);
    }

    private class GetAuthTokenTask extends AsyncTask<Bundle, Void, Bundle> {
        @Override
        protected Bundle doInBackground(Bundle... bundles) {
            // Retrieve the strings.
            Bundle bnd = bundles[0];
            String accountType = bnd.getString(AccountManager.KEY_ACCOUNT_TYPE);
            String authTokenType = bnd.getString("AuthTokenType");

            // Find the account.
            final Account[] accountArray = mAccountManager.getAccountsByType(accountType);

            String authToken = null;
            Bundle result = new Bundle();

            if (accountArray.length == 0) {
                showMessage("No accounts found.");
            } else {
                // Get an authToken by using blockingGetAuthToken().

                try {
                    authToken = mAccountManager.blockingGetAuthToken(accountArray[0],
                            authTokenType, false);
                } catch (AuthenticatorException ae) {
                    ae.printStackTrace();
                } catch (OperationCanceledException oce) {
                    oce.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                if ( null == authToken ) {
                    return result;
                }

                if (true == Authenticator.isAuthTokenExpired(mAccountManager, accountArray[0], 600)) {
                    showMessage("authToken is expired.");

                    mAccountManager.invalidateAuthToken(accountType, authToken);

                    try {
                        authToken = mAccountManager.blockingGetAuthToken(accountArray[0],
                                authTokenType, false);
                    } catch (AuthenticatorException ae) {
                        ae.printStackTrace();
                    } catch (OperationCanceledException oce) {
                        oce.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                    if ( null == authToken ) {
                        return result;
                    }
                }

                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                result.putString(AccountManager.KEY_ACCOUNT_NAME, accountArray[0].name);
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            super.onPostExecute(bundle);

            // Retrieve the authToken and userName strings.
            String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
            String userName  = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);

            // Update the UI and save the data.
            if ( null != authToken && null != userName ) {
                mAuthToken = authToken;
                mUserName  = userName;

                mTextViewAuthToken.setText("AuthToken: " + authToken + "\nUserName: " + userName);
                showMessage("AuthToken obtained.");
            } else {
                mTextViewAuthToken.setText("No data.");
                showMessage("Failed to obtained authToken");
            }
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
