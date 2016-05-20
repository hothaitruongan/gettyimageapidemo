package truongan.android.shuttershockapidemo.photodetail;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoDetail;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;
import truongan.android.shuttershockapidemo.service.GettyImageApiService;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoDetailPresentorImpl implements DetailInterface.PhotoDetailPresentor {

    DetailInterface.PhotoDetailView mView;
    Retrofit mRetrofit;
    Call<PhotoDetail> call;
    Call<PhotoWrapper> call2;

    public PhotoDetailPresentorImpl(DetailInterface.PhotoDetailView mView, Retrofit mRetrofit) {
        this.mView = mView;
        this.mRetrofit = mRetrofit;
    }

    @Override
    public void requestForPhoto(final String id) {
        mView.showLoading();

        HashMap<String, String> map = new HashMap<>();
        map.put("fields", "summary_set, caption, people");

        call = mRetrofit.create(GettyImageApiService.class).getPhotoDetail(id, map);
        call.enqueue(new Callback<PhotoDetail>() {
            @Override
            public void onResponse(Call<PhotoDetail> call, Response<PhotoDetail> response) {
                PhotoDetail photoDetail = response.body();
                if(photoDetail != null) {
                    mView.loadTheFuckingDetail(photoDetail);
                } else {
                    mView.showError();
                }
                mView.hideLoading();
            }

            @Override
            public void onFailure(Call<PhotoDetail> call, Throwable t) {
                mView.hideLoading();
                mView.showError();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void requestSimilar(String id) {
        HashMap<String, String> map = new HashMap<>();
                map.put("fields","display_set, summary_set");
        call2 = mRetrofit.create(GettyImageApiService.class).getSimilarPhoto(id, map);

        call2.enqueue(new Callback<PhotoWrapper>() {
            @Override
            public void onResponse(Call<PhotoWrapper> call, Response<PhotoWrapper> response) {
                List<BasePhoto> similarPhotos = response.body().getPhotos();
                mView.loadSimilarPhoto(similarPhotos);
            }

            @Override
            public void onFailure(Call<PhotoWrapper> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onPhotoClick(BasePhoto photo, SimilarListingAdapter.PhotoViewHolder holder) {
        mView.navigateToDetail(photo, holder);
    }

    @Override
    public void onStop() {
        if(call != null && call.isExecuted())
            call.cancel();
        if(call2 != null && call2.isExecuted())
            call2.cancel();
    }
}
