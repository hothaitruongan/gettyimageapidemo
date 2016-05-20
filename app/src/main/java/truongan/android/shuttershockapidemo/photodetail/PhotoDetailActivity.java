package truongan.android.shuttershockapidemo.photodetail;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import truongan.android.shuttershockapidemo.R;
import truongan.android.shuttershockapidemo.base.BaseActivity;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoDetail;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;
import truongan.android.shuttershockapidemo.photolist.PhotoListingAdapter;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoDetailActivity extends BaseActivity implements DetailInterface.PhotoDetailView {

    DetailInterface.PhotoDetailPresentor mPresentor;
    @Bind(R.id.loadingView)
    ProgressBar mLoadingView;
    BasePhoto mPhoto;
    @Bind(R.id.image)
    ImageView imageView;
    @Bind(R.id.artist)
    TextView tvArtist;
    @Bind(R.id.title)
    TextView tvTitle;
    @Bind(R.id.people)
    TextView tvPeople;
    @Bind(R.id.caption)
    TextView tvCaption;
    @Bind(R.id.scrollview)
    ScrollView mScrollView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    SimilarListingAdapter mAdapter;
    GridLayoutManager mGridLayoutManager;
    final int SPAN_NUM = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public void onChildCreate(Bundle savedInstanceState) {
        mPresentor = new PhotoDetailPresentorImpl(this, mRetrofit);
        //setup toolbar
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    supportFinishAfterTransition();
                }
            });
        }

        //setup recyclerview
        mGridLayoutManager = new GridLayoutManager(this, SPAN_NUM, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new SimilarListingAdapter(this, mPresentor);
        mAdapter.setHasHeader(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        //get serializable sextra
        if (savedInstanceState == null) {
            // load data from extra
            // need to call api again
            mPhoto = (BasePhoto) getIntent().getExtras().getSerializable("_photo");
            startup();
        } else {
            // load data from savedInstance
            // no need to call api again
            mPhoto = (BasePhoto) savedInstanceState.getSerializable("_photo");
            final List<BasePhoto> photos = ((PhotoWrapper) savedInstanceState.getSerializable("photowrapper")).getPhotos();
            navigateToDetail(null, null);
            loadSimilarPhoto(photos);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("_photo", mPhoto);
        outState.putSerializable("photowrapper", mAdapter.getDatasetAsWrapper());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void navigateToDetail(BasePhoto photo, SimilarListingAdapter.PhotoViewHolder holder) {
        if (photo != null)
            mPhoto = photo;
        Picasso.with(this)
                .load(mPhoto.getUrl())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        tvTitle.setText(mPhoto.getTitle());
                        mScrollView.smoothScrollTo(0, 0);
                    }

                    @Override
                    public void onError() {
                        showError();
                    }
                });

        //load detail info
        showLoading();
        mPresentor.requestForPhoto(mPhoto.getId());
    }

    @Override
    public void loadSimilarPhoto(List<BasePhoto> photos) {
        hideLoading();
        mAdapter.addAll(photos, false);
    }

    @Override
    public void loadTheFuckingDetail(PhotoDetail photo) {
        hideLoading();
        final Resources resources = getResources();
        tvArtist.setText(String.format(getResources().getString(R.string.detail_by_artist_prefix), photo.getArtist()));
        final String captionFormatter = resources.getString(R.string.detail_caption);
        tvCaption.setText(String.format(captionFormatter, photo.getCaption()));

        final String[] peopleInPhoto = photo.getPeople();
        final int arrayLength = peopleInPhoto.length;
        if (arrayLength != 0) {
            final StringBuilder builder = new StringBuilder(resources.getString(R.string.detail_people_in_photo));
            for (int i = 0; i < arrayLength; i++) {
                builder.append(" " + peopleInPhoto[i]);
                if (i != (arrayLength - 1)) {
                    builder.append(",");
                }
            }
            tvPeople.setText(builder.toString());
        } else {
            tvPeople.setVisibility(View.GONE);
        }

    }

    @Override
    public void showError() {
        hideLoading();
        Toast.makeText(PhotoDetailActivity.this, getResources().getString(R.string.toast_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startup() {
        Picasso.with(this)
                .load(mPhoto.getUrl())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        tvTitle.setText(mPhoto.getTitle());
                    }

                    @Override
                    public void onError() {

                    }
                });
        //load detail info
        showLoading();
        mPresentor.requestForPhoto(mPhoto.getId());
        mPresentor.requestSimilar(mPhoto.getId());
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
}
