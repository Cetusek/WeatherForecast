package pl.marek.weatherforecast.favourites;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.persistence.DBAsyncTask;
import pl.marek.weatherforecast.persistence.PlaceEntity;

import static android.R.id.list;


public class FavouritesAdapter extends ArrayAdapter<FavouritesListItem> {

    Context mContext;

    public FavouritesAdapter(Context context, FavouritesList list) {
        super(context, 0, list);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FavouritesListItem favouritesListItem = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.favourites_list_item, parent, false);
        TextView name = (TextView) layout.findViewById(R.id.FavouritesListItemName);
        name.setText(favouritesListItem.name);
        Button button = (Button)layout.findViewById(R.id.FavouritesListItemUp);
        button.setTag(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upPressed(Integer.parseInt(v.getTag().toString()));
            }
        });
        button = (Button)layout.findViewById(R.id.FavouritesListItemDown);
        button.setTag(position);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downPressed(Integer.parseInt(v.getTag().toString()));
            }
        });
        CheckBox checkBox = (CheckBox) layout.findViewById(R.id.FavouritesListCheckBox);
        checkBox.setTag(position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckBoxChanged(Integer.parseInt(buttonView.getTag().toString()), isChecked);
            }
        });
        ImageButton imageButton = (ImageButton) parent.findViewById(R.id.FavoutitesActivityDeleteButton);
        return layout;
    }

    private void upPressed(int position) {
        if (position > 0) {
            FavouritesList list = getList();
            clear();
            FavouritesListItem item = list.get(position - 1);
            list.set(position - 1, list.get(position));
            list.set(position, item);
            addAll(list);
            notifyDataSetChanged();
        }
    }

    private void downPressed(int position) {
        if (position + 1 < getCount()) {
            FavouritesList list = getList();
            clear();
            FavouritesListItem item = list.get(position + 1);
            list.set(position + 1, list.get(position));
            list.set(position, item);
            addAll(list);
            notifyDataSetChanged();
        }
    }

    private FavouritesList getList() {
        FavouritesList list = new FavouritesList();
        for (int i = 0; i < getCount(); i++) {
            list.add(getItem(i));
        }
        return list;
    }

    private void onCheckBoxChanged(int position, boolean isChecked) {
        getItem(position).isChecked = isChecked;
    }

    public void deleteSelectedItems() {
        FavouritesList list = getList();
        clear();
        FavouritesListItem item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (!item.isChecked) {
                add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void saveItems(String callerId, DBAsyncTask.Callback callback) {
        PlaceEntity[] places = new PlaceEntity[getCount()];
        for (int i = 0; i < getCount(); i++) {
            places[i] = getItem(i).getPlace();
        }
        DBAsyncTask task = new DBAsyncTask(callerId, callback);
        task.replacePlaces(places);
    }

    private void logList() {
        Log.i("MY_APP", "------------------ LIST -------------------");
        for (int i = 0; i < getCount(); i++) {
            Log.i("MY_APP", getItem(i).toString());
        }
    }

}
