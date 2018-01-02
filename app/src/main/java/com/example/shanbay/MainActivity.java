package com.example.shanbay;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements ActivityView {
    private TextView textView;
    private Button button;
    private TextView tv_token;
    public static Notification notification;
    public static NotificationManager notifyMgr;
    public static NotificationCompat.Builder notiCptBuilder;
    public static String auth_token;
    public static Calendar calendar;
    private AlarmReceiver alarmReceiver;
    private IntentFilter intentFilter;
    private ClockService.MyBinder myBinder;
    private ServiceConnection connection;
    private PendingIntent pi;
    private AlarmManager alarm;
    private Intent intent_broadcast;
    private final static String TAG = "woggle";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inite();
        initeAlarm();
        setAlarm();


    }

    private void inite() {
        textView = findViewById(R.id.textview);
        tv_token = findViewById(R.id.tv_token);
        button = findViewById(R.id.button);
        notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        login();

////////////////////////////////////////////////Bind With Service///////////////////////////////////////////
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: ");
                myBinder = (ClockService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intentService = new Intent(MainActivity.this, ClockService.class);
        startService(intentService);
        bindService(intentService, connection, BIND_AUTO_CREATE);
////////////////////////////////////////////////Bind With Service///////////////////////////////////////////

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    ///////////////////////////////////////////////登陆//////////////////////////////////////////////////////

    private void login() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .build();
        final RequestBody requestBody = new FormBody
                .Builder()
                .add("username", "13821547562")
                .add("password", "zh1154097852")
                .build();
        final Request request = new Request.Builder()
                .url("https://www.shanbay.com/api/v1/account/login/web/")
                .addHeader("Accept", "*/*")
                .addHeader("Origin", " https://www.shanbay.com")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36")
                .addHeader("X-CSRFToken", "null")
                .addHeader("Content-Type", "application/json")
                .put(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showError("onResponseError");
                    return;
                }
                String result = response.body().string();
                String status_code = result.substring(result.indexOf("\"status_code\": ") + "\"status_code\": ".length(), result.indexOf("\"status_code\": ") + "\"status_code\": ".length() + 1);
                if (status_code.equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showText("登陆成功！");
                        }
                    });


                    List<String> cookies = response.headers("Set-Cookie");
                    for (String cookie : cookies) {
                        if (cookie.startsWith("auth_token")) {
                            auth_token = cookie.substring(cookie.indexOf("auth_token=") + "auth_token=".length());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToken(auth_token);
                                }
                            });

                            break;
                        }
                    }

                } else if (status_code.equals("1")) {
                    showText("验证码错误！");
                }

            }
        });
    }
    ///////////////////////////////////////////////登陆//////////////////////////////////////////////////////


    ///////////////////////////////////////////设置AlarmManager////////////////////////////////////////////////

    private void initeAlarm() {
        intent_broadcast = new Intent("android.intent.action.MyService");
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//获取Alarmmanager实例
        pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent_broadcast, 0);//PendingIntent包含了Intent需要执行的一些列start方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();//获取calendar实例，时间为该代码执行的时间
        }
    }

    private void setAlarm() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//获取Alarmmanager实例
//
//            Intent intent = new Intent("android.intent.action.MyService");
//            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);//PendingIntent包含了Intent需要执行的一些列start方法
            Calendar calendar_cur = Calendar.getInstance();//获取calendar实例，时间为该代码执行的时间
            //Log.d(TAG, "现在是" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "点");

            calendar.set(calendar_cur.get(Calendar.YEAR), calendar_cur.get(Calendar.MONTH), calendar_cur.get(Calendar.DAY_OF_MONTH), 0, 18);//将时间设置为当天23：00
            while (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.MINUTE, 1);
            }
            //Log.d(TAG, "闹钟是" + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "点");
            //calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.add(Calendar.SECOND, 10);

            alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

            if (myBinder != null) {
                Log.d(TAG, "setAlarm: 不为空");
                myBinder.setNotificationText(myBinder.getAlarm());
            } else {
                Log.d(TAG, "setAlarm: 为空");
            }
        }
    }
    ///////////////////////////////////////////设置AlarmManager////////////////////////////////////////////////


    /////////////////////////////////////////////BroadcastReceiver//////////////////////////////////////////
