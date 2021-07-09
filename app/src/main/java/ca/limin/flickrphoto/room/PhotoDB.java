package ca.limin.flickrphoto.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ca.limin.flickrphoto.model.Photo;

@Database(entities = {Photo.class}, version = 1)
public abstract class PhotoDB extends RoomDatabase {
    private static PhotoDB instance;
    public abstract PhotoDao photoDao();

    public static PhotoDB getInstance(Context context) {
        if (instance == null) {
            synchronized (PhotoDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), PhotoDB.class, "photo_db")
                            .fallbackToDestructiveMigration() // reset database to null if migration error happen
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
