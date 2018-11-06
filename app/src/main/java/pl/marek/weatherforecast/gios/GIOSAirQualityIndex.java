package pl.marek.weatherforecast.gios;

import org.json.JSONException;
import org.json.JSONObject;

public class GIOSAirQualityIndex {

    public int stationId;
    public String stCalcDate;
    public String indexLevelName;
    public int indexLevelId;

    public GIOSAirQualityIndex(JSONObject json) {
        try {
            stationId = json.getInt("id");
            stCalcDate = json.getString("stCalcDate");
            indexLevelName = json.getJSONObject("stIndexLevel").getString("indexLevelName");
            indexLevelId = json.getJSONObject("stIndexLevel").getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "GIOSAirQualityIndex{" +
                "stCalcDate='" + stCalcDate + '\'' +
                ", indexLevelName='" + indexLevelName + '\'' +
                ", indexLevelId=" + indexLevelId +
                '}';
    }
}
