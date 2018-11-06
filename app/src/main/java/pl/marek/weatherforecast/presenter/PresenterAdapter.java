package pl.marek.weatherforecast.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.gios.GIOSAirQualityIndex;
import pl.marek.weatherforecast.gios.GIOSSensor;
import pl.marek.weatherforecast.gios.GIOSStation;
import pl.marek.weatherforecast.gios.GIOSStationPresenter;
import pl.marek.weatherforecast.network.DownloadGIOSManager;


public class PresenterAdapter extends PagerAdapter implements DownloadGIOSManager.DownloadGIOSManagerCallback {

    private PresenterList presenterList;
    private Context mContext;
    ViewGroup container;

    public PresenterAdapter(Context context) {
        presenterList = new PresenterList();
        this.mContext = context;
    }

    public void addItem(PresenterListItem item) {
        presenterList.add(item);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (presenterList != null) {
            count = presenterList.size();
        }
        return count;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.container = container;
        View layout = container.getChildAt(position);
        if (layout != null) {
            return layout;
        }
        PresenterListItem presenterListItem = presenterList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.presenter_page, container, false);
        viewGroup.setTag(getTagForPosition(position));
        deployLogText(viewGroup, "");
        TextView pageName = (TextView) viewGroup.findViewById(R.id.PresenterPageName);
        pageName.setText(presenterListItem.name);
        if (presenterListItem.bitmap != null) {
            deployImage(viewGroup, presenterListItem.bitmap);
        }
        TextView airQualityIndexTextView = (TextView) viewGroup.findViewById(R.id.PresenterPageAirQualityIndexTextView);
        airQualityIndexTextView.setTag(position);
        airQualityIndexTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onAirQualityIndexClick((Integer) v.getTag());
            }
        });
        container.addView(viewGroup);
        return viewGroup;
    }

    private void onAirQualityIndexClick(int position) {
        PresenterListItem item = getPresenterListItem(position);
        if (item.GIOSStation != null) {
            Intent intent = new Intent(mContext, GIOSStationPresenter.class);
            intent.putExtra("GIOSStation", item.GIOSStation);
            mContext.startActivity(intent);
        }
    }

    private void deployImage(ViewGroup layout, Bitmap bitmap) {
        ImageView imageView = (ImageView) layout.findViewById(R.id.PresenterPageImageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
    }

    private void deployLogText(ViewGroup layout, String logText) {
        TextView textView = (TextView) layout.findViewById(R.id.PresenterPageLog);
        textView.setText(logText);
    }

    private void deployAirQualityIndex(ViewGroup layout, String indexLevelName) {
        if (layout != null) {
            TextView textView = (TextView) layout.findViewById(R.id.PresenterPageAirQualityIndexTextView);
            textView.setText(mContext.getResources().getString(R.string.air_quality_index)+" "+indexLevelName);
        }
    }

    private void deployAirQualityIndexLoading(ViewGroup layout) {
        if (layout != null) {
            TextView textView = (TextView) layout.findViewById(R.id.PresenterPageAirQualityIndexTextView);
            textView.setText(mContext.getResources().getString(R.string.air_quality_index_downloading));
        }
    }


    private String getTagForPosition(int position) {
        return "PresenterView"+position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public PresenterListItem getPresenterListItem(int position) {
        return presenterList.get(position);
    }


    private ViewGroup getViewForPosition(int position) {
        ViewGroup result = null;
        if (container != null) {
            result = (ViewGroup) container.findViewWithTag(getTagForPosition(position));
        }
        return result;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onImageLoaded(Bitmap bitmap, int position, String logText) {
        PresenterListItem item = getPresenterListItem(position);
        item.bitmap = bitmap;
        if (bitmap != null) {
            deployImage(getViewForPosition(position), item.bitmap);
        }
        deployLogText(getViewForPosition(position), logText);
    }

    public void loadClosestStation(int position) {
        deployAirQualityIndexLoading(getViewForPosition(position));
        PresenterListItem item = getPresenterListItem(position);
        DownloadGIOSManager downloadGIOSManager = new DownloadGIOSManager(this);
        downloadGIOSManager.downloadClosestStation(position, item.getLatLng().latitude, item.getLatLng().longitude);
    }

    @Override
    public void GIOSClosestStationDownloaded(int position, GIOSStation GIOSStation) {
        if (GIOSStation != null) {
            PresenterListItem item = getPresenterListItem(position);
            item.GIOSStation = GIOSStation;
            loadAirQualityIndex(position);
        }
    }

    @Override
    public void GIOSSensorsDownloaded(ArrayList<GIOSSensor> list) {

    }

    @Override
    public void GIOSSensorDataDownloaded(GIOSSensor sensor) {

    }


    public void loadAirQualityIndex(int position) {
        deployAirQualityIndexLoading(getViewForPosition(position));
        PresenterListItem item = getPresenterListItem(position);
        if (item.GIOSStation == null) {
            loadClosestStation(position);
        }
        else {
            DownloadGIOSManager downloadGIOSManager = new DownloadGIOSManager(this);
            downloadGIOSManager.downloadAirQualityIndexForStation(position, item.GIOSStation);
        }

    }



    public void onAirQualityIndexLoaded(int position, String airQualityIndexLevelName) {
        deployAirQualityIndex(getViewForPosition(position), airQualityIndexLevelName);
    }


    @Override
    public void GIOSAirQualityIndexDownloaded(int position, GIOSAirQualityIndex airQualityIndex) {
        PresenterListItem item = getPresenterListItem(position);
        String airQualityIndexLevelName = "???";
        if (airQualityIndex != null) {
            airQualityIndexLevelName = airQualityIndex.indexLevelName;
        }
        onAirQualityIndexLoaded(position, airQualityIndexLevelName);
    }


    @Override
    public void GIOSErrorOccurred(String message) {

    }
}
