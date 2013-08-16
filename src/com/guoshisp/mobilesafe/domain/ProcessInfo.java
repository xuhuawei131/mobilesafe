package com.guoshisp.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	//Ӧ�ó������
	private String packname; 
	//Ӧ�ó���ͼ��
	private Drawable icon;
	//Ӧ�ó�����ռ�õ��ڴ�ռ䣬��λ��byte
	private long memsize; 
	//�Ƿ������û�����
	private boolean userprocess;
	//���̵�pid�����̵ı�ǣ�
	private int pid;
	//ӦӴ����������
	private String appname;
	//Ӧ�ó�����Item���Ƿ��ڱ�ѡ��״̬��Ĭ����û�б�ѡ�У�
	private boolean checked;
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	public boolean isUserprocess() {
		return userprocess;
	}
	public void setUserprocess(boolean userprocess) {
		this.userprocess = userprocess;
	}
}
