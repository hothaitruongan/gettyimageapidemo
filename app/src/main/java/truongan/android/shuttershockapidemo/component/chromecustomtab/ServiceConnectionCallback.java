package truongan.android.shuttershockapidemo.component.chromecustomtab;

import android.support.customtabs.CustomTabsClient;

/**
 * Created by truongan91 on 5/20/16.
 */
public interface ServiceConnectionCallback  {

    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();
}
