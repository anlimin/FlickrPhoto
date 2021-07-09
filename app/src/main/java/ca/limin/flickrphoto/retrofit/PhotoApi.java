package ca.limin.flickrphoto.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotoApi {
    @GET("photos_public.gne?format=json&nojsoncallback=1")
    Call<String> getAllPhotos();

    @GET("photos_public.gne?format=json&nojsoncallback=1")
    Call<String> categorySearch(@Query("tags") String search);
}
