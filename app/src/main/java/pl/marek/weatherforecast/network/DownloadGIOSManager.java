package pl.marek.weatherforecast.network;


import android.util.Log;
import android.widget.TextView;

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
    private TaskType taskType;

    private int position;

    private GIOSSensor sensor;

    private double latitude;
    private double longitude;


    public DownloadGIOSManager(DownloadGIOSManagerCallback callback) {
        this.callback = callback;
        taskType = TaskType.NONE;
    }

    public void downloadStations() {
        DownloadGIOSData task = new DownloadGIOSData(this, URL_STATIONS, DownloadGIOSData.DownloadDataKind.STATIONS);
        task.execute();
    }

    public void downloadSensors(int stationId) {
        taskType = TaskType.SENSORS;
        DownloadGIOSData task = new DownloadGIOSData(this, URL_SENSORS.replace("$stationId$", Integer.toString(stationId)), DownloadGIOSData.DownloadDataKind.SENSORS);
        task.execute();
    }

    public void downloadSensorData(GIOSSensor sensor) {
        taskType = TaskType.SENSOR_DATA;
        this.sensor = sensor;
        DownloadGIOSData task = new DownloadGIOSData(this, URL_SENSOR_DATA.replace("$sensorId$", Integer.toString(sensor.id)), DownloadGIOSData.DownloadDataKind.SENSOR_DATA);
        task.execute();
    }

    public void downloadAirQualityIndexForStation(int position, GIOSStation GIOSStation) {
        taskType = TaskType.AIR_QUALITY_INDEX_FOR_STATION;
        this.position = position;
        DownloadGIOSData task = new DownloadGIOSData(this, URL_AIR_QUALITY_INDEX.replace("$stationId$", Integer.toString(GIOSStation.id)), DownloadGIOSData.DownloadDataKind.AIR_QUALITY_INDEX);
        task.execute();
    }

    public void downloadClosestStation(int position, double latitude, double longitude) {
        taskType = TaskType.CLOSEST_STATION;
        this.latitude = latitude;
        this.longitude = longitude;
        this.position = position;
        downloadStations();
    }

    private void onStationsDownloaded(Object result) {
        GIOSStationList list = encodeStations((JSONArray) result);
        switch (taskType) {
            case NONE:
                break;
            case CLOSEST_STATION:
                GIOSStation closestStation = list.getClosestStation(latitude, longitude);
                if (callback != null) {
                    callback.GIOSClosestStationDownloaded(position, closestStation);
                }
                break;
        }
    }

    private void onAirQualityIndexDownloaded(Object result) {
        GIOSAirQualityIndex airQualityIndex = encodeAirQualityIndex((JSONObject) result);
        switch (taskType) {
            case NONE:
                break;
            case AIR_QUALITY_INDEX_FOR_STATION:
                if (callback != null) {
                    callback.GIOSAirQualityIndexDownloaded(position, airQualityIndex);
                }
                break;
        }
    }

    private void onSensorsDownloaded(Object result) {
        ArrayList<GIOSSensor> list = encodeSensors((JSONArray) result);
        if (callback != null) {
            callback.GIOSSensorsDownloaded(list);
        }
    }

    private GIOSStationList encodeStations(JSONArray json) {
        GIOSStationList list = new GIOSStationList();
        if (json != null) {
            for (int i = 0; i < json.length(); i++) {
                try {
                    list.add(new GIOSStation(json.getJSONObject(i)));
                } catch (JSONException e) {
                    return list;
                }
            }
        }
        return list;
    }


    private ArrayList<GIOSSensor> encodeSensors(JSONArray json) {
        ArrayList<GIOSSensor> list = new ArrayList<>();
        if (json != null) {
            for (int i = 0; i < json.length(); i++) {
                try {
                    list.add(new GIOSSensor(json.getJSONObject(i)));
                } catch (JSONException e) {
                    return list;
                }
            }
        }
        return list;
    }

    private void onSensorDataDownload(Object result) {
        sensor.values  = encodeSensorData((JSONObject) result);
        if (callback != null) {
            callback.GIOSSensorDataDownloaded(sensor);
        }

    }

    private ArrayList<GIOSSensorValue> encodeSensorData(JSONObject json) {
        ArrayList<GIOSSensorValue> list = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray values = json.getJSONArray("values");
                for (int i = 0; i < values.length(); i++) {
                        list.add(new GIOSSensorValue(values.getJSONObject(i)));
            }
            } catch (JSONException e) {
                return list;
            }
        }
        return list;
    }

    private GIOSAirQualityIndex encodeAirQualityIndex(JSONObject json) {
        GIOSAirQualityIndex airQualityIndex = null;
        if (json != null) {
            airQualityIndex = new GIOSAirQualityIndex(json);
        }
        return airQualityIndex;
    }

    @Override
    public void onDownloadGIOSSuccess(DownloadGIOSData.DownloadDataKind downloadDataKind, Object result) {
        switch (downloadDataKind) {
            case STATIONS:
                onStationsDownloaded(result);
                break;
            case SENSORS:
                onSensorsDownloaded(result);
                break;
            case SENSOR_DATA:
                onSensorDataDownload(result);
                break;
            case AIR_QUALITY_INDEX:
                onAirQualityIndexDownloaded(result);
                break;
        }
    }

    @Override
    public void onDownloadGIOSDataFailure(DownloadGIOSData.DownloadDataKind downloadDataKind, String message) {
        Log.i("MY_APP", "FAILURE: "+message);
    }

    public interface DownloadGIOSManagerCallback {
        void GIOSAirQualityIndexDownloaded(int position, GIOSAirQualityIndex airQualityIndex);
        void GIOSClosestStationDownloaded(int position, GIOSStation GIOSStation);
        void GIOSSensorsDownloaded(ArrayList<GIOSSensor> list);
        void GIOSSensorDataDownloaded(GIOSSensor sensor);
        void GIOSErrorOccurred(String message);
    }

    private enum TaskType {
        NONE,
        CLOSEST_STATION,
        AIR_QUALITY_INDEX_FOR_STATION,
        SENSORS,
        SENSOR_DATA
    }

}
