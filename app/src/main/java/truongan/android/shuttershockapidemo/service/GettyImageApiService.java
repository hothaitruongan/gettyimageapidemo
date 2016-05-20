package truongan.android.shuttershockapidemo.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoDetail;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;

/**
 * Created by truongan91 on 5/17/16.
 */
public interface GettyImageApiService {

    @GET("search/images")
    Call<PhotoWrapper> listPhoto(@QueryMap Map<String, String> map);

    @GET("images/{id}")
    Call<PhotoDetail> getPhotoDetail(@Path("id") String id, @QueryMap Map<String, String> map);

    @GET("images/{id}/similar")
    Call<PhotoWrapper> getSimilarPhoto(@Path("id") String id, @QueryMap Map<String, String> map);
}
