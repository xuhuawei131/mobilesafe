package com.guoshisp.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	//Ӧ�ð���
	private String packname;
	//Ӧ�ð汾��
	private String version;
	//Ӧ������
	private String appname;
	//Ӧ��ͼ��
	private Drawable appicon;
	//��Ӧ���Ƿ������û�����
	private boolean userapp;
	public boolean isUserapp() {
		return userapp;
	}
	public void setUserapp(boolean userapp) {
		this.userapp = userapp;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public Drawable getAppicon() {
		return appicon;
	}
	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}
}
