package pl.marek.weatherforecast.gios;

import java.util.ArrayList;

public class GIOSStationList extends ArrayList<GIOSStation> {


    public GIOSStationList() {
        super();
    }

    public GIOSStation getClosestStation(double latitude, double longitude) {
        GIOSStation result = null;
        double lastDistance = Double.MAX_VALUE;
        double currentDistance;
        for (GIOSStation station : this) {
            currentDistance = (latitude - station.gegrLat)*(latitude - station.gegrLat) + (longitude - station.gegrLon)*(longitude - station.gegrLon);
            if (currentDistance < lastDistance) {
                result = station;
                lastDistance = currentDistance;
            }
        }
        return result;
    }

}
