package truongan.android.shuttershockapidemo.searchphoto;

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
 * Created by truongan91 on 5/18/16.
 */
public class SearchPhotoPresentorImpl implements SearchPhotoInterface.SearchPresentor {

    SearchPhotoInterface.SearchView mView;
    Retrofit mRetrofit;
    Call<PhotoWrapper> call;

    public SearchPhotoPresentorImpl(Retrofit mRetrofit, SearchPhotoInterface.SearchView mView) {
        this.mRetrofit = mRetrofit;
        this.mView = mView;
    }

    @Override
    public void enterQuery(String query) {
        HashMap<String, String> map = new HashMap<>();
        map.put("phrase", query);

        call = mRetrofit.create(GettyImageApiService.class).listPhoto(map);
        call.enqueue(new Callback<PhotoWrapper>() {
            @Override
            public void onResponse(Call<PhotoWrapper> call, Response<PhotoWrapper> response) {
                List<BasePhoto> photos = response.body().getPhotos();
                if(photos != null)
                    mView.loadData(photos);
            }

            @Override
            public void onFailure(Call<PhotoWrapper> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onStop() {
        if (call != null && call.isExecuted())
            call.cancel();
    }
}
