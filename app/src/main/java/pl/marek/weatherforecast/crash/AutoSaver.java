package pl.marek.weatherforecast.crash;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AutoSaver {

    private static AutoSaver instance;

    private String localPath;
    private String prefix;

    private AutoSaver () {
        //this.localPath = Environment.getExternalStorageDirectory().getPath()+"/trace";
        //this.localPath = "/sdcard/trace";
        localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/trace";
    }

    public static AutoSaver getInstance() {
        if (instance == null) {
            instance = new AutoSaver();
        }
        return instance;
    }

    private void saveToFile(String text, String fileName) {
        File file = new File(localPath+'/'+fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveCrash(String text) {
        prefix = "Weatherforecast_crash_";
        saveToFile(text, generateFileName());
    }


    public void saveInfo(String text) {
        prefix = "Weatherforecast_info_";
        saveToFile(text, generateFileName());
    }

    private String generateFileName() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date());
        return prefix+timestamp+".txt";
    }

    public void saveCallStack() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StringBuffer stringBuffer = new StringBuffer();
        for (StackTraceElement element : elements) {
            stringBuffer.append(element.toString());
            stringBuffer.append("\n");
        }
        prefix = "Weatherforecast_call_stack_";
        saveToFile(stringBuffer.toString(), generateFileName());
    }


}
