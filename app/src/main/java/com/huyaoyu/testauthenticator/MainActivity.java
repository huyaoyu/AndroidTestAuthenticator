package com.huyaoyu.testauthenticator;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AccountManager mAccountManager;
    private TextView mTextViewAuthToken;

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

    private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future =
                mAccountManager.getAuthTokenByFeatures(
                        accountType,
                        authTokenType,
                        null,
                        this,
                        null,
                        null,
                        new AccountManagerCallback<Bundle>() {
                            @Override
                            public void run(AccountManagerFuture<Bundle> future) {
                                Bundle bnd = null;

                                try {
                                    bnd = future.getResult();
                                    final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                                    final String userName  = bnd.getString(AccountManager.KEY_ACCOUNT_NAME);
                                    showMessage(( (authToken != null) ? "Auth token:" + authToken + "." : "Failed." ));

                                    mTextViewAuthToken.setText("Auth token: " + authToken + "\nUser name: " + userName);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                    showMessage(e.getMessage());
                                }
                            }
                        },
                        null
                );
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
