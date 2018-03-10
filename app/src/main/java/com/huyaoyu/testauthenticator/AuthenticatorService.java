package com.huyaoyu.testauthenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;

    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAuthenticator = new Authenticator(AuthenticatorService.this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mAuthenticator.getIBinder();
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
