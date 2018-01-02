package com.example.shanbay;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 上官轩明 on 2017/12/10.
 */

 /*
    * Service 不能是内部类，否则会抛出运行时异常不能实例化service，因为没有无参的构造方法，即使你写了
    * IntentService 必须要有无参构造方法且在其内部调用父类的super（String）否则也会抛出异常
    * */

public class ClockService extends Service {

    private Intent intent;
    private MyBinder myBinder = new MyBinder();
    private Calendar calendar = MainActivity.calendar;
    public static final String TAG = "woggle";
    String title = "运行中";

    public ClockService() {
        super();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
            /*
            * 利用Notification将该服务设置为前台服务
            * */

        Intent intent = new Intent(this, MainActivity.class);//MainActivity设置为singleInstance模式
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        MainActivity.notification = new Notification
                .Builder(this)
                .setContentTitle("ShanBay")
                .setContentText("运行中")
                .setWhen(System.currentTimeMillis())//是否显示时间
                .setSmallIcon(R.mipmap.ic_launcher)//必须设置该属性，通知才有显示
                .setContentIntent(pi)//当该notification被点击时会跳转到MainActivity
                .build();
        startForeground(1, MainActivity.notification);//显示
        Toast.makeText(this, "服务已启动", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override

        /*service中的代码都是默认运行在主线程的，
        * 如果直接在服务里去处理一些耗时逻辑，很容易出现ANR
        * ApplicationNotRespondiing
        */
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();//不加这行会RuntimeError，非主线程不能修改UI
                myBinder.setNotificationText("运行中", myBinder.getAlarm());
                check();
                Looper.loop();
            }
        }).start();
        // Toast.makeText(this, "onstart", Toast.LENGTH_SHORT).show();
        return START_REDELIVER_INTENT;//保证该服务被kill后立即重启，只要app不死，服务不灭
        //return super.onStartCommand(intent,flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "服务被杀掉了", Toast.LENGTH_SHORT).show();
        startService(intent);//我也不想写流氓软件。。。
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    /////////////////////////////////////////////////////打卡////////////////////////////////////////////////////////////
    public void checkin() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .build();
        RequestBody requestBody = new FormBody
                .Builder()
                .build();
        final Request request = new Request.Builder()
                .url("https://www.shanbay.com/api/v1/checkin/?for_web=true")
                .addHeader("Connection","keep-alive")
                .addHeader("Origin", " https://www.shanbay.com")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36")
                .addHeader("X-CSRFToken", "null")
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "auth_token=" + MainActivity.auth_token)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: 打卡Failure");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    if(result.contains("SUCCESS"))
                    {
                        myBinder.setNotificationText("已打卡", myBinder.getAlarm());
                    }
                    Log.d(TAG, "onResponse: " + result);
                } else {
                    Log.d(TAG, "onResponse: 打卡isn't success");
                }
            }
        });
    }
/////////////////////////////////////////////////////打卡////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////查卡///////////////////////////////////////////////////////////
    public void check() {
        Log.d(TAG, "check: 正在查卡");
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .build();
        final Request request = new Request.Builder()
                .url("https://www.shanbay.com/api/v1/checkin/?for_web=true&_=" + System.currentTimeMillis())
                .addHeader("Accept", "*/*")
                .addHeader("Origin", " https://www.shanbay.com")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36")
                .addHeader("X-CSRFToken", "null")
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "auth_token=" + MainActivity.auth_token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("woggle", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    CheckDataBean checkDataBean = gson.fromJson(result, new TypeToken<CheckDataBean>() {
                    }.getType());
                    Log.d(TAG, "onResponse: 查卡结果 " + "finsih = " + checkDataBean.getData().isFinished() + "  check = " + checkDataBean.getData().isChecked());

                    if (checkDataBean.getData().isChecked()) {
                        Log.d(TAG, "onResponse: 已打卡");
                        myBinder.setNotificationText("已打卡", myBinder.getAlarm());
                    } else if (!checkDataBean.getData().isFinished()) {
                        Log.d(TAG, "onResponse: 快去学习");
                        myBinder.setNotificationText("还未学习", myBinder.getAlarm());
                        sendAlarmBroadcast();
                        Looper.prepare();
                        Toast.makeText(ClockService.this, "快去学习", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Log.d(TAG, "onResponse: 可以打卡");
                        checkin();
                    }

                } else {
                    Log.d(TAG, "onResponse: 查卡reponseisn't successed");
                }
            }
        });
    }

///////////////////////////////////////////////////////查卡///////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////发送广播设置Alarm/////////////////////////////////////////////////////////
    private void sendAlarmBroadcast() {
        Intent intent = new Intent();
        intent.putExtra("from", "service");
        intent.setAction("android.intent.action.MyService");
        sendBroadcast(intent);
    }
/////////////////////////////////////////////////////发送广播设置Alarm/////////////////////////////////////////////////////////


    class MyBinder extends Binder {
        public void setNotificationText(String title, String text) {
            //一个通知必须要包含以下三个属性，否则或报参数异常
ClockService.this.title = title;
            Intent intent = new Intent(ClockService.this, MainActivity.class);//MainActivity设置为singleInstance模式
            PendingIntent pi = PendingIntent.getActivity(ClockService.this, 0, intent, 0);
            MainActivity.notiCptBuilder = new NotificationCompat.Builder(ClockService.this, "ShanBay")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(ClockService.this.title)
                    .setContentText(text)
                    .setContentIntent(pi);
            MainActivity.notifyMgr.notify(1, MainActivity.notiCptBuilder.build());
            //Log.d(TAG, "setNotificationText: " + title + text);
        }
        public void setNotificationText( String text) {
            //一个通知必须要包含以下三个属性，否则或报参数异常
            Intent intent = new Intent(ClockService.this, MainActivity.class);//MainActivity设置为singleInstance模式
            PendingIntent pi = PendingIntent.getActivity(ClockService.this, 0, intent, 0);
            MainActivity.notiCptBuilder = new NotificationCompat.Builder(ClockService.this, "ShanBay")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(ClockService.this.title)
                    .setContentText(text)
                    .setContentIntent(pi);
            MainActivity.notifyMgr.notify(1, MainActivity.notiCptBuilder.build());
            //Log.d(TAG, "setNotificationText: " + title + text);
        }


        @TargetApi(Build.VERSION_CODES.N)
        public String getAlarm() {
            return "AL：" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";
        }


    }


}

