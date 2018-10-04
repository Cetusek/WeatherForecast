package pl.marek.weatherforecast.presenter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.persistence.PlaceEntity;

public class PresenterListItem {
    public String name;
    public double latitude;
    public double longitude;
    public Bitmap bitmap;


    public PresenterListItem() {
        //bitmap = BitmapFactory.decodeResource(null, R.drawable.loading);
    }

    public PresenterListItem(PlaceEntity place) {
        name = place.name;
        latitude = place.latitude;
        longitude = place.longitude;
    }

    public LatLng getLatLng() {
        LatLng latLng = new LatLng(latitude, longitude);
        return latLng;
    }
}
