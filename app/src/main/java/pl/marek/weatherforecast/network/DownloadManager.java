package pl.marek.weatherforecast.network;


import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;

import pl.marek.weatherforecast.meteo.Coordinates;
import pl.marek.weatherforecast.meteo.URLs;
import pl.marek.weatherforecast.presenter.PresenterAdapter;

public class DownloadManager implements DownloadCoordinates.DownloadCoordinatesCallback, DownloadImage.DownloadImageCallback{

    private PresenterAdapter presenterAdapter;
    int position;
    Bitmap bitmap;

    public void downloadImage(LatLng location, PresenterAdapter presenterAdapter, int position) {
        this.presenterAdapter = presenterAdapter;
        this.position = position;
        executeTask(location);
    }

    public void downloadImage(LatLng location, Bitmap bitmap) {
        this.bitmap = bitmap;
        executeTask(location);
    }


    private void executeTask(LatLng location) {
        DownloadCoordinates task = new DownloadCoordinates(location, this);
        task.execute();
    }


    @Override
    public void onCoordinatesWereDownloaded(Coordinates coordinates) {
        DownloadImage task = null;
        try {
            task = new DownloadImage(new URL(URLs.getImageURL(coordinates)), this);
        } catch (MalformedURLException e) {
            onImageDownloadErrorOccurred(e.getMessage());
        }
        task.execute();
    }

    @Override
    public void onCoordinatesDownloadErrorOccurred(String message) {
        Log.i("MY_APP", "Error: "+message);
    }

    @Override
    public void onImageIsDownloaded(Bitmap bitmap) {
        if (presenterAdapter != null) {
            presenterAdapter.onImageLoaded(bitmap, position);
        }
        this.bitmap = bitmap;
    }

    @Override
    public void onImageDownloadErrorOccurred(String message) {
        Log.i("MY_APP", "Error: "+message);
    }
}