/*BroadcastReceiver
* 接收广播，同时起动服务发起网络请求
* 且注册文件内exported属性为false，不接受本程序以外的广播
* 如果是内部类
* 1.声明为静态
* 2.动态注册
* Service同理
* */

    public class AlarmReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d(TAG, "onReceive: 收到广播了");
            if ((intent.getStringExtra("from") != null) && intent.getStringExtra("from").equals("service")) {
                Log.d(TAG, "onReceive: 受到来自service的广播了");
                Log.d(TAG, "onReceive: " + calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分");
                setAlarm();
                return;
            }


            Intent intent1 = new Intent(MainActivity.this, ClockService.class);
            startService(intent1);
        }
    }
    /////////////////////////////////////////////BroadcastReceiver//////////////////////////////////////////


    @Override
    protected void onResume() {
        alarmReceiver = new AlarmReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MyService");
        registerReceiver(alarmReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        registerReceiver(alarmReceiver, intentFilter);
        super.onDestroy();
    }

    @Override
    public void showText(String content) {
        textView.setText("\n" + textView.getText() + content);
    }

    @Override
    public void showError(String error) {
        textView.setText("\n" + textView.getText() + error);
    }

    @Override
    public void showToken(String token) {
        tv_token.setText(token);
    }
}


/////////////////////////////////////////////////////Service/////////////////////////////////////////////////////////
    /*Service
        * Service 不能是内部类，否则会抛出运行时异常不能实例化service，因为没有无参的构造方法，即使你写了
        * IntentService 必须要有无参构造方法且在其内部调用父类的super（String）否则也会抛出异常
        * */


/////////////////////////////////////////////////////Service/////////////////////////////////////////////////////////


//    public static class ClockServier extends Service {
//
//        public ClockServier() {
//            super();
//        }
//
//        ;
//
//        @Override
//        public void onCreate() {
//            Log.d(TAG, "onCreate: ");
//            Intent intent = new Intent(this, MainActivity.class);
//            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
//            Notification notification = new Notification
//                    .Builder(this)
//                    .setContentTitle("ShanBay")
//                    .setContentText("运行中")
//                    .setWhen(System.currentTimeMillis())//是否显示时间
//                    .setSmallIcon(R.mipmap.ic_launcher)//必须设置该属性，通知才有显示
//                    .setContentIntent(pi)//当该notification被点击时会跳转到MainActivity
//                    .build();
//            startForeground(1, notification);
//            Log.d(TAG, "onCreate:1 ");
//            Toast.makeText(this, "1111", Toast.LENGTH_SHORT).show();
//            super.onCreate();
//        }
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            Log.d(TAG, "onStartCommand: ");
//            return super.onStartCommand(intent, flags, startId);
//
//        }
//
//        @Override
//        public void onDestroy() {
//            Log.d(TAG, "onDestroy: ");
//            Toast.makeText(this, "gg", Toast.LENGTH_SHORT).show();
//            super.onDestroy();
//        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//    }
//////////////////////////////////////////////////Service//////////////////////////////////////////////////////////


