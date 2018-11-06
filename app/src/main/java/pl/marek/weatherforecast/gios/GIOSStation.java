package pl.marek.weatherforecast.gios;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GIOSStation implements Serializable{

    public int id;
    public String stationName;
    public double gegrLat;
    public double gegrLon;
    public String cityName;
    public String addressStreet;


    public GIOSStation(JSONObject json) {
        try {
            id = json.getInt("id");
            stationName = json.getString("stationName");
            gegrLat = json.getDouble("gegrLat");
            gegrLon = json.getDouble("gegrLon");
            cityName = json.getJSONObject("city").getString("name");
            addressStreet = json.getString("addressStreet");
        } catch (JSONException e) {
            stationName = "JSONException: "+e.getMessage();
        }
    }

    @Override
    public String toString() {
        return "GIOSStation{" +
                "id=" + id +
                ", stationName='" + stationName + '\'' +
                ", gegrLat=" + gegrLat +
                ", gegrLon=" + gegrLon +
                ", cityName='" + cityName + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                '}';
    }
}
