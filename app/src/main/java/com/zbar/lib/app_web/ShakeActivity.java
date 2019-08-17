package com.zbar.lib.app_web;

import android.app.Service;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.zbar.lib.R;
import com.zbar.lib.app_root.BaseActivity;
import com.zbar.lib.constant.SinaUrlConstants;
import com.zbar.lib.custom_views.imgview.UserFaceImageView;
import com.zbar.lib.nohttp.CallServer;
import com.zbar.lib.nohttp.HttpListener;
import com.zbar.lib.util.NetworkUtil;
import com.zbar.lib.util.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class ShakeActivity extends BaseActivity implements SensorEventListener {
    private final int DURATION_TIME = 600;//动画持续时间
    private SensorManager sensorManager = null;
	private Vibrator mVibrate = null;
	private LinearLayout topLayout, bottomLayout;
    private RelativeLayout rlResult;
	private ImageView topLineIv, bottomLineIv;

    private ImageButton ibBack,ibConfig;
    private TextView tvTitle;
	private boolean isShake = false;
    private SoundPool sndPool;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    //Result Box
	private SharedPreferences sp;
    private TextView tvStName,tvStInfo;
    private UserFaceImageView imvResultPic;
    private String StID,StName,StSex,StPic,StReason,StNum;

    @Override
    public void initWidget() {
        this.setAllowFullScreen(true);//true 不显示系统的标题栏
        setContentView(R.layout.layout_activity_sa_shake);
        sp = this.getSharedPreferences("TcInfo",0);//教师登录信息存储器
        ibBack = findViewById(R.id.ib_top_back);
        tvTitle = findViewById(R.id.tv_top_title);
        tvTitle.setText("摇一摇");
        ibConfig = findViewById(R.id.ib_top_config);

        View.OnClickListener clickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ib_top_back:
                        finish();
                        break;
                    case R.id.ib_top_config:
                        tvTitle.setText("设置");
                        break;
                }
            }
        };
        ibBack.setOnClickListener(clickListener);
        ibConfig.setOnClickListener(clickListener);

        topLayout = findViewById(R.id.shake_top_layout);
        topLineIv = findViewById(R.id.shake_top_line);
        bottomLayout = findViewById(R.id.shake_bottom_layout);
        bottomLineIv = findViewById(R.id.shake_bottom_line);
        topLineIv.setVisibility(View.GONE);
        bottomLineIv.setVisibility(View.GONE);

        rlResult = findViewById(R.id.rl_shake_result_box);
        rlResult.setVisibility(View.GONE);//隐藏结果显示区域
        tvStName = findViewById(R.id.tv_shake_result_user_name);
        tvStInfo = findViewById(R.id.tv_shake_result_user_info);
        imvResultPic = findViewById(R.id.imv_shake_result_user_face);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrate = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        // 检查设备是否有震动装置
        // mVibrator.hasVibrator();
        //加载音效
        loadSound();
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public boolean widgetLongClick(View v) {
        return false;
    }

    /****
     * 获取音效
     */
    private void loadSound() {

        //设置最多可容纳 2 个音频流，音频的品质为 5
        sndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        //Error:FileNotFoundException
        /*
        try {
            soundID.put(0, sndPool.load(getAssets().openFd("sound/shake_sound_male.mp3"), 1));
            soundID.put(1, sndPool.load(getAssets().openFd("sound/shake_match.mp3"), 1));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("loadSound2", e.toString());
        }
        */
        //Raw
        soundID.put(0, sndPool.load(this, R.raw.shake_sound_male, 1));
        soundID.put(1, sndPool.load(this, R.raw.shake_match, 1));
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopListener();
	}

    @Override
    protected void onResume() {
        super.onResume();
        startListener();
    }

    //记得在不用的时候关掉传感器，因为手机黑屏是不会自动关掉传感器的，当然如果你觉得电量一直都很足，那算我多嘴咯。
    @Override
    protected void onStop() {
        super.onStop();
        stopListener();// 取消监听
    }
    // 监听到手机摇动
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            //设备坐标系是固定于设备的，与设备的方向（在世界坐标系中的朝向）无关
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            //Log.i("sensor", "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z);
            //Log.i("sensor各方向上的重力加速度", "x:" + x +  "；y:" + y +  "；z:" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 16;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue && !isShake) {
				isShake = true;
				new Thread() {
					public void run() {
						try {
							runOnUiThread(new Runnable() {
								public void run() {
									// 摇动手机后，再伴随震动提示~~
									mVibrate.vibrate(300);
                                    rlResult.setVisibility(View.GONE);//隐藏结果区域
									topLineIv.setVisibility(View.VISIBLE);
									bottomLineIv.setVisibility(View.VISIBLE);
									startAnimation(false);
								}
							});
							Thread.sleep(500);
							runOnUiThread(new Runnable() {
								public void run() {
									// 摇动手机后，再伴随震动提示~~
									mVibrate.vibrate(300);
                                    //播放音效
                                    playSound(soundID.get(0));
								}
							});
							Thread.sleep(500);
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									isShake = false;
									startAnimation(true);
								}
							});
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
	}

    //当传感器精度的改变时  进行的操作
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
    //定义震动
    public void startVibrato(){
        mVibrate.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
    }

    private void startAnimation(boolean isBack) {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		float topFromYValue;
		float topToYValue;
		float bottomFromYValue;
		float bottomToYValue;
		if (isBack) {
			topFromYValue = -0.5f;
			topToYValue = 0;
			bottomFromYValue = 0.5f;
			bottomToYValue = 0;
		} else {
			topFromYValue = 0;
			topToYValue = -0.5f;
			bottomFromYValue = 0;
			bottomToYValue = 0.5f;
		}
		TranslateAnimation topAnimation = new TranslateAnimation(type, 0, type,
				0, type, topFromYValue, type, topToYValue);
		topAnimation.setDuration(DURATION_TIME);
		topAnimation.setFillAfter(true);
		TranslateAnimation bottomAnimation = new TranslateAnimation(type, 0,
				type, 0, type, bottomFromYValue, type, bottomToYValue);
		bottomAnimation.setDuration(DURATION_TIME);
		bottomAnimation.setFillAfter(true);
		if (isBack) {
			bottomAnimation.setAnimationListener(new TranslateAnimation.AnimationListener() {
                        // 动画监听，开始时显示加载状态，
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
                            stopListener();
							//播放音效
							//playSound(soundID.get(0));
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
                            startListener();
							topLineIv.setVisibility(View.GONE);
							bottomLineIv.setVisibility(View.GONE);
                            //播放音效
                            playSound(soundID.get(1));
                            //Toast.makeText(ShakeActivity.this, "检测到摇晃，执行操作！", Toast.LENGTH_SHORT).show();
                            //Log.i("sensor", "检测到摇晃，执行操作！");
                            //从服务器获取学生信息并显示出来
                            if(NetworkUtil.isOnLine(getApplicationContext())) {
								ShowResult();
							}else{
								showMessage("手机没网络，请检查WIFI或数据网络环境！");
                                finish();//返回上一层界面
							}
						}
					});
		}
		bottomLayout.startAnimation(bottomAnimation);
		topLayout.startAnimation(topAnimation);
	}
	/**
	 * 随机返回学生信息，并显示
	 */
	private void ShowResult() {
        String tid = sp.getString("TcID","0");
        //ToastUtil.showToast(getApplicationContext(),"tid=" + tid);
		String TID = StringUtil.Base64Encode(tid);
        //ToastUtil.showToast(getApplicationContext(),"TID=" + TID);
		Request<JSONObject> request = NoHttp.createJsonObjectRequest(SinaUrlConstants.SAE_ShakeUrl, RequestMethod.POST);
		request.add("TID", TID);
		CallServer.getRequestInstance().add(this, 0, request, new HttpListener<JSONObject>() {
			@Override
			public void onSucceed(int what, Response response) {
				//showMessage(response.get().toString());
				JSONObject jsonObject = (JSONObject)response.get();
				//Log.i("ShakeResultJSON：",response.toString());
				try {
					if (jsonObject.has("Data")) {
						JSONObject StData = jsonObject.getJSONObject("Data");
						StID = StData.getString("SID");
						StName = StData.getString("SN");
						StSex = StData.getString("Sex");
						StReason = StData.getString("Reason");
						StNum = StData.getString("Num");
						StPic = "http://lzedu-sa.stor.sinaapp.com/Images/Students_Face/" + StID + ".jpg";
						tvStName.setText(StName);
						tvStInfo.setText(StSex + "|" + StReason + "|" + StNum);
						//imvResultPic.setImageUrl(StPic ,imageLoader);
						GetImg(StPic);//加载头像
						rlResult.setVisibility(View.VISIBLE);
					}else{
						String err = jsonObject.getString("err");
						String msg = jsonObject.getString("msg");
						showMessage(err + ":" + msg + "\n获取学生信息失败，请重新摇一摇！");
					}
				} catch (Exception e) {
					showMessage("服务器返回值有误，摇一摇失败："+e.toString());
				}
			}

			@Override
			public void onFailed(int what, Response response) {
				showMessage("获取学生信息失败！\n请扫码登录教室后再试！");
			}
		}, true, true);
	}

	/**
	 * 获取并显示 头像
	 */
	private void GetImg(String imgUrl) {
		Request<Bitmap> request = NoHttp.createImageRequest(imgUrl);
		CallServer.getRequestInstance().add(this, 1, request, new HttpListener<Bitmap>() {
			@Override
			public void onSucceed(int what, Response<Bitmap> response) {
				imvResultPic.setImageBitmap(response.get());
			}

			@Override
			public void onFailed(int what, Response<Bitmap> response) {
				imvResultPic.setImageResource(R.drawable.default_userhead);
			}
		}, true, false);
	}

    // 注册监听器
    public void startListener() {
        if (sensorManager != null) {
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    // 取消监听器
    public void stopListener() {
        //结束监听
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
    //播放音效
    public void playSound(int sid) {
        //=======================================================================
        //sndPool.play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        /* 参数依次是：
        soundID：Load()返回的声音ID号
        leftVolume：左声道音量设置
        rightVolume：右声道音量设置
        priority：指定播放声音的优先级，数值越高，优先级越大。
        loop：指定是否循环：-1表示无限循环，0表示不循环，其他值表示要重复播放的次数
        rate：指定播放速率：1.0的播放率可以使声音按照其原始频率，而2.0的播放速率，可以使声音按照其
        原始频率的两倍播放。如果为0.5的播放率，则播放速率是原始频率的一半。播放速率的取值范围是0.5至2.0。
        */
        //Toast.makeText(ShakeActivity.this, "sid:" + sid + "???", Toast.LENGTH_LONG).show();

        sndPool.play(sid, 1, 1, 0, 0, 1);
    }
    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        stopListener();
        //回收SoundPool资源
        sndPool.release();
    }
}
