package com.remoter.mobile;

public interface X_Socket {
	
	public void PacketSetup(String addr); //���ݰ���ַ����
	
	public void connect(); //����
	
	public void disconnect(); //�Ͽ�
	
	public void sendData(String Data); //��������
	
	public String receiveData(); //��������
	
	public String getAddr(); //��ȡ���ص�ַ
	
}
