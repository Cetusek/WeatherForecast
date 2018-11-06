package pl.marek.weatherforecast.gios;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GIOSSensor {

    public int id;
    public int stationId;
    public String paramName;
    public String paramFormula;
    public String paramCode;
    public ArrayList<GIOSSensorValue> values;


    public GIOSSensor(JSONObject json) {
        try {
            id = json.getInt("id");
            stationId = json.getInt("stationId");
            paramName = json.getJSONObject("param").getString("paramName");
            paramFormula = json.getJSONObject("param").getString("paramFormula");
            paramCode = json.getJSONObject("param").getString("paramCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getValue() {
        String result = "";
        if (values != null) {
            if (values.size() > 0) {
                result = values.get(0).value;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "GIOSSensor{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", paramName='" + paramName + '\'' +
                ", paramFormula='" + paramFormula + '\'' +
                ", paramCode='" + paramCode + '\'' +
                '}';
    }
}
