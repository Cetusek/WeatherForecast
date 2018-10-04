package pl.marek.weatherforecast.favourites;

import java.util.ArrayList;

import pl.marek.weatherforecast.persistence.PlaceEntity;


public class FavouritesList extends ArrayList<FavouritesListItem> {

    public void addFavouritePlace(PlaceEntity placeEntity) {
        FavouritesListItem item = new FavouritesListItem();
        item.name = placeEntity.name;
        item.latitude = placeEntity.latitude;
        item.longitude = placeEntity.longitude;
        add(item);
    }

}
