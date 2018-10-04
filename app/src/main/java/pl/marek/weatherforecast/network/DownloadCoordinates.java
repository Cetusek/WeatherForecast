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

    @Override
    protected Coordinates doInBackground(Object[] params) {

        Coordinates result = new Coordinates();

        try {
            URL url = new URL(URLs.getCoordinatesURL(location));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            reader.close();
            JSONObject json = new JSONObject(line);
            result.row = json.getInt("row");
            result.col = json.getInt("col");
        } catch (MalformedURLException e) {
            callback.onCoordinatesDownloadErrorOccurred("onCoordinatesDownloadErrorOccurred MalformedURLException "+e.getMessage());
        } catch (IOException e) {
            callback.onCoordinatesDownloadErrorOccurred("onCoordinatesDownloadErrorOccurred IOException "+e.getMessage());
        } catch (JSONException e) {
            callback.onCoordinatesDownloadErrorOccurred("onCoordinatesDownloadErrorOccurred JSONException "+e.getMessage());
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
