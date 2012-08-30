package com.remoter.mobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class X_Activity extends Activity {

	private static boolean DBG = true;
	private static String TAG = "X_Activity";

	private X_Menu mMenu;

	private IntentFilter filter;
	private MenuCmd mCmd;
	private boolean MenuCmdisRegister = false;

	protected static boolean isFirstOpen = false;

	protected static Vibrator mVibrator;
	protected static boolean enadleVibrator = false;

	protected static Socket_UDP mSocket;
	protected static WifiManager mWifiManager;
	protected static String targetIP_str;
	protected static int targetPort;
	protected static String locateIP_str;
	protected static int locatePort;

	protected X_Sensor mSensor;
	private int TheSourceMode = X_Menu.TouchMode;
	private String TheMsgAboutReceiveBroadcastis = "X_Activity receive Broadcast";

	public static int Screen_x, Screen_y;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر���
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��

		if (!isFirstOpen) { //ȫ�ֱ����Ŀ���
			isFirstOpen = true;
			// ---------------------vibrate---------------------- //��ȡ�𶯷���
			mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); 
			enadleVibrator = true;
			// ---------------------socket----------------------//��ʼ��socket��ֵ
			// ---------------------target----------------------
			targetIP_str = "192.168.0.104";
			targetPort = 40001;
			// ---------------------locate---------------------//��ȡ����ip������ȥ��
			mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiinfo = mWifiManager.getConnectionInfo();
			int i = wifiinfo.getIpAddress();
			locateIP_str = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
					+ ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
			locatePort = 40002;
			// ---------------------init socket--------------------
			mSocket = new Socket_UDP(targetIP_str, targetPort, locateIP_str,
					locatePort);
			mSocket.start();
			// ---------------------screen size-------------------//��ȡ�ֻ���Ļ�ߴ磬֮�󷢸�PC��
			getScreenSize(); 
		}

		mMenu = new X_Menu(X_Activity.this);
		mSensor = new X_Sensor(
				(SensorManager) getSystemService(Context.SENSOR_SERVICE),
				mSocket);

		filter = new IntentFilter(); //����anroid�㲥������
		filter.addAction(X_Menu.MENU_EXIT);
		filter.addAction(X_Menu.MENU_IPSETUP);
		filter.addAction(X_Menu.MENU_VIBRATE);
		filter.addAction(X_Menu.MENU_SOURCEMODE);
		mCmd = new MenuCmd(); //�㲥������
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (!MenuCmdisRegister) {
			MenuCmdisRegister = true;
			registerReceiver(mCmd, filter);
		}
		if (TheSourceMode == X_Menu.SensorMode) {
			mSensor.Sensors_register();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (MenuCmdisRegister) {
			MenuCmdisRegister = false;
			unregisterReceiver(mCmd);
		}
		if (TheSourceMode == X_Menu.SensorMode) { //���������SensorMode������ע��������
			mSensor.Sensors_unregister();
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (DBG)
			Log.i(TAG, "destroy.");
		super.onDestroy();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		vibrate();
		super.finish();
		if (DBG)
			Log.i(TAG, "finish.");
	}

	public static void vibrate() {
		if (enadleVibrator) //���������𶯽ӿڵĿ���
			mVibrator.vibrate(30);  //�𶯵�ʱ��
		// long[] i = {100,20};
		// if(enadleVibrator) mVibrator.vibrate(i, 10);
	}

	public void getScreenSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Screen_x = metrics.widthPixels; //���õ���ֵ�����ھ�̬������
		Screen_y = metrics.heightPixels;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(mMenu.CreateOptionsMenu(menu));//ͨ��X_menu�����˵�
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(mMenu.OptionsItemSelected(item));//ͨ��X_menu�Բ˵�������Ӧ
	}

	class MenuCmd extends BroadcastReceiver { //�㲥������
		Context mContext;

		public MenuCmd() {
		}

		public MenuCmd(Context context) {
			super();
			mContext = context;
		}

		@Override
		public void onReceive(Context context, Intent intent) {  //�Թ㲥������Ӧ������ض�����
			// TODO Auto-generated method stub
			if (DBG)
				Log.i(TAG,
						TheMsgAboutReceiveBroadcastis + ": "
								+ intent.getAction());

			if (intent.getAction().equals(X_Menu.MENU_EXIT)) {
				finish();
			} else if (intent.getAction().equals(X_Menu.MENU_VIBRATE)) { //�������߹ر���
				if (enadleVibrator)
					enadleVibrator = false;  
				else
					enadleVibrator = true;
			} else if (intent.getAction().equals(X_Menu.MENU_IPSETUP)) { //����X_menu�Ǳ߷����������ݶԵ�ַ��������
				String ip = intent.getExtras()
						.getString(X_Menu.MENU_IPSETUP_IP);
				int port = intent.getExtras().getInt(X_Menu.MENU_IPSETUP_PORT);
				mSocket.PacketSetup(ip + ":" + port);
			} else if (intent.getAction().equals(X_Menu.MENU_SOURCEMODE)) { // ѡ�񴥿���Ϊ����Դ���ߴ�������Ϊ����Դ
				if (intent.getIntExtra(X_Menu.TheSourceMode, X_Menu.TouchMode) == X_Menu.TouchMode) {
					if (DBG)
						Log.i(TAG, "touch");
					TheSourceMode = X_Menu.TouchMode;//��־��ǰģʽ
					mSensor.Sensors_unregister();
					setContentView(R.layout.keyandmouse);//���Ľ��沼��
				} else {
					if (DBG)
						Log.i(TAG, "sensor");
					TheSourceMode = X_Menu.SensorMode; //��־��ǰģʽ
					setContentView(R.layout.s_activity); //���Ľ��沼��
					sensor_btns_init();
				}
			}
		}
		private void sensor_btns_init(){  //�л������������棬���԰������г�ʼ��
			Button switcher = (Button) findViewById(R.id.switcher);  //���°���ʱ�������
			if (switcher != null) {
				switcher.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							mSensor.Sensors_register();
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							mSensor.Sensors_unregister();
						}
						return false;
					}
				});

				Button leftkey = (Button) findViewById(R.id.leftkey);
				leftkey.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mSensor.leftclick();
					}
				});

				Button midkey = (Button) findViewById(R.id.midkey);
				midkey.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mSensor.midclick();
					}
				});

				Button rightkey = (Button) findViewById(R.id.rightkey);
				rightkey.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mSensor.rightclick();
					}
				});
			}
		}
	}
}