package pl.marek.weatherforecast.presenter;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.network.DownloadManager;
import pl.marek.weatherforecast.persistence.DBAsyncTask;
import pl.marek.weatherforecast.persistence.PlaceEntity;

public class PresenterActivity extends AppCompatActivity implements DBAsyncTask.Callback{

    ViewPager viewPager;
    PresenterAdapter presenterAdapter;
    PresenterParameters presenterParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter);
        mapGUI();
        retrieveParameters();
        doAction();
    }

    private void retrieveParameters() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenterParameters = (PresenterParameters) bundle.get("PARAMETERS");
        }
    }

    private void doAction() {
        if (presenterParameters != null) {
            switch (presenterParameters.action) {
                case SHOW_FOR_ONE:
                    showForOne();
                    viewPager.setAdapter(presenterAdapter);
                    break;
                case SHOW_FOR_FAVOURITES:
                    loadFavourites();
                    break;
            }
        }
    }

    private void loadFavourites() {
        DBAsyncTask task = new DBAsyncTask(null, this);
        task.selectPlaces();
    }

    private void onFavouritesLoaded(PlaceEntity[] places) {
        for (int i = 0; i < places.length; i++) {
            presenterAdapter.addItem(new PresenterListItem(places[i]));
        }
        if (places.length > 0) {
            loadImageForFirstItem();
        }
        viewPager.setAdapter(presenterAdapter);
    }

    private void showForOne() {
        PresenterListItem item = new PresenterListItem();
        item.name = presenterParameters.name;
        item.latitude = presenterParameters.latitude;
        item.longitude = presenterParameters.longitude;
        presenterAdapter.addItem(item);
        loadImageForFirstItem();
    }

    private void loadImageForFirstItem() {
        loadImage(0);
    }

    private void mapGUI() {
        presenterAdapter = new PresenterAdapter(this);
        viewPager = (ViewPager) findViewById(R.id.PresenterActivityViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                onViewerPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void loadImage(int position) {
        DownloadManager m = new DownloadManager();
        m.downloadImage(presenterAdapter.getPresenterListItem(position).getLatLng(), presenterAdapter, position);
    }

    private void onViewerPageSelected(int position) {
        loadImage(position);
    }

    @Override
    public void DBAsyncTaskFinished(String callerId, Object result) {
        onFavouritesLoaded((PlaceEntity[]) result);
    }

    @Override
    public void DBAsyncTaskError(String callerId, String message) {

    }


    //https://www.journaldev.com/10096/android-viewpager-example-tutorial

}
