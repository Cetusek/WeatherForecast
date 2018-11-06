package pl.marek.weatherforecast.network;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import pl.marek.weatherforecast.gios.GIOSStation;
import pl.marek.weatherforecast.meteo.Coordinates;

public class DownloadGIOSData extends AsyncTask {


    private DownloadGIOSDataCallback callback;
    private String url;
    private DownloadDataKind downloadDataKind;

    public DownloadGIOSData(DownloadGIOSDataCallback callback, String url, DownloadDataKind downloadDataKind) {
        this.callback = callback;
        this.url = url;
        this.downloadDataKind = downloadDataKind;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Object result = null;
        try {
            Log.i("MY_APP", url);
            URL url_ = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url_.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            Log.i("MY_APP", "before readLine");
            char[] buffer = new char[1024];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1)  {
                Log.i("MY_APP", "charsRead = "+charsRead);
                sb.append(Arrays.copyOf(buffer, charsRead));
                Log.i("MY_APP", "appended. last char: "+buffer[charsRead-1]);
            }
            Log.i("MY_APP", "after readLine: "+sb.toString());
            reader.close();
            switch (downloadDataKind) {
                case STATIONS:
                    result = new JSONArray(sb.toString());
                    break;
                case SENSORS:
                    result = new JSONArray(sb.toString());
                    break;
                case SENSOR_DATA:
                    result = new JSONObject(sb.toString());
                    break;
                case AIR_QUALITY_INDEX:
                    result = new JSONObject(sb.toString());
                    break;
            }
        } catch (MalformedURLException e) {
            failureOccured("DownloadGIOSData MalformedURLException "+e.getMessage());
        } catch (IOException e) {
            failureOccured("DownloadGIOSData IOException "+e.getMessage());
        } catch (JSONException e) {
            failureOccured("DownloadGIOSData JSONException "+e.getMessage());
        }

        return result;
    }

    private void failureOccured(String message) {
        callback.onDownloadGIOSDataFailure(downloadDataKind, message);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        callback.onDownloadGIOSSuccess(downloadDataKind, o);
    }

    interface DownloadGIOSDataCallback {
        public void onDownloadGIOSSuccess(DownloadDataKind downloadDataKind, Object result);
        public void onDownloadGIOSDataFailure(DownloadDataKind downloadDataKind, String message);
    }

    enum DownloadDataKind {
        STATIONS,
        SENSORS,
        SENSOR_DATA,
        AIR_QUALITY_INDEX
    }

}
