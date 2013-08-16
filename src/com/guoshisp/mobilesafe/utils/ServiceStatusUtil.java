package com.guoshisp.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtil {

	/**
	 * �ж�ĳ�������Ƿ�������״̬
	 * @param context
	 * @param serviceClassName ���������������
	 * @return true��ʾ��������   false��ʾû������
	 */
	public static boolean isServiceRunning(Context context,String serviceClassName){
		ActivityManager  am  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//����100����ʾҪ��ȡ�������е�100���������û��100���򷵻������������еģ��������100����ֻ����100����
		List<RunningServiceInfo>  infos = am.getRunningServices(100);
		//�������صķ����ж����ǲ鿴�ķ����Ƿ�������״̬
		for(RunningServiceInfo info: infos){
			if(serviceClassName.equals(info.service.getClassName())){
				return true;
			}
		}
		return false;
	}
}
