package truongan.android.shuttershockapidemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import truongan.android.shuttershockapidemo.MyApplication;
import truongan.android.shuttershockapidemo.R;

/**
 * Created by truongan91 on 5/17/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    public Retrofit mRetrofit;
    @Nullable
    @Bind(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getBaseComponent().inject(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (mToolbar != null)
            setSupportActionBar(mToolbar);
        onChildCreate(savedInstanceState);
    }

    public abstract int getLayoutId();

    public abstract void onChildCreate(Bundle savedInstanceState);

    public void hideNotificationBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
