package com.xuexiang.chh_healthy_android.step.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.xuexiang.chh_healthy_android.activity.PlanActivity;
import com.xuexiang.chh_healthy_android.activity.StepActivity;
import com.xuexiang.chh_healthy_android.core.FinalEnum;
import com.xuexiang.chh_healthy_android.core.http.callback.TipCallBack;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonRequest;
import com.xuexiang.chh_healthy_android.core.http.entity.CommonResponse;
import com.xuexiang.chh_healthy_android.core.http.pojo.dto.PlanDTO;
import com.xuexiang.chh_healthy_android.core.http.pojo.query.StepQuery;
import com.xuexiang.chh_healthy_android.step.UpdateUiCallBack;
import com.xuexiang.chh_healthy_android.step.accelerometer.StepCount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xuexiang.chh_healthy_android.R;
import com.xuexiang.chh_healthy_android.step.accelerometer.StepValuePassListener;
import com.xuexiang.chh_healthy_android.step.bean.StepData;
import com.xuexiang.chh_healthy_android.utils.MMKVUtils;
import com.xuexiang.chh_healthy_android.utils.TokenUtils;
import com.xuexiang.xhttp2.XHttp;
import com.xuexiang.xhttp2.callback.CallBackProxy;
import com.xuexiang.xhttp2.exception.ApiException;
import com.xuexiang.xutil.common.logger.Logger;
import com.xuexiang.xutil.net.JsonUtil;

public class StepService extends Service implements SensorEventListener {
    private String TAG = "StepService";
    /**
     * 默认为30秒进行一次存储
     */
    private static int duration = 30 * 1000;
    /**
     * 当前的日期
     */
    private static String CURRENT_DATE = "";
    /**
     * 传感器管理对象
     */
    private SensorManager sensorManager;
    /**
     * 广播接受者
     */
    private BroadcastReceiver mBatInfoReceiver;
    /**
     * 保存记步计时器
     */
    private TimeCount time;
    /**
     * 当前所走的步数
     */
    private int CURRENT_STEP;
    /**
     * 计步传感器类型  Sensor.TYPE_STEP_COUNTER或者Sensor.TYPE_STEP_DETECTOR
     */
    private static int stepSensorType = -1;
    /**
     * 每次第一次启动记步服务时是否从系统中获取了已有的步数记录
     */
    private boolean hasRecord = false;
    /**
     * 系统中获取到的已有的步数
     */
    private int hasStepCount = 0;
    /**
     * 上一次的步数
     */
    private int previousStepCount = 0;
    /**
     * 通知管理对象
     */
    private NotificationManager mNotificationManager;
    /**
     * 加速度传感器中获取的步数
     */
    private StepCount mStepCount;
    /**
     * IBinder对象，向Activity传递数据的桥梁
     */
    private StepBinder stepBinder = new StepBinder();
    /**
     * 通知构建者
     */
    private NotificationCompat.Builder mBuilder;

    private List<StepData> list = new ArrayList<>();

