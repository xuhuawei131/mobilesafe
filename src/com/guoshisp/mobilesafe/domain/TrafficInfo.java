package com.guoshisp.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TrafficInfo {
	//Ӧ�õİ���
	private String packname;
	//Ӧ�õ�����
	private String appname;
	//�ϴ�������
	private long tx;
	//���ص�����
	private long rx;
	//Ӧ��ͼ��
	private Drawable icon;
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public long getTx() {
		return tx;
	}
	public void setTx(long tx) {
		this.tx = tx;
	}
	public long getRx() {
		return rx;
	}
	public void setRx(long rx) {
		this.rx = rx;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
}
