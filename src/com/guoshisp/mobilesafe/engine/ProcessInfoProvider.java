package com.guoshisp.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.guoshisp.mobilesafe.R;
import com.guoshisp.mobilesafe.domain.ProcessInfo;

public class ProcessInfoProvider {
	private Context context;

	public ProcessInfoProvider(Context context) {
		this.context = context;
	}

	/**
	 * �������е��������еĳ�����Ϣ
	 * @return
	 */
	public List<ProcessInfo> getProcessInfos() {
		//am���Զ�̬�Ļ�ȡӦ�õĽ�����Ϣ���൱��PC���ϵĽ��̹�����
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		//pm���Ծ�̬�Ļ�ȡ���ֻ��е�����Ӧ�ó�����Ϣ���൱��PC���ϵĳ��������
		PackageManager pm = context.getPackageManager();
		//���������������еĽ���
		List<RunningAppProcessInfo> runingappsInfos = am
				.getRunningAppProcesses();
		//���ڴ�Ž�����Ϣ
		List<ProcessInfo> processInfos = new ArrayList<ProcessInfo>();
		//������ÿ�����̣�����ÿ�����̵���Ϣ��װ��ProcessInfo�����У�������еĽ��̴����List<ProcessInfo>�з���
		for (RunningAppProcessInfo info : runingappsInfos) {
			//���ڷ�װ������Ϣ
			ProcessInfo processInfo = new ProcessInfo();
			//��ȡ���̵�pid�����̵ı�ǣ�
			int pid = info.pid;
			//�����̵�pid��processName��memsize��װ��ProcessInfo������
			processInfo.setPid(pid);
			String packname = info.processName;
			processInfo.setPackname(packname);
			//��ȡ���ý��̶�Ӧ��Ӧ�ó�����ռ�õ��ڴ�ռ�
			long memsize = am.getProcessMemoryInfo(new int[] { pid })[0]
					.getTotalPrivateDirty() * 1024;
			processInfo.setMemsize(memsize);

			try {
				//ͨ�����̵�packname����ȡ���ý��̶�Ӧ��Ӧ�ó�����󣨻�ȡ��Ӧ�ó���Ķ���󣬾Ϳ���ͨ���ö����ȡӦ�ó�����Ϣ��
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
				//�жϸ�Ӧ�ó����Ƿ��ǵ�����Ӧ�ó��򣬱����Ժ����
				if(filterApp(applicationInfo)){
					processInfo.setUserprocess(true);
				}else{
					processInfo.setUserprocess(false);
				}
				//�ֱ��ȡ��Ӧ�ó����ͼ������ƣ��������װ��ProcessInfo������
				processInfo.setIcon(applicationInfo.loadIcon(pm));
				processInfo.setAppname(applicationInfo.loadLabel(pm).toString());
			} catch (Exception e) {
				//������׳�һ������δ�ҵ��쳣�����ǽ�������Ϊϵͳ���̣�Ӧ��ͼ��ΪĬ�ϵ�ϵͳͼ��
				e.printStackTrace();
				processInfo.setUserprocess(false);
				processInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				processInfo.setAppname(packname);
			}
			processInfos.add(processInfo);
			processInfo = null;
			
		}
		return  processInfos;
	}
	
	/**
	 * ����Ӧ�õĹ����� ,��
	 * 
	 * @param info
	 * @return true ����Ӧ�� false ϵͳӦ��
	 */
	public boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
