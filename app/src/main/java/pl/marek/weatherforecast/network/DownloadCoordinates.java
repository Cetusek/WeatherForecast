package pl.marek.weatherforecast.network;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import pl.marek.weatherforecast.meteo.Coordinates;
import pl.marek.weatherforecast.meteo.URLs;


public class DownloadCoordinates extends AsyncTask {

    private LatLng location;
    private DownloadCoordinatesCallback callback;

    public DownloadCoordinates(LatLng location, DownloadCoordinatesCallback callback) {
        this.location = location;
        this.callback = callback;
    }

    private String getStringBetweenStrings(String source, String s1, String s2) {
        String result = null;
        String tmp = source.toString();
        int pos = tmp.indexOf(s1);
        if (pos != -1) {
            tmp = tmp.substring(pos + s1.length());
            pos = tmp.indexOf(s2);
            if (pos != -1) {
                result = tmp.substring(0, pos);
            }
        }
        return result;
    }

    @Override
    protected Coordinates doInBackground(Object[] params) {

        Coordinates result = null;

        try {
            URL url = new URL(URLs.getCoordinatesURL(location));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("var act_x = ") != -1) {
                    String act_x = getStringBetweenStrings(line, "act_x = ", ";");
                    String act_y = getStringBetweenStrings(line, "act_y = ", ";");
                    if (act_x != null && act_y != null) {
                        result = new Coordinates();
                        result.setRow(Integer.parseInt(act_y));
                        result.setCol(Integer.parseInt(act_x));
                    }
                }
            }
            reader.close();
        } catch (MalformedURLException e) {
            callback.onCoordinatesDownloadErrorOccurred("onCoordinatesDownloadErrorOccurred MalformedURLException "+e.getMessage());
        } catch (IOException e) {
            callback.onCoordinatesDownloadErrorOccurred("onCoordinatesDownloadErrorOccurred IOException "+e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        callback.onCoordinatesWereDownloaded((Coordinates) o);
    }

    interface DownloadCoordinatesCallback {
        public void onCoordinatesWereDownloaded(Coordinates coordinates);
        public void onCoordinatesDownloadErrorOccurred(String message);
    }

}
