package pl.marek.weatherforecast.main;

import android.Manifest;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.crash.CustomExceptionHandler;
import pl.marek.weatherforecast.favourites.FavouritesActivity;
import pl.marek.weatherforecast.map.MapActivity;
import pl.marek.weatherforecast.permission.PermissionTools;
import pl.marek.weatherforecast.persistence.AppDatabase;
import pl.marek.weatherforecast.persistence.DBAsyncTask;
import pl.marek.weatherforecast.presenter.PresenterAction;
import pl.marek.weatherforecast.presenter.PresenterActivity;
import pl.marek.weatherforecast.presenter.PresenterParameters;
import pl.marek.weatherforecast.storm.StormActivity;

public class MainActivity extends AppCompatActivity implements DBAsyncTask.Callback{

    ImageButton buttonMap;
    ImageButton buttonFavourites;
    ImageButton buttonPresenter;
    ImageButton buttonStorms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomExceptionHandler.assignHandler(this);
        setContentView(R.layout.activity_main);
        mapGUI();
        checkPermissions();
        initDBInstance();
        checkFavourites();
    }

    private void mapGUI() {
        buttonMap = (ImageButton) findViewById(R.id.MainActivityButtonMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMapClicked();
            }
        });
        buttonFavourites = (ImageButton) findViewById(R.id.MainActivityButtonFavourites);
        buttonFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFavouritesPressed();
            }
        });
        buttonPresenter = (ImageButton) findViewById(R.id.MainActivityButtonPresenter);
        buttonPresenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPresenterPressed();
            }
        });
        buttonStorms = (ImageButton) findViewById(R.id.MainActivityButtonStorms);
        buttonStorms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStormsPressed();
            }
        });

    }

    private void buttonMapClicked() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    private void buttonFavouritesPressed() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
    }

    private void buttonPresenterPressed() {
        Intent intent = new Intent(this, PresenterActivity.class);
        PresenterParameters parameters = new PresenterParameters();
        parameters.action = PresenterAction.SHOW_FOR_FAVOURITES;
        intent.putExtra("PARAMETERS", parameters);
        startActivity(intent);
    }

    private void buttonStormsPressed() {
        Intent intent = new Intent(this, StormActivity.class);
        startActivity(intent);
    }

    private void checkPermissions() {
        String[] permissions = getNeededPermissions();
        if (!PermissionTools.arePermissionGranted(this, permissions)) {
            PermissionTools.requestPermissions(this, permissions);
        }
    }

    private String[] getNeededPermissions() {
        return new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionTools.arePermissionGranted(this, permissions)) {
            PermissionTools.showAlertNoPermissions(this);
            finish();
        }
    }

    private void initDBInstance() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
    }


    private void checkFavourites() {
        DBAsyncTask task = new DBAsyncTask("CHECK_FAVOURITES", this);
        task.countPlaces();
    }

    @Override
    public void DBAsyncTaskFinished(String callerId, Object result) {
        Log.i("MY_APP", "DBAsyncTaskFinished = "+result.toString());
        if ((Integer)result > 0) {
            buttonPresenterPressed();
        }
    }

    @Override
    public void DBAsyncTaskError(String callerId, String message) {

    }
}
