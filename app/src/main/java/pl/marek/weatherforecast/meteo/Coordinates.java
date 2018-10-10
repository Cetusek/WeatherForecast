package pl.marek.weatherforecast.meteo;

public class Coordinates {

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



}
