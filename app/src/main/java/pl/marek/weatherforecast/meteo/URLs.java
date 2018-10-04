package pl.marek.weatherforecast.meteo;


import com.google.android.gms.maps.model.LatLng;

public class URLs {

    private static final String URL_COORDINATES = "https://nowe.meteo.pl/search_mgram_pos?lat=$$LATITUDE$$&lon=$$LONGITUDE$$&model=0";
    private static final String URL_IMAGE = "https://www.meteo.pl/um/metco/mgram_pict.php?ntype=0u&row=$$ROW$$&col=$$COL$$&lang=pl";

    public static String getCoordinatesURL(LatLng location) {
        String result = URL_COORDINATES;
        result = result.replace("$$LATITUDE$$", Double.toString(location.latitude));
        result = result.replace("$$LONGITUDE$$", Double.toString(location.longitude));
        return result;
    }

    public static String getImageURL(Coordinates coordinates) {
        String result = URL_IMAGE;
        result = result.replace("$$ROW$$", Integer.toString(coordinates.row));
        result = result.replace("$$COL$$", Integer.toString(coordinates.col));
        return result;
    }

}
