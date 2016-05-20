package truongan.android.shuttershockapidemo;

import android.app.Application;

import truongan.android.shuttershockapidemo.base.Constants;
import truongan.android.shuttershockapidemo.component.BaseComponent;
import truongan.android.shuttershockapidemo.component.DaggerBaseComponent;
import truongan.android.shuttershockapidemo.module.ApplicationModule;
import truongan.android.shuttershockapidemo.module.HttpClientModule;

/**
 * Created by truongan91 on 5/17/16.
 */
public class MyApplication extends Application {
    BaseComponent mBaseComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseComponent = DaggerBaseComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpClientModule(new HttpClientModule(Constants.BASE_URL))
                .build();
    }

    public BaseComponent getBaseComponent() {
        return mBaseComponent;
    }
}
