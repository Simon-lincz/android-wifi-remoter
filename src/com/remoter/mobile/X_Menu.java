package com.remoter.mobile;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.Toast;

public class X_Menu {
	
	private boolean DBG = true;
	private String TAG = "X_Menu";
	
	private Context mContext;
	//MenuId
	private static final int ABOUT = 1;
	private static final int CONNECT = 2;
	private static final int EXIT = 3;
	private static final int VIBATRE = 4;
	private static final int IPSETUP = 5;
	private static final int SOURCEMODE = 6;
	
	//Intent Actions
	public static final String MENU_EXIT = "EXIT"; 
	public static final String MENU_IPSETUP = "IPSETP";
	public static final String MENU_IPSETUP_IP = "targetIP_str";
	public static final String MENU_IPSETUP_PORT = "targetPort";
	public static final String MENU_VIBRATE = "vibrate_setup"; 
	
	public static final String MENU_SOURCEMODE = "sourcemode_setup"; //choose control source: touch or sensor
	public static final String TheSourceMode = "TheSourceMode";
	
	public static final int TouchMode = 1;
	public static final int SensorMode = 2;
	
	private String AMsgAboutSendBroadcast = "X_Menu send Broadcast.";
	
	public X_Menu() {
		
	}

	public X_Menu(Context context) {
		super();
		this.mContext = context;
	}

	public Menu CreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, X_Menu.ABOUT, 1,
				mContext.getResources().getString(R.string.about)).setIcon(
				R.drawable.ic_menu_home);   //���ò˵���ͼ��ͱ���
//		menu.add(0, X_Menu.CONNECT, 1,
//				mContext.getResources().getString(R.string.connect)).setIcon(
//				R.drawable.ic_menu_refresh);
		menu.add(0, X_Menu.IPSETUP, 1,
				mContext.getResources().getString(R.string.ipsetup)).setIcon(
				R.drawable.ic_input_add);
		SubMenu moresetup = menu.addSubMenu(0, 0, 1, mContext.getResources()
				.getString(R.string.more));
		moresetup.setIcon(R.drawable.ic_menu_more);// �� more ������������ͼ��
		moresetup.setHeaderIcon(R.drawable.ic_menu_manage); // �� �����Ի��� ������������ͼ��
		moresetup.add(0, X_Menu.VIBATRE, 1,
				mContext.getResources().getString(R.string.vibrate)).setIcon(
				R.drawable.ic_lock_silent_mode_vibrate);
//		moresetup.add(0, X_Menu.IPSETUP, 1,
//				mContext.getResources().getString(R.string.ipsetup)).setIcon(
//				R.drawable.ic_input_add);
		moresetup.add(0, X_Menu.SOURCEMODE, 1,
				mContext.getResources().getString(R.string.sourcemode))
				.setIcon(R.drawable.ic_menu_manage);
