package pl.marek.weatherforecast.gios;


import org.json.JSONException;
import org.json.JSONObject;

public class GIOSSensorValue {

    public String date;
    public String value;


    public GIOSSensorValue (JSONObject json) {
        try {
            date = json.getString("date");
            value = json.getString("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "GIOSSensorValue{" +
                "date='" + date + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
