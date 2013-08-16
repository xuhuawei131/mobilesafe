package com.guoshisp.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.guoshisp.mobilesafe.EnterPwdActivity;
import com.guoshisp.mobilesafe.IService;
import com.guoshisp.mobilesafe.db.dao.AppLockDao;

public class WatchDogService1 extends Service {
	protected static final String TAG = "WatchDogService";
	//�Ƿ�Ҫֹͣ�����Ź�����true��ʾ�������У�false��ʾֹͣ���С�
	boolean flag;
	//Ҫ����һ���ѱ�������Ӧ�ó���ǰ����Ҫ������ȷ�������ſ��Խ��롣����һ�����ڼ�����������Ľ��档
	private Intent pwdintent;
	//�������ѱ�������Ӧ�ó���İ�������ڸü��ϻ�����
	private List<String> lockPacknames;
	//�������ݿ�Ķ���
	private AppLockDao dao;
	//�����ʱ��Ҫ��������Ӧ�ó������
	private List<String> tempStopProtectPacknames;
	//���ص�EnterPwdActivity�е�ServiceConnection������onServiceConnected(ComponentName name, IBinder service)�����ĵڶ�������
	private MyBinder binder;
	//���ݹ۲���
	private MyObserver observer;
	//�����Ĺ㲥������
	private LockScreenReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		binder = new MyBinder();
		Log.v("service1", "onBind ");
		return binder;
	}
	private class MyBinder extends Binder implements IService{
		public void callTempStopProtect(String packname) {
			tempStopProtect(packname);
		}
	}
	//��ʱֹͣ����һ����������Ӧ�ó���ķ���
	public void tempStopProtect(String packname){
		//����Ҫ��ʱֹͣ�����ĳ���İ�����ӵ���Ӧ�ļ�����
		tempStopProtectPacknames.add(packname);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.v("service1", "onUnbind ");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		//����Ҫƥ���Uri·��
		Log.v("service1", "onCreate ");
		Uri uri = Uri.parse("content://com.guoshisp.applock/");
		observer = new MyObserver(new Handler());
		//�ڶ����������Ϊtrue��Uri�е�content://com.guoshisp.applock/ƥ����ȷ���ɸ�Ӧ��������ģ�ADD��DELETE�����ü�����ƥ����ȥ
		getContentResolver().registerContentObserver(uri, true, observer);
		//�Դ��붯̬ע��һ���㲥������
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new LockScreenReceiver();
		// ���ô��붯̬��ע��㲥������.
		registerReceiver(receiver, filter);
		
		super.onCreate();
		dao = new AppLockDao(this);
		//�����Ź�����ı������Ϊtrue������һֱ�ں�̨���С�
		flag = true;
		tempStopProtectPacknames = new ArrayList<String>();
		//�ӳ�������Ӧ�����ݿ���ȡ������Ӧ�ó���İ�����
		lockPacknames = dao.findAll();
		pwdintent = new Intent(this,EnterPwdActivity.class);
		//��Ϊ������û������ջ�����Ҫ����һ����Ҫ������ջ�����е�Activity�Ļ�����ҪΪ��Activity����һ������ջ��
		pwdintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//����һ���̲߳��ϵ����п��Ź�����
		new Thread() {
			public void run() {
				//����һ����ѭ�������Ϊtrue����һֱ���С�
				while (flag){
					//��ȡһ��Activity�Ĺ�������ActivityManager���Զ�̬�Ĺ۲쵽��ǰ������Щ���̡�
					ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					//��ȡ����ǰ����ջ�����е�Activity��
					RunningTaskInfo taskinfo = am.getRunningTasks(1).get(0);
					//��ȡ����ǰ����ջ����������Ӧ�İ�����
					String packname = taskinfo.topActivity.getPackageName();
					Log.i(TAG,packname);
					//�жϵ�ǰջ��Ӧ�ó����Ӧ�İ����Ƿ�����ʱ�������ĳ���
					if(tempStopProtectPacknames.contains(packname)){
						try {
							//���Ź�����ǳ��ĵ磬���������ø÷�����ͣ200����
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//��ǰջ��Ӧ�ó����Ӧ�İ�������ʱ�������ĳ�����������ǰ��if��䣬����ִ��whileѭ��
						continue;
					}
					//������ջ���ĳ���İ�����Ϣ������ͼ�У��Լ�ֵ�Ե���ʽ���룬�����ڱ������Activity��ͨ��getIntent()����ȡ����ͼ��Ȼ���ٻ�ȡ��ͼ�����е����ݣ���
					pwdintent.putExtra("packname", packname);
					//�ж�������ջ���ĳ�������Ӧ�İ����Ƿ�����������Ӧ�ó���
					if(lockPacknames.contains(packname)){
						//���ֵ�ǰӦ�ó���Ϊ��������Ӧ�ó�����Ҫ������������Ľ��档
						startActivity(pwdintent);
					}
					try {
						//���Ź�����ǳ��ĵ磬���������ø÷�����ͣ200����
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.v("service1", "onStart ");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v("service1", "onStartCommand ");
		return super.onStartCommand(intent, flags, startId);
	}
	//������ֹͣʱ������Ӧֹͣ���Ź��������У�ͬʱ�����ݹ۲��߸���ע�������ע����㲥������
	@Override
	public void onDestroy() {
		flag = false;
		Log.v("service1", "onDestroy ");
		//�����ݹ۲��߷�ע���
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
		//��ע����㲥������
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	private class MyObserver extends ContentObserver{
		public MyObserver(Handler handler) {
			super(handler);
		}
		//����Ӧ��Uri�е����ݷ����ı�ʱ���ø÷���
		@Override
		public void onChange(boolean selfChange) {
			//���´����ݿ��л�ȡ����
			lockPacknames = dao.findAll();
			super.onChange(selfChange);
		}
	}
	private class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG,"������");
			//��ռ��ϣ�����������
			tempStopProtectPacknames.clear();
		}
		
	}
}
