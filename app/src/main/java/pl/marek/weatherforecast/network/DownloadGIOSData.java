package pl.marek.weatherforecast.network;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import pl.marek.weatherforecast.meteo.Coordinates;

public class DownloadGIOSData extends AsyncTask {


    private DownloadGIOSDataCallback callback;
    private String url;

    public DownloadGIOSData(DownloadGIOSDataCallback callback, String url) {
        this.callback = callback;
        this.url = url;
    }

    @Override
    protected JSONArray doInBackground(Object[] params) {
        JSONArray result = null;
        try {
            URL url_ = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url_.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)  {
                sb.append(line);
            }
            reader.close();
            Log.i("MY_APP", "sb.toString().length() = "+sb.toString().length());
            //result = new JSONArray(sb.toString().substring(1, sb.toString().length()));
            result = new JSONArray(sb.toString());
        } catch (MalformedURLException e) {
            callback.onDownloadGIOSDataFailure("DownloadGIOSData MalformedURLException "+e.getMessage());
        } catch (IOException e) {
            callback.onDownloadGIOSDataFailure("DownloadGIOSData IOException "+e.getMessage());
        } catch (JSONException e) {
            callback.onDownloadGIOSDataFailure("DownloadGIOSData JSONException "+e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null) {
            callback.onDownloadGIOSDataSuccess((JSONArray) o);
        }
    }

    interface DownloadGIOSDataCallback {
        public void onDownloadGIOSDataSuccess(JSONArray result);
        public void onDownloadGIOSDataFailure(String message);
    }

}