//		moresetup.add(0, WholeMenu.CONTROLMODE, 1, 
//				mContext.getResources().getString(R.string.controlmode))
//				.setIcon(R.drawable.ic_menu_manage);
		menu.add(0, X_Menu.EXIT, 1,
				mContext.getResources().getString(R.string.exit)).setIcon(
				R.drawable.ic_delete);
		return menu;
	}

	public MenuItem OptionsItemSelected(MenuItem item) {//����MenuId����ƥ�����Ӧ
		// TODO Auto-generated method stub
		if (item.getItemId() == X_Menu.ABOUT) {  //���ڳ���Ľ���
			Builder b = new AlertDialog.Builder(mContext);
			b.setTitle("About:")
			 .setTitle(R.string.about)
			 .setMessage(
					mContext.getResources().getString(R.string.about_content));
			AlertDialog alert = b.create();
			alert.show();
		} else if (item.getItemId() == X_Menu.CONNECT) { //����
			Builder b = new AlertDialog.Builder(mContext);
			b.setCancelable(true)
					.setIcon(R.drawable.ic_menu_refresh)
					.setTitle(
							mContext.getResources().getString(
									R.string.connect_title))
					.setPositiveButton(
							mContext.getResources().getString(
									R.string.connect_btn_connect),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Toast.makeText(
											mContext.getApplicationContext(),
											"It had connected to pc,yet.",
											Toast.LENGTH_SHORT).show();
								}
							})
					.setNegativeButton(
							mContext.getResources().getString(
									R.string.connect_btn_disconnect),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Toast.makeText(
											mContext.getApplicationContext(),
											"It has not connected to pc,yet.",
											Toast.LENGTH_SHORT).show();
								}
							});
			AlertDialog alert = b.create();
			alert.show();
		} else if (item.getItemId() == X_Menu.VIBATRE) { //������
			
			SendBroadcastToTellX_Activity(MENU_VIBRATE);
			
		} else if (item.getItemId() == X_Menu.EXIT) { //�˳�
			
			SendBroadcastToTellX_Activity(MENU_EXIT);
			
		} else if (item.getItemId() == X_Menu.IPSETUP) { //IP����
			final EditText ipsetup = new EditText(mContext);
			ipsetup.setText("");
			Builder b = new AlertDialog.Builder(mContext);
			b.setIcon(R.drawable.ic_input_add)
					.setTitle(
							mContext.getResources().getString(
									R.string.ipsetup_title))
					.setView(ipsetup)
					.setPositiveButton("OK",               //��EditText�������OK��ť
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									String addr = ipsetup.getText().toString();//��ȡ������ĵ�ַ
									
									if (addr.indexOf(":") != -1) { //�ж�����ĵ�ַ��ʽ�Ƿ���ȷ
										String targetIP_str = addr.substring(0, //��ȡIP�Ͷ˿�
												addr.indexOf(":"));
										int targetPort = Integer.valueOf(
												addr.substring(
														addr.indexOf(":") + 1,
														addr.length()));
										Intent intent = new Intent();//�ѵ�ַͨ���㲥���ͳ�ȥ
										intent.setAction(MENU_IPSETUP);
										intent.putExtra(MENU_IPSETUP_IP, targetIP_str);
										intent.putExtra(MENU_IPSETUP_PORT, targetPort);
										mContext.sendBroadcast(intent);
										Log.i(TAG,AMsgAboutSendBroadcast);
										Toast.makeText(
												mContext,
												"Now, target address is: "
														+ targetIP_str + " : "
														+ targetPort,
												Toast.LENGTH_SHORT).show();
									} else {//��ַ��ʽ����
										Toast.makeText(
												mContext,
												">>>INPUT Error<<<"
														+ "\n"
														+ "You input a wrong tpye."
														+ "\n"
														+ "Please input the address,again.",
												Toast.LENGTH_LONG).show();
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			AlertDialog alert = b.create();
			alert.show();
		} else if (item.getItemId() == X_Menu.SOURCEMODE) { //ѡ�����Դ�����ػ��ߴ�����
			AlertDialog.Builder b = new AlertDialog.Builder(mContext);
			AlertDialog a = b
					.setIcon(R.drawable.ic_menu_manage)
					.setTitle(
							mContext.getResources().getString(
									R.string.sourcemode_title))
					.setPositiveButton(  //����ģʽ
							mContext.getResources().getString(
									R.string.sourcemode_btn_touch),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									
									SendBroadcastToTellX_Activity(MENU_SOURCEMODE, TheSourceMode, TouchMode);
									
								}
							})
					.setNegativeButton(//������ģʽ
							mContext.getResources().getString(
									R.string.sourcemode_btn_sensor),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									
									SendBroadcastToTellX_Activity(MENU_SOURCEMODE, TheSourceMode, SensorMode);
									
								}
							}).create();
			a.show();
		}
		return item;
	}
	
	private void SendBroadcastToTellX_Activity(String action){//ֱ�ӷ��͹㲥
		Intent intent = new Intent();
		intent.setAction(action);
		if(DBG) Log.i(TAG,AMsgAboutSendBroadcast);
		mContext.sendBroadcast(intent);
	}
	
	private void SendBroadcastToTellX_Activity(String action,String name,int value){//���͹㲥������Я������
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra(name, value);
		if(DBG) Log.i(TAG,AMsgAboutSendBroadcast + ": " + action);
		mContext.sendBroadcast(intent);
	}
}
