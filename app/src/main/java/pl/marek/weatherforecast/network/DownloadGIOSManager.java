package pl.marek.weatherforecast.network;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadGIOSManager implements DownloadGIOSData.DownloadGIOSDataCallback{

    private final String URL_STATIONS = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private final String URL_SENSORS = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/$stationId$";

    public DownloadGIOSManager() {
    }

    public void downloadStations() {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_STATIONS);
        task.execute();
    }

    public void downloadSensors(int stationId) {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_SENSORS.replace("$stationId$", Integer.toString(stationId)));
        task.execute();
    }

    @Override
    public void onDownloadGIOSDataSuccess(JSONArray result) {
        for (int i = 0; i < result.length(); i++) {
            try {
                Log.i("MY_APP", "JSON: "+result.getJSONObject(i).getString("param"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onDownloadGIOSDataFailure(String message) {
        Log.i("MY_APP", message);

    }
}
