package com.guoshisp.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.guoshisp.mobilesafe.domain.TrafficInfo;

public class TrafficInfoProvider {
	private PackageManager pm;
	private Context context;

	public TrafficInfoProvider(Context context) {
		this.context = context;
		pm = context.getPackageManager();
	}

	/**
	 * �������е��л���������Ȩ�޵�Ӧ�ó����������Ϣ��
	 * @return
	 */
	public List<TrafficInfo> getTrafficInfos() {
		//��ȡ������Ȩ����Ϣ��Ӧ�ó���
		List<PackageInfo> packinfos = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		//��ž���InternetȨ����Ϣ��Ӧ��
		List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();
		for(PackageInfo packinfo : packinfos){
			//��ȡ��Ӧ�õ�����Ȩ����Ϣ
			String[] permissions = packinfo.requestedPermissions;
			if(permissions!=null&&permissions.length>0){
				for(String permission : permissions){
					//ɸѡ������InternetȨ�޵�Ӧ�ó���
					if("android.permission.INTERNET".equals(permission)){
						//���ڷ�װ����InternetȨ�޵�Ӧ�ó�����Ϣ
						TrafficInfo trafficInfo = new TrafficInfo();
						//��װӦ����Ϣ
						trafficInfo.setPackname(packinfo.packageName);
						trafficInfo.setIcon(packinfo.applicationInfo.loadIcon(pm));
						trafficInfo.setAppname(packinfo.applicationInfo.loadLabel(pm).toString());
						//��ȡ��Ӧ�õ�uid��user id��
						int uid = packinfo.applicationInfo.uid;
						//TrafficStats����ͨ��Ӧ�õ�uid����ȡӦ�õ����ء��ϴ�������Ϣ
						trafficInfo.setRx(TrafficStats.getUidRxBytes(uid));
						trafficInfo.setTx(TrafficStats.getUidTxBytes(uid));
						trafficInfos.add(trafficInfo);
						trafficInfo = null;
						break;
					}
				}
			}
		}
		return trafficInfos;
	}
}
