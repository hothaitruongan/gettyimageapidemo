package truongan.android.shuttershockapidemo.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import truongan.android.shuttershockapidemo.base.BaseActivity;
import truongan.android.shuttershockapidemo.module.ApplicationModule;
import truongan.android.shuttershockapidemo.module.HttpClientModule;

/**
 * Created by truongan91 on 5/17/16.
 */
@Component(modules = {HttpClientModule.class, ApplicationModule.class})
@Singleton
public interface BaseComponent {
    void inject(BaseActivity activity);
}
