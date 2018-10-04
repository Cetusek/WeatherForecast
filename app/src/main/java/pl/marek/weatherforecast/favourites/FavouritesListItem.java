package pl.marek.weatherforecast.favourites;

import pl.marek.weatherforecast.persistence.PlaceEntity;

public class FavouritesListItem {

    public String name;
    public double latitude;
    public double longitude;
    public boolean isChecked;

    public PlaceEntity getPlace() {
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.name = name;
        placeEntity.latitude = latitude;
        placeEntity.longitude = longitude;
        return placeEntity;
    }

    @Override
    public String toString() {
        return "FavouritesListItem{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isChecked=" + isChecked +
                '}';
    }
}
