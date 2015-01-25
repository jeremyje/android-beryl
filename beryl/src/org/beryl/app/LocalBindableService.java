package org.beryl.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public abstract class LocalBindableService extends ServiceBase {

	/**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	LocalBindableService getService() {
            return LocalBindableService.this;
        }
    }
    
    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
    
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
