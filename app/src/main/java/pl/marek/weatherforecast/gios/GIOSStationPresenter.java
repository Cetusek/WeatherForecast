package pl.marek.weatherforecast.gios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.marek.weatherforecast.R;
import pl.marek.weatherforecast.network.DownloadGIOSManager;

public class GIOSStationPresenter extends AppCompatActivity implements DownloadGIOSManager.DownloadGIOSManagerCallback{

    private GIOSStation station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giosstation_presenter);
        deployStationInfo();
        loadSensors();
    }

    private void loadSensors() {
        DownloadGIOSManager downloadGIOSManager = new DownloadGIOSManager(this);
        downloadGIOSManager.downloadSensors(station.id);
    }

    private void deployStationInfo() {
        TextView stationInfo = (TextView) findViewById(R.id.GIOSStationPresenterStationInfoTextView);
        station = (GIOSStation) getIntent().getSerializableExtra("GIOSStation");
        stationInfo.setText(Html.fromHtml(station.stationName+"<BR>"+station.cityName+"<BR>"+station.addressStreet));
    }

    private void deploySensor(GIOSSensor sensor) {
        ViewGroup parentLayout = (ViewGroup) findViewById(R.id.GIOSStationPresenterContainer);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.giosstation_presenter_station_parameter, parentLayout, false);
        TextView t = (TextView) layout.findViewById(R.id.GIOSStationPresenterStationParameterNameTextView);
        t.setText(sensor.paramName);
        t = (TextView) layout.findViewById(R.id.GIOSStationPresenterStationParameterValueTextView);
        t.setText(sensor.getValue());
        parentLayout.addView(layout);
    }

    @Override
    public void GIOSAirQualityIndexDownloaded(int position, GIOSAirQualityIndex airQualityIndex) {

    }

    @Override
    public void GIOSClosestStationDownloaded(int position, GIOSStation GIOSStation) {

    }

    @Override
    public void GIOSSensorsDownloaded(ArrayList<GIOSSensor> list) {
        for (GIOSSensor sensor : list) {
            DownloadGIOSManager manager = new DownloadGIOSManager(this);
            manager.downloadSensorData(sensor);
        }
    }

    @Override
    public void GIOSSensorDataDownloaded(GIOSSensor sensor) {
        deploySensor(sensor);
    }

    @Override
    public void GIOSErrorOccurred(String message) {

    }
}
