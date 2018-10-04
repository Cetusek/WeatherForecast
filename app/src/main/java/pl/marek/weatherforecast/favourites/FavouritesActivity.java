package pl.marek.weatherforecast.favourites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.persistence.DBAsyncTask;
import pl.marek.weatherforecast.persistence.PlaceEntity;

public class FavouritesActivity extends AppCompatActivity implements DBAsyncTask.Callback {

    private final String TASK_LIST_LOAD = "LIST_LOAD";
    private final String TASK_SAVE_LIST = "LIST_SAVE";

    ListView listView;
    FavouritesAdapter adapter;
    ImageButton deleteButton;
    ImageButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        mapGUI();
        createFavouritesList();
    }

    private void mapGUI() {
        listView = (ListView) findViewById(R.id.FavouritesActivityList);
        deleteButton = (ImageButton) findViewById(R.id.FavoutitesActivityDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonPressed();
            }
        });
        saveButton = (ImageButton) findViewById(R.id.FavoutitesActivitySaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonPressed();
            }
        });
    }

    private void createFavouritesList() {
        DBAsyncTask task = new DBAsyncTask(TASK_LIST_LOAD, this);
        task.selectPlaces();
    }

    private void attachAdapter(FavouritesList list) {
        adapter = new FavouritesAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void DBAsyncTaskFinished(String callerId, Object result) {
        switch (callerId) {
            case TASK_LIST_LOAD:
                PlaceEntity[] places = (PlaceEntity[]) result;
                FavouritesList list = new FavouritesList();
                for(int i = 0; i < places.length; i++) {
                    list.addFavouritePlace(places[i]);
                }
                attachAdapter(list);
                break;
            case TASK_SAVE_LIST:
                Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    @Override
    public void DBAsyncTaskError(String callerId, String message) {

    }

    private void onDeleteButtonPressed() {
        adapter.deleteSelectedItems();
    }

    private void onSaveButtonPressed() {
        adapter.saveItems(TASK_SAVE_LIST, this);
    }
}
