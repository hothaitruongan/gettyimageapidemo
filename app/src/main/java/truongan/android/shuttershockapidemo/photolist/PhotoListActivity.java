package truongan.android.shuttershockapidemo.photolist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import io.codetailps.animation.SupportAnimator;
import io.codetailps.animation.ViewAnimationUtils;
import truongan.android.shuttershockapidemo.R;
import truongan.android.shuttershockapidemo.base.BaseActivity;
import truongan.android.shuttershockapidemo.base.RecyclerViewItemTouchHelper;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.photodetail.PhotoDetailActivity;
import truongan.android.shuttershockapidemo.searchphoto.SearchActivity;

public class PhotoListActivity extends BaseActivity implements PhotoListInterface.PhotoListView, View.OnClickListener, TextView.OnEditorActionListener {
    int SPAN_NUM = 2;
    boolean isSearchBarShown = false;

    final int ORIENTATION_LANDSCAPE = 1;
    final int ORIENTATION_PORTRAIT = 0;

    GridLayoutManager mGridLayoutManager;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.emptyView)
    View mEmptyView;
    PhotoListingAdapter mAdapter;
    @Bind(R.id.swipetorefresh)
    SwipeRefreshLayout mSwipeToRefreshLayout;
    @Bind(R.id.loadingView)
    ProgressBar mLoadingView;
    ItemTouchHelper mTouchHelper;

    //search bar
    @Bind(R.id.searchbar_wrapper)
    RelativeLayout mWrapper;
    @Bind(R.id.btn_reveal)
    ImageView btnRevealSearch;
    @Bind(R.id.btn_back)
    ImageView btnCloseSearchBar;
    @Bind(R.id.edittext)
    EditText editText;
    @Bind(R.id.btn_clear)
    ImageView btnClear;

    final String DEBUG_TAG = "Test_orientation";

    PhotoListInterface.PhotoListPresentor mPresentor;


    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_list;
    }

    @Override
    public void onChildCreate(Bundle savedInstanceState) {
        mPresentor = new PhotoListPresentorImpl(this, mRetrofit);
        setupView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private int getScreenOrientation() {
        final int currentOrientation = getResources().getConfiguration().orientation;
        return currentOrientation == Configuration.ORIENTATION_LANDSCAPE ? 1 : 0;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN_NUM = 5;
            if (mGridLayoutManager != null)
                mGridLayoutManager.setSpanCount(SPAN_NUM);
            else
                mGridLayoutManager = new GridLayoutManager(this, SPAN_NUM, LinearLayoutManager.VERTICAL, false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            SPAN_NUM = 2;
            if (mGridLayoutManager != null)
                mGridLayoutManager.setSpanCount(SPAN_NUM);
            else
                mGridLayoutManager = new GridLayoutManager(this, SPAN_NUM, LinearLayoutManager.VERTICAL, false);
        }
    }

    @Override
    public void setupView() {
        if (mToolbar != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.activity_photolist_title));
        }
        mGridLayoutManager = new GridLayoutManager(this, SPAN_NUM, LinearLayoutManager.VERTICAL, false);
        mAdapter = new PhotoListingAdapter(this, mPresentor);
        mAdapter.setEmptyView(mEmptyView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback touchHelperCallback = new RecyclerViewItemTouchHelper(mAdapter);
        mTouchHelper = new ItemTouchHelper(touchHelperCallback);
        mTouchHelper.attachToRecyclerView(mRecyclerView);

        editText.setOnEditorActionListener(this);
        btnRevealSearch.setOnClickListener(this);
        btnCloseSearchBar.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        mSwipeToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToRefreshLayout.setRefreshing(true);
                mPresentor.pullToRefresh();
            }
        });
        showLoading();
        mPresentor.requestData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reveal:
                enterReveal(mWrapper, btnRevealSearch);
                break;
            case R.id.btn_back:
                exitReveal(mWrapper, btnRevealSearch);
                break;
            case R.id.btn_clear:
                editText.getText().clear();
                break;
        }
    }

    @Override
    public void navigateToDetail(BasePhoto photo, RecyclerView.ViewHolder holder) {
        final Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra("_photo", photo);
        Pair<View, String> p1 = Pair.create((View) ((PhotoListingAdapter.PhotoViewHolder) holder).imageView, "photo");
        Pair<View, String> p2 = Pair.create((View) ((PhotoListingAdapter.PhotoViewHolder) holder).textView, "title");
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void loadData(List<BasePhoto> photo, boolean isReload) {
        hideLoading();
        mAdapter.addAll(photo, isReload);
    }

    @Override
    public void refresh(List<BasePhoto> photo) {
        mSwipeToRefreshLayout.setRefreshing(false);
        mAdapter.addAll(photo, true);
    }

    @Override
    public void showErrorMesage() {
        hideLoading();
        Toast.makeText(PhotoListActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToSearchView() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_slide_down);
    }

    @Override
    public void forceCloseKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void startup() {

    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        mPresentor.onStop();
        super.onStop();
    }

    public void showSearchBar(MenuItem menuItem) {
        navigateToSearchView();
    }

    public void showAbout(MenuItem menuItem) {
        Toast.makeText(PhotoListActivity.this, getResources().getString(R.string.toast_about), Toast.LENGTH_LONG).show();
    }

    void enterReveal(final View targetView, View initAnimView) {
        isSearchBarShown = true;
        final int cx = (initAnimView.getLeft() + initAnimView.getRight()) / 2;
        final int cy = (initAnimView.getTop() + initAnimView.getBottom()) / 2;
        int finalRadius;
        if (targetView.getWidth() != 0)
            finalRadius = Math.max(targetView.getWidth(), targetView.getHeight());
        else
            finalRadius = getResources().getDisplayMetrics().widthPixels;
        SupportAnimator anim =
                ViewAnimationUtils.createCircularReveal(targetView, cx, cy, 0, finalRadius);
        anim.setDuration(400);
        anim.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                targetView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
                editText.requestLayout();
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });
        anim.start();

    }

    void exitReveal(final View targetView, View initAnimView) {
        isSearchBarShown = false;
        final int cx = (initAnimView.getLeft() + initAnimView.getRight()) / 2;
        final int cy = (initAnimView.getTop() + initAnimView.getBottom()) / 2;
        int initialRadius = targetView.getWidth();
        SupportAnimator anim =
                ViewAnimationUtils.createCircularReveal(targetView, cx, cy, initialRadius, 0);
        anim.setDuration(400);
        anim.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                targetView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel() {

            }

            @Override
            public void onAnimationRepeat() {

            }
        });

        // start the animation
        anim.start();
    }

    @Override
    public void onBackPressed() {
        if (isSearchBarShown)
            exitReveal(mWrapper, btnRevealSearch);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent == null) {
            Log.d(SearchActivity.class.getSimpleName(), "key event found");
            final String query = editText.getText().toString();
            mPresentor.enterQuery(query);
            forceCloseKeyboard();
            showLoading();
        }
        return true;
    }
}
