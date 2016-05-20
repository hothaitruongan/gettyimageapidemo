package truongan.android.shuttershockapidemo.photolist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;
import truongan.android.shuttershockapidemo.service.GettyImageApiService;

/**
 * Created by truongan91 on 5/17/16.
 */
public class PhotoListPresentorImpl implements PhotoListInterface.PhotoListPresentor {

    PhotoListInterface.PhotoListView mView;
    Retrofit mRetrofit;
    Call<PhotoWrapper> call;
    String mQuery = "Elizabeth Olsen";
    boolean isRefresh = false;

    public PhotoListPresentorImpl(PhotoListInterface.PhotoListView mView, Retrofit retrofit) {
        this.mView = mView;
        this.mRetrofit = retrofit;
    }

    @Override
    public void onPhotoClick(BasePhoto photo, RecyclerView.ViewHolder holder) {
        //navigate to photo-detail
        mView.navigateToDetail(photo, holder);
    }

    @Override
    public void pullToRefresh() {
        isRefresh = true;
        requestData();
    }

    @Override
    public void requestData() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("phrase", mQuery);
        map.put("fields", "display_set, summary_set");

        call = mRetrofit.create(GettyImageApiService.class).listPhoto(map);
        call.enqueue(new Callback<PhotoWrapper>() {
            @Override
            public void onResponse(Call<PhotoWrapper> call, Response<PhotoWrapper> response) {
                List<BasePhoto> photos = response.body().getPhotos();
                Log.d(PhotoListPresentorImpl.class.getSimpleName(), "photo size:" + photos.size());
                if (photos != null) {
                    if (isRefresh)
                        mView.refresh(photos);
                    else
                        mView.loadData(photos, true);
                } else {
                    mView.showErrorMesage();
                }
                isRefresh = false;
            }

            @Override
            public void onFailure(Call<PhotoWrapper> call, Throwable t) {
                isRefresh = false;
                mView.hideLoading();
                mView.showErrorMesage();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void enterQuery(String query) {
        mQuery = query;
        HashMap<String, String> map = new HashMap<>();
        map.put("phrase", query);
        map.put("fields", "display_set, detail_set");

        call = mRetrofit.create(GettyImageApiService.class).listPhoto(map);
        call.enqueue(new Callback<PhotoWrapper>() {
            @Override
            public void onResponse(Call<PhotoWrapper> call, Response<PhotoWrapper> response) {
                List<BasePhoto> photos = response.body().getPhotos();
                if (photos != null)
                    mView.loadData(photos, true);
            }

            @Override
            public void onFailure(Call<PhotoWrapper> call, Throwable t) {
                mView.hideLoading();
                mView.showErrorMesage();
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onStop() {
        if (call.isExecuted()) {
            call.cancel();
        }
    }
}
