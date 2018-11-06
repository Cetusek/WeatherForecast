package pl.marek.weatherforecast.network;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.marek.weatherforecast.gios.GIOSAirQualityIndex;
import pl.marek.weatherforecast.gios.GIOSSensor;
import pl.marek.weatherforecast.gios.GIOSSensorValue;
import pl.marek.weatherforecast.gios.GIOSStation;
import pl.marek.weatherforecast.gios.GIOSStationList;

public class DownloadGIOSManager implements DownloadGIOSData.DownloadGIOSDataCallback{

    private final String URL_STATIONS = "http://api.gios.gov.pl/pjp-api/rest/station/findAll";
    private final String URL_SENSORS = "http://api.gios.gov.pl/pjp-api/rest/station/sensors/$stationId$";
    private final String URL_SENSOR_DATA = "http://api.gios.gov.pl/pjp-api/rest/data/getData/$sensorId$";
    private final String URL_AIR_QUALITY_INDEX = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/$stationId$";

    private DownloadGIOSManagerCallback callback;

    public DownloadGIOSManager() {
    }

    public void downloadStations() {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_STATIONS, DownloadGIOSData.DownloadDataKind.STATIONS);
        task.execute();
    }

    public void downloadSensors(int stationId) {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_SENSORS.replace("$stationId$", Integer.toString(stationId)), DownloadGIOSData.DownloadDataKind.SENSORS);
        task.execute();
    }

    public void downloadAirQualityIndex(int stationId) {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_AIR_QUALITY_INDEX.replace("$stationId$", Integer.toString(stationId)), DownloadGIOSData.DownloadDataKind.AIR_QUALITY_INDEX);
        task.execute();
    }
    public void downloadSensorData(int sensorId) {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_SENSOR_DATA.replace("$sensorId$", Integer.toString(sensorId)), DownloadGIOSData.DownloadDataKind.SENSOR_DATA);
        task.execute();
    }

    private void encodeStations(JSONArray json) {
        GIOSStationList list = new GIOSStationList();
        if (json != null) {
            for (int i = 0; i < json.length(); i++) {
                try {
                    list.add(new GIOSStation(json.getJSONObject(i)));
                } catch (JSONException e) {
                    return;
                }
            }
        }
        GIOSStation closestStation = list.getClosestStation(51.739105, 19.564188);
        if (closestStation != null) {
            Log.i("MY_APP", closestStation.toString());
        }
    }

    private void encodeSensors(JSONArray json) {
        ArrayList<GIOSSensor> list = new ArrayList<>();
        if (json != null) {
            for (int i = 0; i < json.length(); i++) {
                try {
                    list.add(new GIOSSensor(json.getJSONObject(i)));
                } catch (JSONException e) {
                    return;
                }
            }
        }
        for (GIOSSensor sensor :  list) {
            Log.i("MY_APP", sensor.toString());
        }
    }

    private void encodeSensorData(JSONObject json) {
        ArrayList<GIOSSensorValue> list = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray values = json.getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                        list.add(new GIOSSensorValue(values.getJSONObject(i)));
            }
            } catch (JSONException e) {
                return;
            }
        }
        for (GIOSSensorValue value :  list) {
            Log.i("MY_APP", value.toString());
        }
    }

    private void encodeAirQuallityIndex(JSONObject json) {
        if (json != null) {
            GIOSAirQualityIndex airQualityIndex = new GIOSAirQualityIndex(json);
            Log.i("MY_APP", airQualityIndex.toString());
        }
    }

    @Override
    public void onDownloadGIOSSuccess(DownloadGIOSData.DownloadDataKind downloadDataKind, Object result) {
        switch (downloadDataKind) {
            case STATIONS:
                encodeStations((JSONArray) result);
                break;
            case SENSORS:
                encodeSensors((JSONArray) result);
                break;
            case SENSOR_DATA:
                encodeSensorData((JSONObject) result);
                break;
            case AIR_QUALITY_INDEX:
                encodeAirQuallityIndex((JSONObject) result);
                break;
        }
    }

    @Override
    public void onDownloadGIOSDataFailure(DownloadGIOSData.DownloadDataKind downloadDataKind, String message) {
        Log.i("MY_APP", "FAILURE: "+message);
    }

    public interface DownloadGIOSManagerCallback {
        void GIOSStationListDownloaded(ArrayList<GIOSStation> list);
        void GIOSErrorOccured(String message);
    }

}
