package pl.marek.weatherforecast.crash;


import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler{

    public static void assignHandler(Context context) {
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(context));
        }
    }


    private Context context;
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public CustomExceptionHandler(Context context) {
        this.context = context;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            StringBuffer trace = new StringBuffer();
            trace.append(writer.toString());
            trace.append("------------------------------------------\n");
            trace.append(Log.getStackTraceString(ex));
            Log.i("MY_APP", "uncaught exception: "+trace.toString());
            AutoSaver.getInstance().saveCrash(trace.toString());
            showAlert(trace.toString());
        }
        catch (Exception e) {
        }
        defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }


    private void showAlert(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

 }