    private String CHANNEL_ONE_ID;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
        initNotification();
        initTodayData();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                startStepDetector();
            }
        }).start();
        startTimeCount();

    }

    /**
     * 获取当天日期
     *
     * @return
     */
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 初始化通知栏
     */
    private void initNotification() {
        CHANNEL_ONE_ID = "chh";
        String CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.xui_config_color_red);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("今日步数" + CURRENT_STEP + " 步")
                .setChannelId(CHANNEL_ONE_ID)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(false)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.mipmap.logo);
        Notification notification = mBuilder.build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(notifyId_Step, notification);
        Log.d(TAG, "initNotification()");
    }

    /**
     * 初始化当天的步数
     */
    private void initTodayData() {
        CURRENT_DATE = getTodayDate();
        StepQuery query = new StepQuery();
        query.setToday(CURRENT_DATE);
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        CommonRequest<StepQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        Integer localStep = MMKVUtils.getInt(CURRENT_DATE + TokenUtils.getUserInfo().getId(),-1);
        CURRENT_STEP = localStep;
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<StepData>>, List<StepData>>(new TipCallBack<List<StepData>>() {
                    @Override
                    public void onSuccess(List<StepData> response) throws Throwable {
                        list = response;
                        if ( (list.size() == 0 || list.isEmpty()) && localStep == -1) {
                            CURRENT_STEP = 0;
                        } else if (list.size() == 1) {
                            Log.v(TAG, "StepData=" + list.get(0).toString());
                            if (localStep >= Integer.parseInt(list.get(0).getStep())) {
                                CURRENT_STEP = localStep;
                            } else {
                                CURRENT_STEP = Integer.parseInt(list.get(0).getStep());
                            }
                        } else {
                        }
                        if (mStepCount != null) {
                            mStepCount.setSteps(CURRENT_STEP);
                        }
                        updateNotification();
                    }
                    @Override
                    public void onError(ApiException e) {
                        //本地有记录
                        if (localStep.equals(-1)) {
                            CURRENT_STEP = 0;
                        }
                        updateNotification();
                    }
                }){});
    }

    /**
     * 注册广播
     */
    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
//        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d(TAG, "screen off");
                    //改为60秒一存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d(TAG, "screen unlock");
