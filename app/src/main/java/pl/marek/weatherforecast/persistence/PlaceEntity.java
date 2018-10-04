package pl.marek.weatherforecast.persistence;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "PLACES")

public class PlaceEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public double latitude;
    public double longitude;

    @Override
    public String toString() {
        return "PlaceEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
