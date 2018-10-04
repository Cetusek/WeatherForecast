package pl.marek.weatherforecast.storm;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.blitzortung.URLs;
import pl.marek.weatherforecast.network.DownloadImage;
import pl.marek.weatherforecast.presenter.ZoomedImageView;

public class StormActivity extends AppCompatActivity implements DownloadImage.DownloadImageCallback{

    ZoomedImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storm);
        mapGUI();
        loadImage();
    }

    private void mapGUI() {
        imageView = (ZoomedImageView) findViewById(R.id.StormActivityImageView);

    }

    private void loadImage() {
        try {
            DownloadImage task = new DownloadImage(new URL(URLs.URL_for_Poland), this);
            task.execute();
        } catch (MalformedURLException e) {
            onImageDownloadErrorOccurred("URL problem: "+e.getMessage());
        }
    }




    @Override
    public void onImageIsDownloaded(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onImageDownloadErrorOccurred(String message) {

    }


}
