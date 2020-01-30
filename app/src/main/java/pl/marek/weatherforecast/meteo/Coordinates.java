package pl.marek.weatherforecast.meteo;

import com.google.android.gms.maps.model.LatLng;

public class Coordinates {

    private final int MIN_ROW = 17;
    private final int MAX_ROW = 598;
    private final int MIN_COL = 17;
    private final int MAX_COL = 430;
    private final int STEP = 7;

    private final double MIN_LATITUDE = 44.7666666666666667;
    private final double MAX_LATITUDE = 66.3166666666666667;
    private final double MIN_LONGITUDE = 4.1166666666666667;
    private final double MAX_LONGITUDE = 34.6166666666666667;

    private int row;
    private int col;
    private int attemptNo = 0;

    private int[][] neighbours = {{0, 0}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row + neighbours[attemptNo][0];
    }

    public int getCol() {
        return col + neighbours[attemptNo][1];
    }

    public String toString() {
        return "row = "+Integer.toString(row)+" col = "+Integer.toString(col);
    }

    public void increaseAttempt() {
        attemptNo++;
    }

    public int getAttemptNo() {
        return attemptNo + 1;
    }

    public int getMaxAttempt() {
        return neighbours.length;
    }

    public String getCoordinates() {
        return getRow()+" "+getCol();
    }

    public void set(LatLng location) {
        double lat_row = (MAX_LATITUDE - location.latitude) / (MAX_LATITUDE - MIN_LATITUDE);
        row = (int) Math.round(lat_row * (MAX_ROW - MIN_ROW) / STEP) * STEP + MIN_ROW;
        double lon_col = (location.longitude - MIN_LONGITUDE) / (MAX_LONGITUDE - MIN_LONGITUDE);
        col = (int) Math.round(lon_col * (MAX_COL - MIN_COL) / STEP) * STEP + MIN_COL;
    }


}
