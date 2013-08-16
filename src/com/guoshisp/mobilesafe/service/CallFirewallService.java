package com.guoshisp.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.guoshisp.mobilesafe.db.dao.BlackNumberDao;

public class CallFirewallService extends Service {
	public static final String TAG = "CallFirewallService";
	private TelephonyManager tm;
	private MyPhoneListener listener;
	private BlackNumberDao dao;
//	private long  starttime;
//	private long endtime;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * �������һ�α�������ʱ�� ����
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);
		// ע��ϵͳ�ĵ绰״̬�ı�ļ�����.
		listener = new MyPhoneListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// ϵͳ�ĵ绰���� �ͼ����� �绰״̬�ı仯,
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// �ֻ�����������.
				//starttime = System.currentTimeMillis();
				// �ж� incomingNumber �Ƿ��Ǻ���������
				int mode = dao.findNumberMode(incomingNumber);
				if (mode == 0 || mode == 2) {
					// ����������
					Log.i(TAG, "�Ҷϵ绰");
					//�Ҷϵ绰
					endcall(incomingNumber);
				}
				break;

			case TelephonyManager.CALL_STATE_IDLE: // �ֻ��Ŀ���״̬
				/*endtime = System.currentTimeMillis();
				if(dao.find(incomingNumber)){
					break;
				}
				if(endtime - starttime<3000){
					Log.i(TAG,"ɧ�ŵ绰");
					//showNotification(incomingNumber);
				}*/
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:// �ֻ���ͨͨ����״̬

				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * ȡ���绰״̬�ļ���.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		
	}

	/**
	 * ��ʾ��Ӻ����������notification
	 * @param incomingNumber
	 *//*
	public void showNotification(String incomingNumber) {
		//1.����һ��notification�Ĺ�����
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		//2.����һ��notification 
		int icon = R.drawable.notification;
		CharSequence tickerText = "���ص�һ��һ�������";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		//3.����notification�ľ������� �͵���¼�
		Context context = getApplicationContext();
		CharSequence contentTitle = "������һ������";
		CharSequence contentText = "����Ϊ:"+incomingNumber;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		Intent notificationIntent = new Intent(this, CallSmsSafeActivity.class);
		notificationIntent.putExtra("blacknumber", incomingNumber);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		//4.����notification��manager ��ʾһ��notification
		mNotificationManager.notify(0, notification);
		
	}*/

	/**
	 * �Ҷϵ绰
	 * ��Ҫ��������aidl�ļ�
	 * ���Ȩ��<uses-permission android:name="android.permission.CALL_PHONE" />
	 * @param incomingNumber
	 */
	public void endcall(String incomingNumber) {
		try {
			//ʹ�÷����ȡϵͳ��service����
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,
					new Object[] { TELEPHONY_SERVICE });
			//ͨ��aidlʵ�ַ����ĵ���
			ITelephony telephony = ITelephony.Stub.asInterface(binder);
			telephony.endCall();//�÷�����һ���첽�����������¿���һ���߳̽�����ĺ���������ݿ���
			
			//deleteCallLog(incomingNumber);

			// ע��һ�����ݹ۲��� �۲�uri���ݵı仯      
			getContentResolver().registerContentObserver(
					CallLog.Calls.CONTENT_URI, true, new MyObserver(new Handler(), incomingNumber));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����Լ������ݹ۲��� ,
	 * �ڹ��췽�����洫�� �۲�ĺ���
	 * @author 
	 *
	 */
	
	private class MyObserver extends ContentObserver {
		private String incomingNumber;
		public MyObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		/**
		 * ���ݿ����ݷ����ı��ʱ����õķ���
		 */
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//����ִ��ɾ������
			deleteCallLog(incomingNumber);
			//ֹͣ���ݵĹ۲�
			getContentResolver().unregisterContentObserver(this);
		}
	}

	/**
	 * ɾ�����м�¼
	 * 
	 * @param incomingNumber
	 */
	private void deleteCallLog(String incomingNumber) {
		// ���м�¼�����ṩ�߶�Ӧ��uri
		Uri uri = Uri.parse("content://call_log/calls");
		// CallLog.Calls.CONTENT_URI;
		Cursor cursor = getContentResolver().query(uri, new String[] { "_id" },
				"number=?", new String[] { incomingNumber }, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			getContentResolver().delete(uri, "_id=?", new String[] { id });
		}
		cursor.close();
	}
}
