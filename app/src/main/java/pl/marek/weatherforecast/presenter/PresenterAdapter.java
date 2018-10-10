package pl.marek.weatherforecast.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.marek.weatherforecast.R;


public class PresenterAdapter extends PagerAdapter {

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
        PresenterListItem presenterListItem = presenterList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.presenter_page, container, false);
        layout.setTag(getTagForPosition(position));
        deployLogText(layout, "");
        TextView pageName = (TextView) layout.findViewById(R.id.PresenterPageName);
        pageName.setText(presenterListItem.name);
        if (presenterListItem.bitmap != null) {
            deployImage(layout, presenterListItem.bitmap);
        }
        container.addView(layout);
        return layout;
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

    private String getTagForPosition(int position) {
        return "PresenterView"+position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public PresenterListItem getPresenterListItem(int position) {
        return presenterList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onImageLoaded(Bitmap bitmap, int position, String logText) {
        PresenterListItem item = getPresenterListItem(position);
        item.bitmap = bitmap;
        ViewGroup viewGroup = (ViewGroup) container.findViewWithTag(getTagForPosition(position));
        deployImage(viewGroup, item.bitmap);
        deployLogText(viewGroup, logText);
    }

}
