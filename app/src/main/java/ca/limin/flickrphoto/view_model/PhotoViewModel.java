package ca.limin.flickrphoto.view_model;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import ca.limin.flickrphoto.json_parser.JsonParser;
import ca.limin.flickrphoto.model.Photo;
import ca.limin.flickrphoto.paging.BoundaryInterface;
import ca.limin.flickrphoto.paging.MyBoundaryCallback;
import ca.limin.flickrphoto.repository.PhotoRepository;
import ca.limin.flickrphoto.retrofit.PhotoApi;
import ca.limin.flickrphoto.retrofit.PhotoServiceBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class PhotoViewModel extends AndroidViewModel implements LifecycleObserver, BoundaryInterface {
    private static final String TAG = PhotoViewModel.class.getSimpleName();
    private final PhotoRepository photoRepository;
    private LiveData<PagedList<Photo>> pagedListLiveData;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MyBoundaryCallback myBoundaryCallback;
    private boolean isQuery = false;
    private boolean isNetwork = true;

    public PhotoViewModel(Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
        checkNetwork(application);
        createPageList();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    private void checkNetwork(Application application) {
        isNetwork = isNetwork(application);
        if (!this.isNetwork) {
            getAllPhotosByPaging();
        }
    }

    private boolean isNetwork(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.isAvailable()) {
            return true;
        }
        return false;
    }

    public LiveData<PagedList<Photo>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    private void createPageList() {
        PagedList.Config myPagingConfig = new PagedList.Config.Builder()
                .setPageSize(20)
                .setPrefetchDistance(20)
                .setEnablePlaceholders(false)
                .build();
        myBoundaryCallback = new MyBoundaryCallback(this);
        pagedListLiveData = (new LivePagedListBuilder<>(photoRepository.getAllPhotosByPaging(),
                myPagingConfig)).setBoundaryCallback(myBoundaryCallback).build();
    }

    public void insert(Photo photo) {
        compositeDisposable.add(photoRepository.insert(photo)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(() -> { Log.i(TAG, "insert one finished"); },
                         throwable -> throwable.printStackTrace()));
    }

    public void insertAll(List<Photo> list) {
        compositeDisposable.add(photoRepository.insertAll(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { Log.i(TAG, "insert all finished"); },
                        throwable -> throwable.printStackTrace()));
    }

    public void update(Photo photo) {
        compositeDisposable.add(photoRepository.update(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { Log.i(TAG, "update finished"); },
                        throwable -> throwable.printStackTrace()));
    }

    public void delete(Photo photo) {
        compositeDisposable.add(photoRepository.delete(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { Log.i(TAG, "delete finished"); },
                        throwable -> throwable.printStackTrace()));
    }

    public void deleteAll() {
        compositeDisposable.add(photoRepository.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> { Log.i(TAG, "delete all finished"); },
                        throwable -> throwable.printStackTrace()));
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return photoRepository.getAllPhotos();
    }

    public DataSource.Factory<Integer, Photo> getAllPhotosByPaging() {
        return photoRepository.getAllPhotosByPaging();
    }

    public LiveData<List<Photo>> categorySearch(String query) {
        return photoRepository.categoryPhoto(query);
    }

    public void setQuery(boolean query) {
        isQuery = query;
    }

    public void requestNetwork() {
        Call<String> call = PhotoServiceBuilder.buildService(PhotoApi.class).getAllPhotos();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull retrofit2.Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "requestNetwork failed code" + response.code());
                    return;
                }
                insertAll(JsonParser.parseJson(response.body()));
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    public void requestCategorySearch(String query) {
        myBoundaryCallback.setQuery(query);
        myBoundaryCallback.setQuery(true);
        Call<String> call = PhotoServiceBuilder.buildService(PhotoApi.class).categorySearch(query);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "categorySearch failed code" + response.code());
                    return;
                }
                insertAll(JsonParser.parseJson(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void requestMore() {
        requestNetwork();
    }

    @Override
    public void requestSearchMore(String query) {
        requestCategorySearch(query);
    }

    public boolean isNetworkAvailable() {
        return isNetwork;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void onCreate() {
        Log.i(TAG, "create()");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() { Log.i(TAG, "stop()"); }
}
