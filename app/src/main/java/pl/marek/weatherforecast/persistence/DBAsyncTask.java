package pl.marek.weatherforecast.persistence;


import android.os.AsyncTask;

public class DBAsyncTask extends AsyncTask{

    private String callerId;
    private Callback callback;
    private DBAsyncTaskType taskType;

    public DBAsyncTask(String callerId, Callback callback) {
        this.callerId = callerId;
        this.callback = callback;
    }

    public void insertPlaceEntity(PlaceEntity placeEntity) {
        execute(DBAsyncTaskType.INSERT_PLACE, placeEntity);
    }

    public void selectPlaces() {
        execute(DBAsyncTaskType.SELECT_PLACES);
    }

    public void replacePlaces(PlaceEntity[] places) {
        execute(DBAsyncTaskType.REPLACE_PLACES, places);
    }

    public void countPlaces() {
        execute(DBAsyncTaskType.COUNT_PLACES, null);
    }

    private Long _insertPlaceEntity(PlaceEntity placeEntity) {
        AppDatabase db = AppDatabase.getInstance();
        return db.AppDao().insertPlace(placeEntity);
    }

    private PlaceEntity[] _selectPlaces() {
        AppDatabase db = AppDatabase.getInstance();
        return db.AppDao().selectPlaces();
    }

    private long[] _replacePlaces(PlaceEntity[] places) {
        AppDatabase db = AppDatabase.getInstance();
        db.AppDao().deletePlaces();
        return db.AppDao().insertPlaces(places);
    }

    private int _countPlaces() {
        AppDatabase db = AppDatabase.getInstance();
        return db.AppDao().countPlaces();
    }


    @Override
    protected Object doInBackground(Object[] params) {
        Object result = null;
        switch ((DBAsyncTaskType) params[0]) {
            case INSERT_PLACE:
                result = _insertPlaceEntity((PlaceEntity)params[1]);
                break;
            case SELECT_PLACES:
                result = _selectPlaces();
                break;
            case REPLACE_PLACES:
                result = _replacePlaces((PlaceEntity[]) params[1]);
                break;
            case COUNT_PLACES:
                result = _countPlaces();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (callback != null) {
            callback.DBAsyncTaskFinished(callerId, o);
        }
    }

    private enum DBAsyncTaskType {
        INSERT_PLACE,
        SELECT_PLACES,
        REPLACE_PLACES,
        COUNT_PLACES
    }

    public interface Callback {
        public void DBAsyncTaskFinished(String callerId, Object result);
        public void DBAsyncTaskError(String callerId, String message);
    }

}
