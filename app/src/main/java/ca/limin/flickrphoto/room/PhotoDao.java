package ca.limin.flickrphoto.room;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import ca.limin.flickrphoto.model.Photo;
import io.reactivex.Completable;

@Dao
public interface PhotoDao {
    @Insert
    Completable insert(Photo photo);

    @Insert
    Completable insertAll(List<Photo> photos);

    @Update
    Completable update(Photo photo);

    @Delete
    Completable delete(Photo photo);

    @Query("delete from Photo")
    Completable deleteAll();

    @Query("select * from Photo order by id desc")
    LiveData<List<Photo>> getAllPhotos();

    @Query("select * from Photo order by id asc")
    DataSource.Factory<Integer, Photo> getAllPhotosByPaging();

    @Query("select * from Photo where tag like :query")
    LiveData<List<Photo>> categorySearch(String query);
}
