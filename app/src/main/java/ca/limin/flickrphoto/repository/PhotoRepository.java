package ca.limin.flickrphoto.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import ca.limin.flickrphoto.model.Photo;
import ca.limin.flickrphoto.room.PhotoDB;
import ca.limin.flickrphoto.room.PhotoDao;

import java.util.List;

import io.reactivex.Completable;

public class PhotoRepository {
    private final PhotoDao photoDao;

    public PhotoRepository(Application application) {
        PhotoDB photoDB = PhotoDB.getInstance(application);
        photoDao = photoDB.photoDao();
    }

    public Completable insert(Photo photo) {
        return photoDao.insert(photo);
    }

    public Completable insertAll(List<Photo> list) {
        return photoDao.insertAll(list);
    }

    public Completable delete(Photo photo) { return photoDao.delete(photo);}

    public Completable deleteAll() { return photoDao.deleteAll();}

    public Completable update(Photo photo) { return photoDao.update(photo); }

    public LiveData<List<Photo>> getAllPhotos() {
        return photoDao.getAllPhotos();
    }

    public DataSource.Factory<Integer, Photo> getAllPhotosByPaging() { return photoDao.getAllPhotosByPaging(); }

    public LiveData<List<Photo>> categoryPhoto(String query) {
        query += "%" + query + "%";
        return photoDao.categorySearch(query);
    }
}
