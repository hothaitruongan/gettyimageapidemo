package truongan.android.shuttershockapidemo.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import truongan.android.shuttershockapidemo.api.PhotoDeserializer;
import truongan.android.shuttershockapidemo.api.PhotoDetailDeserializer;
import truongan.android.shuttershockapidemo.api.PhotoWrapperDeserializer;
import truongan.android.shuttershockapidemo.base.Constants;
import truongan.android.shuttershockapidemo.model.BasePhoto;
import truongan.android.shuttershockapidemo.model.PhotoDetail;
import truongan.android.shuttershockapidemo.model.PhotoWrapper;

/**
 * Created by truongan91 on 5/17/16.
 */
@Module
public class HttpClientModule {

    String baseUrl = "";

    public HttpClientModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Singleton
    @Provides
    Cache provideCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(PhotoWrapper.class, new PhotoWrapperDeserializer())
                .registerTypeAdapter(BasePhoto.class, new PhotoDeserializer())
                .registerTypeAdapter(PhotoDetail.class, new PhotoDetailDeserializer())
                .create();
    }

    @Singleton
    @Provides
    OkHttpClient provideHttpClient(Cache cache) {
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        List<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.HTTP_2);
        protocols.add(Protocol.HTTP_1_1);
        return new OkHttpClient.Builder()
                .protocols(protocols)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        request = request.newBuilder()
                                .addHeader("Api-Key", Constants.API_KEY)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .cache(cache)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(client)
                .build();
    }
}
