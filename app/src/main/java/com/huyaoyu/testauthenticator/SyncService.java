package com.huyaoyu.testauthenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {
    private static SyncAdapter mSyncAdapter = null;
    private static final Object mSyncAdapterLock = new Object();

    public SyncService() {
        mSyncAdapter = mSyncAdapter;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (mSyncAdapterLock) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }
}
