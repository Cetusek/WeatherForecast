package pl.marek.weatherforecast.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {PlaceEntity.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase{

    public abstract AppDao AppDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "WeatherforecastDB")
                    .fallbackToDestructiveMigration() //To trzeba usunąć
                    .build();
        }
        return instance;
    }

    public static AppDatabase getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppDatabase instance is not set.");
        }
        return instance;
    }


}