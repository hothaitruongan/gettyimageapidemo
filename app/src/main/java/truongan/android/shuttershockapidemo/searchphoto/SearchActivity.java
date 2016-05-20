package truongan.android.shuttershockapidemo.searchphoto;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import truongan.android.shuttershockapidemo.R;
import truongan.android.shuttershockapidemo.base.BaseActivity;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.photodetail.SimilarListingAdapter;

/**
 * Created by truongan91 on 5/18/16.
 */
public class SearchActivity extends BaseActivity implements SearchPhotoInterface.SearchView {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.edittext)
    EditText editText;
    @Bind(R.id.btn_clear)
    ImageView btnClear;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.emptyView)
    View mEmptyView;

    final int SPAN_NUM = 2;
    StaggeredGridLayoutManager mGridLayoutManager;
    SimilarListingAdapter mAdapter;
    SearchPhotoInterface.SearchPresentor mPresentor;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void onChildCreate(Bundle savedInstanceState) {
        mPresentor = new SearchPhotoPresentorImpl(mRetrofit, this);
        mGridLayoutManager = new StaggeredGridLayoutManager(SPAN_NUM, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new SimilarListingAdapter(this, null);
        mAdapter.setHasHeader(false);
        mAdapter.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mAdapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.getText().clear();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent == null) {
                    Log.d(SearchActivity.class.getSimpleName(), "key event found");
                    final String query = editText.getText().toString();
                    mPresentor.enterQuery(query);
                }
                return true;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void loadData(List<BasePhoto> photo) {
        forceCloseKeyboard();
        mAdapter.addAll(photo, true);
    }

    @Override
    public void forceCloseKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
    }

    @Override
    public void startup() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onStop() {
        mPresentor.onStop();
        super.onStop();
    }
}
