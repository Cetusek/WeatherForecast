package pl.marek.weatherforecast.persistence;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface AppDao {

    @Insert
    public long insertPlace(PlaceEntity entity);

    @Insert
    public long[] insertPlaces(PlaceEntity[] entity);

    @Query("SELECT * FROM PLACES ORDER BY ID")
    public PlaceEntity[] selectPlaces();


    @Query("DELETE FROM PLACES")
    public void deletePlaces();

    @Query("SELECT COUNT(*) FROM PLACES")
    public int countPlaces();


}
