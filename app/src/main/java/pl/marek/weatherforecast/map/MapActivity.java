package pl.marek.weatherforecast.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.network.DownloadCoordinates;
import pl.marek.weatherforecast.network.DownloadManager;
import pl.marek.weatherforecast.persistence.DBAsyncTask;
import pl.marek.weatherforecast.persistence.PlaceEntity;
import pl.marek.weatherforecast.presenter.PresenterAction;
import pl.marek.weatherforecast.presenter.PresenterActivity;
import pl.marek.weatherforecast.presenter.PresenterParameters;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DBAsyncTask.Callback, PlaceDialog.PlaceDialogListener {

    public static String NAME_KEY = "name";

    private MapView mapView;
    private GoogleMap gmap;
    private MarkerOptions markerOption;
    private Marker marker;
    private Button buttonAddToFavourites;
    private Button buttonShowPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapGUI();
        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    private void mapGUI() {

        mapView = (MapView) findViewById(R.id.MapActivityMapView);
        buttonAddToFavourites = (Button) findViewById(R.id.MapActivityButtonAddToFavourites);
        buttonAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddToFavouritesPressed();
            }
        });
        buttonShowPresenter = (Button) findViewById(R.id.MapActivityButtonShowForecast);
        buttonShowPresenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonShowPresenterPressed();
            }
        });

    }

    private String getLocationName(LatLng location) {
        String result = "?";
        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geo.getFromLocation(location.latitude, location.longitude, 1);
            if (!addressList.isEmpty() && addressList.get(0).getLocality() != null) {
                result = addressList.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        LatLng ny = new LatLng(51.759248, 19.455735);
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny, 8.0f));
        markerOption = new MarkerOptions();
        markerOption.position(ny);
        marker = gmap.addMarker(markerOption);
        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                onCameraChangeA();
            }
        });
    }

    private void onCameraChangeA() {
        marker.setPosition(gmap.getCameraPosition().target);
    }

    private void buttonAddToFavouritesPressed() {
        /*
        Log.i("MY_APP", marker.getPosition().toString());
        Log.i("MY_APP", getLocationName(marker.getPosition()));
        PlaceEntity place = new PlaceEntity();
        place.name = getLocationName(marker.getPosition());
        place.latitude =  marker.getPosition().latitude;
        place.longitude = marker.getPosition().longitude;
        DBAsyncTask task = new DBAsyncTask(null, this);
        task.insertPlaceEntity(place);
        */
        PlaceDialog dialog = new PlaceDialog();
        Bundle bundle = new Bundle();
        bundle.putString(NAME_KEY, getLocationName(marker.getPosition()));
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), null);
    }

    private void setButtonShowPresenterPressed() {
        Intent intent = new Intent(this, PresenterActivity.class);
        PresenterParameters parameters = new PresenterParameters();
        parameters.action = PresenterAction.SHOW_FOR_ONE;
        parameters.latitude = marker.getPosition().latitude;
        parameters.longitude = marker.getPosition().longitude;
        parameters.name = getLocationName(marker.getPosition());
        intent.putExtra("PARAMETERS", parameters);
        startActivity(intent);

    }

    @Override
    public void DBAsyncTaskFinished(String callerId, Object result) {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.added_to_favourites), Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void DBAsyncTaskError(String callerId, String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void insertNewPlace(String name) {
        PlaceEntity place = new PlaceEntity();
        place.name = name;
        place.latitude =  marker.getPosition().latitude;
        place.longitude = marker.getPosition().longitude;
        DBAsyncTask task = new DBAsyncTask(null, this);
        task.insertPlaceEntity(place);
    }

    @Override
    public void onPlaceDialogOkPressed(String name) {
        insertNewPlace(name);
    }

    @Override
    public void onPlaceDialogCancelPressed() {

    }
}
