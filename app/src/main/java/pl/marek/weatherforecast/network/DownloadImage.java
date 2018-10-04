package pl.marek.weatherforecast.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.marek.weatherforecast.meteo.Coordinates;
import pl.marek.weatherforecast.meteo.URLs;


public class DownloadImage extends AsyncTask {

    private Coordinates coordinates;
    private DownloadImage.DownloadImageCallback callback;
    private URL url;

    public DownloadImage(URL url, DownloadImage.DownloadImageCallback callback) {
        this.url = url;
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        Bitmap bitmap = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            }
            else {
                throw new Exception("Open HTTP connection error: statusCode = "+statusCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            callback.onImageDownloadErrorOccurred("onImageDownload Exception "+e.getMessage());
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        callback.onImageIsDownloaded((Bitmap) o);
    }

    public interface DownloadImageCallback {
        public void onImageIsDownloaded(Bitmap bitmap);
        public void onImageDownloadErrorOccurred(String message);
    }

}