//                    save();
                    //改为30秒一存储
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    Log.i(TAG, " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {//日期变化步数重置为0
//                    Logger.d("重置步数" + StepDcretor.CURRENT_STEP);
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    //时间变化步数重置为0
                    isCall();
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_TICK.equals(action)) {//日期变化步数重置为0
                    isCall();
//                    Logger.d("重置步数" + StepDcretor.CURRENT_STEP);
                    save();
                    isNewDay();
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }


    /**
     * 监听晚上0点变化初始化数据
     */
    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) || !CURRENT_DATE.equals(getTodayDate())) {
            initTodayData();
        }
    }


    /**
     * 监听时间变化提醒用户锻炼
     */
    private void isCall() {
        if (TokenUtils.getUserInfo() == null) return;
        String time = MMKVUtils.getString("achieveTime", "21:00");
        String plan = MMKVUtils.getString("planWalk_QTY", "7000");
        String remind = MMKVUtils.getString("remind", "1");
        String now = new SimpleDateFormat("HH:mm").format(new Date());
        Logger.d("time=" + time + "\n" +
                "new SimpleDateFormat(\"HH: mm\").format(new Date()))=" + new SimpleDateFormat("HH:mm").format(new Date()));
        if (("1".equals(remind)) &&
                (CURRENT_STEP < Integer.parseInt(plan)) &&
                (time.equals(new SimpleDateFormat("HH:mm").format(new Date())))
                ) {
            remindNotify();
        }
        String myPlanString = MMKVUtils.getString("plan" + TokenUtils.getUserInfo().getId(),"0");
        if (!"0".equals(myPlanString)) {
            PlanDTO myPlan = JsonUtil.fromJson(myPlanString,PlanDTO.class);
            if (myPlan.getStatus() == 1 && myPlan.getPlanList() != null && myPlan.getPlanList().size() != 0) {
                for (PlanDTO p : myPlan.getPlanList()) {
                    if (p.getStatus() == 1 && p.getTime().equals(now)) {
                        remindPlanNotify(p);
                    }
                }
            }
        }
    }

    /**
     * 开始保存记步数据
     */
    private void startTimeCount() {
        if (time == null) {
            time = new TimeCount(duration, 1000);
        }
        time.start();
    }

    /**
     * 更新步数通知
     */
    private void updateNotification() {
        //设置点击跳转
        Intent hangIntent = new Intent(this, StepActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("今日步数" + CURRENT_STEP + " 步")
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setContentIntent(hangPendingIntent)
                .build();
        mNotificationManager.notify(notifyId_Step, notification);
        if (mCallback != null) {
            mCallback.updateUi(CURRENT_STEP);
        }
        Log.d(TAG, "updateNotification()");
    }

    /**
     * UI监听器对象
     */
    private UpdateUiCallBack mCallback;

    /**
     * 注册UI更新监听
     *
     * @param paramICallback
     */
    public void registerCallback(UpdateUiCallBack paramICallback) {
        this.mCallback = paramICallback;
    }

    /**
     * 记步Notification的ID
     */
    int notifyId_Step = 100;
    /**
     * 提醒锻炼的Notification的ID
     */
    int notify_remind_id = 200;

    int notify_plan_id = 300;

    /**
     * 提醒锻炼通知栏
     */
    private void remindNotify() {

        //设置点击跳转
        Intent hangIntent = new Intent(this, StepActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        String plan = MMKVUtils.getString("planWalk_QTY", "7000");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("今日步数" + CURRENT_STEP + " 步")
                .setContentText("距离目标还差" + (Integer.valueOf(plan) - CURRENT_STEP) + "步，加油！")
                .setContentIntent(hangPendingIntent)
                .setChannelId(CHANNEL_ONE_ID)
                .setTicker(getResources().getString(R.string.app_name) + "提醒您开始锻炼了")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.logo);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notify_remind_id, mBuilder.build());
    }

    private void remindPlanNotify(PlanDTO p) {

        //设置点击跳转
        Intent hangIntent = new Intent(this, PlanActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("计划小助手")
                .setContentText( p.getTime() + "," +  p.getName() + "的时间到了！")
                .setContentIntent(hangPendingIntent)
                .setChannelId(CHANNEL_ONE_ID)
                .setTicker("计划小助手提醒您预定计划时间到了！")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.ic_launcher);
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notify_plan_id++, mBuilder.build());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT
     * 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stepBinder;
    }

    /**
     * 向Activity传递数据的纽带
     */
    public class StepBinder extends Binder {

        /**
         * 获取当前service对象
         *
         * @return StepService
         */
        public StepService getService() {
            return StepService.this;
        }
    }

    /**
     * 获取当前步数
     *
     * @return
     */
    public int getStepCount() {
        return CURRENT_STEP;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 获取传感器实例
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) this
                .getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
            addBasePedometerListener();
        }
    }

    /**
     * 添加传感器监听
     * 1. TYPE_STEP_COUNTER API的解释说返回从开机被激活后统计的步数，当重启手机后该数据归零，
     * 该传感器是一个硬件传感器所以它是低功耗的。
     * 为了能持续的计步，请不要反注册事件，就算手机处于休眠状态它依然会计步。
     * 当激活的时候依然会上报步数。该sensor适合在长时间的计步需求。
     * <p>
     * 2.TYPE_STEP_DETECTOR翻译过来就是走路检测，
     * API文档也确实是这样说的，该sensor只用来监监测走步，每次返回数字1.0。
     * 如果需要长事件的计步请使用TYPE_STEP_COUNTER。
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER");
            sensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR");
            sensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v(TAG, "Count sensor not available!");
            addBasePedometerListener();
        }
    }

    /**
     * 传感器监听回调
     * 记步的关键代码
     * 1. TYPE_STEP_COUNTER API的解释说返回从开机被激活后统计的步数，当重启手机后该数据归零，
     * 该传感器是一个硬件传感器所以它是低功耗的。
     * 为了能持续的计步，请不要反注册事件，就算手机处于休眠状态它依然会计步。
     * 当激活的时候依然会上报步数。该sensor适合在长时间的计步需求。
     * <p>
     * 2.TYPE_STEP_DETECTOR翻译过来就是走路检测，
     * API文档也确实是这样说的，该sensor只用来监监测走步，每次返回数字1.0。
     * 如果需要长事件的计步请使用TYPE_STEP_COUNTER。
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            //获取当前传感器返回的临时步数
            int tempStep = (int) event.values[0];
            //首次如果没有获取手机系统中已有的步数则获取一次系统中APP还未开始记步的步数
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                //获取APP打开到现在的总步数=本次系统回调的总步数-APP打开之前已有的步数
                int thisStepCount = tempStep - hasStepCount;
                //本次有效步数=（APP打开后所记录的总步数-上一次APP打开后所记录的总步数）
                int thisStep = thisStepCount - previousStepCount;
                //总步数=现有的步数+本次有效步数
                CURRENT_STEP += (thisStep);
                //记录最后一次APP打开到现在的总步数
                previousStepCount = thisStepCount;
            }
            Logger.d("tempStep" + tempStep);
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
        updateNotification();
    }

    /**
     * 通过加速度传感器来记步
     */
    private void addBasePedometerListener() {
        mStepCount = new StepCount();
        mStepCount.setSteps(CURRENT_STEP);
        // 获得传感器的类型，这里获得的类型是加速度传感器
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        Sensor sensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = sensorManager.registerListener(mStepCount.getStepDetector(), sensor,
                SensorManager.SENSOR_DELAY_UI);
        mStepCount.initListener(new StepValuePassListener() {
            @Override
            public void stepChanged(int steps) {
                CURRENT_STEP = steps;
                updateNotification();
            }
        });
        if (isAvailable) {
            Log.v(TAG, "加速度传感器可以使用");
        } else {
            Log.v(TAG, "加速度传感器无法使用");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * 保存记步数据
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

    }

    /**
     * 保存记步数据
     */
    private void save() {
        if (TokenUtils.getUserInfo() == null) return;
        int tempStep = CURRENT_STEP;
        StepQuery query = new StepQuery();
        query.setToday(CURRENT_DATE);
        query.setCreatedBy(TokenUtils.getUserInfo().getId());
        CommonRequest<StepQuery> commonRequest = new CommonRequest<>();
        commonRequest.setBody(query);
        String body = JsonUtil.toJson(commonRequest);
        MMKVUtils.put(CURRENT_DATE + TokenUtils.getUserInfo().getId(),tempStep);
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/query")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<List<StepData>>, List<StepData>>(new TipCallBack<List<StepData>>() {
                    @Override
                    public void onSuccess(List<StepData> response) throws Throwable {
                        list = response;
                        if (list.size() == 0 || list.isEmpty()) {
                            StepData data = new StepData();
                            data.setToday(CURRENT_DATE);
                            data.setStep(tempStep + "");
                            data.setCreatedBy(TokenUtils.getUserInfo().getId());
                            saveStep(data,tempStep);
                        } else if (list.size() == 1) {
                            StepData data = list.get(0);
                            data.setStep(tempStep + "");
                            data.setVersion(data.getVersion());
                            updateStep(data);
                        } else {
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                    }
                }){});
    }

    public void saveStep(StepData data,int tempStep) {
        CommonRequest<StepData> commonRequest = new CommonRequest<>();
        commonRequest.setBody(data);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/save")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<StepData>, StepData>(new TipCallBack<StepData>() {
                    @Override
                    public void onSuccess(StepData response) throws Throwable {
                        Logger.d("保存步数成功");
                    }
                    @Override
                    public void onError(ApiException e) {
                        Logger.d("保存步数失败");
                    }
                }){});
    }

    public void updateStep(StepData data) {
        CommonRequest<StepData> commonRequest = new CommonRequest<>();
        commonRequest.setBody(data);
        String body = JsonUtil.toJson(commonRequest);
        XHttp.post(FinalEnum.frontUrl + "/healthy/step/update")
                .upJson(body)
                .syncRequest(false)
                .onMainThread(true)
                .execute(new CallBackProxy<CommonResponse<StepData>, StepData>(new TipCallBack<StepData>() {
                    @Override
                    public void onSuccess(StepData response) throws Throwable {
                        Logger.d("更新步数成功");
                    }
                    @Override
                    public void onError(ApiException e) {
                        Logger.d("更新步数失败");
                    }
                }){});
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消前台进程
        stopForeground(true);
        unregisterReceiver(mBatInfoReceiver);
        Logger.d("stepService关闭");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
