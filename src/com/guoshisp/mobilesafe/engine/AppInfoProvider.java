package com.guoshisp.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.guoshisp.mobilesafe.domain.AppInfo;
public class AppInfoProvider {
	private PackageManager pm;
	public AppInfoProvider(Context context) {
		pm = context.getPackageManager();
	}
	/**
	 * ��ȡ���а�װ������Ϣ
	 * @return
	 */
	public List<AppInfo> getInstalledApps(){
		//�������еİ�װ�ĳ����б���Ϣ�����У�������PackageManager.GET_UNINSTALLED_PACKAGES��ʾ��������Щ��ж�صĵ���û��������ݵ�Ӧ��
		List<PackageInfo> packageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for(PackageInfo info : packageinfos){
			AppInfo appinfo = new AppInfo();
			//Ӧ�ó���İ���
			appinfo.setPackname(info.packageName);
			//Ӧ�ó���İ汾��
			appinfo.setVersion(info.versionName);
			//Ӧ�ó����ͼ�� info.applicationInfo.loadIcon(pm);
			appinfo.setAppicon(info.applicationInfo.loadIcon(pm));
			//Ӧ�ó�������� info.applicationInfo.loadLabel(pm);
			appinfo.setAppname(info.applicationInfo.loadLabel(pm).toString());
			//���˳�����������ϵͳ��Ӧ�ó��������
			appinfo.setUserapp(filterApp(info.applicationInfo));
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
	
	/**
	 * ������Ӧ�ó���Ĺ�����,
	 * @param info
	 * @return true ����Ӧ��
	 *         false ϵͳӦ��.
	 */
    public boolean filterApp(ApplicationInfo info) {
    	//��ǰӦ�ó���ı��&ϵͳӦ�ó���ı��
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}
