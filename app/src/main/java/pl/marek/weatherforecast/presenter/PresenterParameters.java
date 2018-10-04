package pl.marek.weatherforecast.presenter;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class PresenterParameters implements Serializable {

    public PresenterAction action;
    public double latitude;
    public double longitude;
    public String name;

}
