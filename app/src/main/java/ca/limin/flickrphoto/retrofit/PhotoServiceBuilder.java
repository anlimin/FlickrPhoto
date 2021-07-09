package ca.limin.flickrphoto.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PhotoServiceBuilder {
    private static final String URL = "https://www.flickr.com/services/feeds/";

    // Create logger
    private static final HttpLoggingInterceptor logger = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    // Factory for calls, which can be used to send HTTP requests and read their responses.
    private static final OkHttpClient.Builder okHttp = new OkHttpClient.Builder().addInterceptor(logger);

    private volatile static Retrofit retrofit;

    // Use builder and Service interface to generate required classes and objects
    public static <S> S buildService(Class<S> serviceType) {
        return getRetrofit().create(serviceType);
    }

    //singleton pattern
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (PhotoServiceBuilder.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(ScalarsConverterFactory.create()) //for string model type
                            //.addConverterFactory(GsonConverterFactory.create()) // Enable GSON converter
                            //.client(okHttp.build())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
