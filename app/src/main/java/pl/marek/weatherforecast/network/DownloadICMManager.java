package pl.marek.weatherforecast.network;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.meteo.Coordinates;
import pl.marek.weatherforecast.meteo.URLs;
import pl.marek.weatherforecast.presenter.PresenterAdapter;

public class DownloadICMManager implements DownloadCoordinates.DownloadCoordinatesCallback, DownloadImage.DownloadImageCallback{

    private PresenterAdapter presenterAdapter;
    private int position;
    private Bitmap bitmap;
    private Coordinates coordinates;
    private Context context;

    public DownloadICMManager(Context context) {
        this.context = context;
    }

    public void downloadICMImage(LatLng location, PresenterAdapter presenterAdapter, int position) {
        this.presenterAdapter = presenterAdapter;
        this.position = position;
        executeTask(location);
    }


    private void executeTask(LatLng location) {
        DownloadCoordinates task = new DownloadCoordinates(location, this);
        task.execute();
    }


    @Override
    public void onCoordinatesWereDownloaded(Coordinates coordinates) {
        this.coordinates = coordinates;
        if (coordinates != null) {
            downloadICMImage();
        }
        else {
            onImageDownloadErrorOccurred(context.getResources().getString(R.string.internet_problem));
        }
    }

    private void downloadICMImage() {
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
    }

    @Override
    public void onImageIsDownloaded(Bitmap bitmap) {
        if (bitmap != null && bitmap.getHeight() < 100) {
            //To znaczy, że ICM jeszcze nie wygenerował obrazka, więc trzbea spróbować w sąsiednim miejscu
            if (coordinates.getAttemptNo() < coordinates.getMaxAttempt()) {
                coordinates.increaseAttempt();
                downloadICMImage();
                return;
            }
        }
        if (presenterAdapter != null) {
            presenterAdapter.onImageLoaded(bitmap, position, Integer.toString(coordinates.getAttemptNo()));
        }
        this.bitmap = bitmap;
    }

    @Override
    public void onImageDownloadErrorOccurred(String message) {
        if (presenterAdapter != null) {
            presenterAdapter.onImageLoaded(null, position, message);
        }
    }
}
