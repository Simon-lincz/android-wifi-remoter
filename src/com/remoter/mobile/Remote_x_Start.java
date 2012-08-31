package com.remoter.mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class Remote_x_Start extends X_Activity implements OnClickListener {

	private static boolean DBG = true;
	private static String TAG = "Remote_x_Start";

	protected MenuCmd mCmd; 
	private static String CheckSignal = "Hi,I am here.";   //���ڻ�ӦPC�Ĺ㲥�ź�
	private Button kButton;        //�л���������ģʽ�İ�ť
	private Button jButton;         //�л����ֱ�ģʽ�İ�ť
	private ProgressBar initbar;  //��ʼ�����㲥ʱ��ʾ�Ķ�����
	boolean isStart = false;         //���ʱ���ڶ԰�������������������а����޷�Ӧ
	
	 LinearLayout linearLayout; //admob
	 AdView adView ; //admob

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_x_start);
		vibrate();                                     //�������������һ������ʾ
		new Pre_Start().execute();         //������к��һ�������������ڼ��㲥�ź�
		kButton = (Button) findViewById(R.id.keyandmouse);   //�󶨿ؼ�
		kButton.setOnClickListener(this);  //��ť�󶨼�����
		jButton = (Button) findViewById(R.id.joystick);   //�ؼ���
		jButton.setOnClickListener(this);  //��ť�󶨼�����
		initbar = (ProgressBar) findViewById(R.id.initing);  //�ؼ���
		 linearLayout = (LinearLayout)findViewById(R.id.linearlayout1);
		 //admob\
		 adView = new AdView(this, AdSize.BANNER, " a14fe4b3c976c93"); //admob
		 linearLayout.addView(adView); //admob
		 adView.loadAd(new AdRequest()); //admob
	}

	@Override
	public void onClick(View v) {  //�л�ģʽ��ť�ļ�����
		// TODO Auto-generated method stub
		if (isStart) {  //������ڼ��㲥�����ʱisStart��false,���°�ť����Ч��
			vibrate(); //���°�ť���𶯷���
			Intent intent = new Intent();  
			if (v.getId() == R.id.keyandmouse) {  //���ݰ��µİ�ť ID���ж����ĸ���ť��Ȼ��������Ӧ��Activity��
				intent.setClass(Remote_x_Start.this, KeyAndMouse.class);
			} else if (v.getId() == R.id.joystick) {
				intent.setClass(Remote_x_Start.this, JoyStick.class);
			}
			startActivity(intent); //������Ӧ��Activity
		}
	}

	@Override
	protected void onDestroy() {  //����ر�
		// TODO Auto-generated method stub
		mVibrator.cancel();  //ȡ�����𶯣����ٺĵ�
		if (isFirstOpen) { //ȫ�ֱ�����һ����־���������Ʊ�����������
			isFirstOpen = false; 
			mSocket.disconnect(); //�ر�ȫ��Socket
		}
		 if (adView != null) { //admob
		 adView.destroy(); //admob
		 } //admob
		super.onDestroy();
	}

	class Pre_Start extends AsyncTask<Object, Boolean, Object> { //������к��һ�������������ڼ��㲥�ź�

		@Override
		protected Object doInBackground(Object... arg0) {  
			// TODO Auto-generated method stub
			String tmp;
			int trytime = 2;
			while (!((tmp = mSocket.receiveData()).equals(CheckSignal))
					&& (trytime > 0))    //�ж�Socket���յ��������Ƿ���CheckSignalһ�� 
				trytime--;   //�жϴ���Ϊ2��

			if (!tmp.equals(CheckSignal)) {  
				publishProgress(false);  //������յ���������CheckSignal��һ��������onProgressUpdate�������� false
			} else {  //��֮��
				if (DBG)
					Log.i(TAG, mSocket.getTargetAddr());
				mSocket.PacketSetup(mSocket.getTargetAddr()); //���Ҵ����ݰ�����ȡ����Դ��IP�Ͷ˿ڣ����������ֻ�Socket�����ݰ�
				mSocket.sendData(CheckSignal); //Ȼ��ط�����ź�
				publishProgress(true);//��onProgressUpdate�������� true
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Boolean... values) { 
			// TODO Auto-generated method stub
			if (!values[0].booleanValue()) //�������溯���еõ���ֵΪfalse������ʾ����ʧ�ܵĶԻ���
				showDialog();
			initbar.setVisibility(View.GONE); //������ô��ȡ��������
			isStart = true; // �԰������н���
			super.onProgressUpdate(values); 
		}
	}

	private void showDialog() { //����ʧ�ܵĶԻ���
		AlertDialog.Builder b = new AlertDialog.Builder(Remote_x_Start.this);
		AlertDialog a = b
				.setMessage(
						Remote_x_Start.this.getResources().getString(
								R.string.checkconnection))
				.setPositiveButton( //���԰�ť
						Remote_x_Start.this.getResources().getString(
								R.string.ignore),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								//��������
							}
						})
				.setNegativeButton(   //�رհ�ť
						Remote_x_Start.this.getResources().getString(
								R.string.close),  
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();   //������������
							}
						}).create();
		a.show(); //��ʾ
	}
}
