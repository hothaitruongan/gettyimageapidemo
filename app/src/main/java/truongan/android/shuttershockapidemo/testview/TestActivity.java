package truongan.android.shuttershockapidemo.testview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;

import butterknife.ButterKnife;
import truongan.android.shuttershockapidemo.R;
import truongan.android.shuttershockapidemo.component.chromecustomtab.CustomTabActivityHelper;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse("http://www.google.com"),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//        if (browserIntent.resolveActivity(getPackageManager()) != null) {
//            startActivity(browserIntent);
//        }
    }
}
