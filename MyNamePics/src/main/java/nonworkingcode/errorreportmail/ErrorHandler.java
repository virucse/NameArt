package nonworkingcode.errorreportmail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collection;

public class ErrorHandler implements UncaughtExceptionHandler {
    static String recipients = "care.formationapps@gmail.com";
    static String subject = null;
    private static volatile boolean _isRegistered;
    private Context _context;
    private UncaughtExceptionHandler _original = Thread.getDefaultUncaughtExceptionHandler();

    private ErrorHandler(Context context) {
        this._context = context;
    }

    public static synchronized void register(Context context, String subject1) {
        synchronized (ErrorHandler.class) {
            if (!_isRegistered) {
                _isRegistered = true;
                subject = subject1;
                Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler(context.getApplicationContext()));
            }
        }
    }

    public static void feedback(Context context, String subject, String body) {
        Collection data = new ArrayList(10);
        data.add("Device model: " + Build.MODEL);
        data.add("Firmware version: " + VERSION.RELEASE);
        data.add("Brand: " + Build.BRAND);
        data.add("App version:" + getVersion(context));
        String content = StringUtils.join(data, ',');
        Intent sendIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:" + recipients));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//268435456
        sendIntent.putExtra("android.intent.extra.SUBJECT", subject);
        if (body != null) {
            content = new StringBuilder(String.valueOf(content)).append("\n").append(body).toString();
        }
        sendIntent.putExtra("android.intent.extra.TEXT", content);
        try {
            context.startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "1.0";
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(baos));
        feedback(this._context, subject, baos.toString());
        if (this._original != null) {
            this._original.uncaughtException(thread, ex);
        }
    }
}