//    private TextView textView;
//    private Button button;
//    private String cookie;
//    private String result;
//    private String body = "{\"username\":\"13821547562\",\"password\":\"zh1154097852\",\"code\":\"k4lg\",\"key\":\"58f32922435931cde5b74ea9ee4ae6946b23ff55\"}";
//
//
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Headers headers = (Headers) msg.obj;
//            textView.setText(textView.getText()+headers.get("set"));
//            String cookie_token;
//            for (int i = 0; i < headers.size(); i++) {
//
//                cookie_token = headers.value(i);
//                if(cookie_token.contains("auth_token="))
//                {
//                    textView.setText(cookie_token);
//                    break;
//                }
//                //textView.setText(textView.getText() + headers.name(i) + ":" + headers.value(i) + "\n");
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        init();
//    }
//
//    private void init() {
//        textView = findViewById(R.id.textview);
//        button = findViewById(R.id.button);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                        .addInterceptor(new AddCookiesInterceptor(MainActivity.this))
////                        .addInterceptor(new SaveCookiesInterceptor(MainActivity.this))
//                        .build();
//
//                RequestBody requestBody = new FormBody
//                        .Builder()
//                        .add("username", "18526648753")
//                        .add("password", "zh1154097852")
//                        .build();
//                final Request request = new Request.Builder()
//                        //.url("https://www.shanbay.com/captcha/image/58f32922435931cde5b74ea9ee4ae6946b23ff55/")
//                        .url("https://www.shanbay.com/api/v1/account/login/web/")
//                        .addHeader("")
//                        .put(requestBody)
//                        .build();
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                        Toast.makeText(MainActivity.this, "gg", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            Toast.makeText(MainActivity.this,response.body().toString(), Toast.LENGTH_SHORT).show();
//                            Headers headers = response.headers();
//                            Message message = Message.obtain();
//                            message.obj = headers;
//                            handler.sendMessage(message);
//
//
//                        }
//                    }
//
//                });
//
//
//            }
//        });
//    }
//
//
////    class AddCookiesInterceptor implements Interceptor {
////        @Override
////        public Response intercept(Chain chain) throws IOException {
////            //Request.Builder builder = chain.request().newBuilder();
////            //HashSet set = (HashSet) Shared   .getDefaultPreferences().getStringSet(Preferences.PREF_COOKIES, new HashSet<>());
////            okhttp3.Request request = chain.request();
////            Response response = chain.proceed(request);
////            //set-cookie可能为多个
////            if (!response.headers("set-cookie").isEmpty()) {
////                List<String> cookies = response.headers("set-cookie");
////                String cookie = encodeCookie(cookies);
////                saveCookie(request.url().toString(),request.url().host(),cookie);
////        }
////    }
//
////
////    public class SaveCookiesInterceptor implements Interceptor {
////        private static final String COOKIE_PREF = "cookies_prefs";
////        private Context mContext;
////
////        public SaveCookiesInterceptor(Context context) {
////            mContext = context;
////        }
////
////        @Override
////        public Response intercept(Chain chain) throws IOException {
////            Request request = chain.request();
////            Response response = chain.proceed(request);
////            //set-cookie可能为多个
////            if (!response.headers("set-cookie").isEmpty()) {
////                List<String> cookies = response.headers("set-cookie");
////                cookie = encodeCookie(cookies);
////                // saveCookie(request.url().toString(),request.url().host(),cookie);
////            }
////
////            return response;
////        }
////
////        //整合cookie为唯一字符串
////        private String encodeCookie(List<String> cookies) {
////            StringBuilder sb = new StringBuilder();
////            Set<String> set = new HashSet<>();
////            for (String cookie : cookies) {
////                String[] arr = cookie.split(";");
////                for (String s : arr) {
////                    if (set.contains(s)) continue;
////                    set.add(s);
////
////                }
////            }
////
////            Iterator<String> ite = set.iterator();
////            while (ite.hasNext()) {
////                String cookie = ite.next();
////                sb.append(cookie).append(";");
////            }
////
////            int last = sb.lastIndexOf(";");
////            if (sb.length() - 1 == last) {
////                sb.deleteCharAt(last);
////            }
////
////            return sb.toString();
////        }
//
//    //保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
//    //这样能使得该cookie的应用范围更广
////        private void saveCookie(String url, String domain, String cookies) {
////            SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
////            SharedPreferences.Editor editor = sp.edit();
////
////            if (TextUtils.isEmpty(url)) {
////                throw new NullPointerException("url is null.");
////            } else {
////                editor.putString(url, cookies);
////            }
////
////            if (!TextUtils.isEmpty(domain)) {
////                editor.putString(domain, cookies);
////            }
////
////            editor.apply();
////
////        }
////    }
////
////
////    public class AddCookiesInterceptor implements Interceptor {
////        private static final String COOKIE_PREF = "cookies_prefs";
////        private Context mContext;
////
////        public AddCookiesInterceptor(Context context) {
////            mContext = context;
////        }
////
////        @Override
////        public Response intercept(Chain chain) throws IOException {
////            Request request = chain.request();
////            Request.Builder builder = request.newBuilder();
////            //String cookie = getCookie(request.url().toString(), request.url().host());
////            if (!TextUtils.isEmpty(cookie)) {
////                builder.addHeader("Cookie", cookie);
////            }
////
////            return chain.proceed(builder.build());
////        }
//
////        private String getCookie(String url, String domain) {
////            SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
////            if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
////                return sp.getString(url, "");
////            }
////            if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
////                return sp.getString(domain, "");
////            }
////
////            return null;
////        }
//    // }
//}
