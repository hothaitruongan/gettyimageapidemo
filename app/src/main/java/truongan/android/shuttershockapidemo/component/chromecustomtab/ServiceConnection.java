package truongan.android.shuttershockapidemo.component.chromecustomtab;

import android.content.ComponentName;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;

import java.lang.ref.WeakReference;

/**
 * Created by truongan91 on 5/20/16.
 */
public class ServiceConnection extends CustomTabsServiceConnection {
    // A weak reference to the ServiceConnectionCallback to avoid leaking it.
    private WeakReference<ServiceConnectionCallback> mConnectionCallback;

    public ServiceConnection(ServiceConnectionCallback connectionCallback) {
        mConnectionCallback = new WeakReference<>(connectionCallback);
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
        ServiceConnectionCallback connectionCallback = mConnectionCallback.get();
        if (connectionCallback != null) connectionCallback.onServiceConnected(client);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        ServiceConnectionCallback connectionCallback = mConnectionCallback.get();
        if (connectionCallback != null) connectionCallback.onServiceDisconnected();
    }
}
