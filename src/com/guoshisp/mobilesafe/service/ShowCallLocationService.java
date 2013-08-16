package com.guoshisp.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoshisp.mobilesafe.R;
import com.guoshisp.mobilesafe.db.dao.NumberAddressDao;
//�ں�̨�����绰�����״̬
public class ShowCallLocationService extends Service {
	private TelephonyManager tm;//�绰������
	private MyPhoneListener listener;//�绰״̬�ı�ļ�����
	private WindowManager windowManager;//���������
	private SharedPreferences sp;//����ȡ�������ط����ʾ����Item��Ӧ��id
	//"��͸��","������","��ʿ��","ƻ����","������"
	private static final  int[] bgs = {R.drawable.call_locate_white,R.drawable.call_locate_orange,
			R.drawable.call_locate_blue,R.drawable.call_locate_green,R.drawable.call_locate_gray};
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
		sp =getSharedPreferences("config",MODE_PRIVATE);
		// ע��ϵͳ�ĵ绰״̬�ı�ļ�����.
		listener = new MyPhoneListener();
		//��ȡϵͳ�ĵ绰������
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//Ϊ�绰����һ������������һ������������������Ҫ�����ĵ绰�ı����� �������������ͨ��״̬��
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	private class MyPhoneListener extends PhoneStateListener {
		private View view;
		//����һ���ֻ���״̬      �����������н������ֻ�����
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// �ֻ�����������.
				Log.v("menu", "CALL_STATE_RINGING ");
				//��ȡ���н�������ĵ�ַ����ѯ����֮ǰ�ĺ�����������ݿ⣩
				String address = NumberAddressDao.getAddress(incomingNumber);
				//ʹ��ϵͳ����˾����ʾ��������Ϣ������ʾ��ʱ��϶�
				//Toast.makeText(getApplicationContext(), "������:"+address, 1).show();
				//ͨ�������������һ����ʾ��������صĲ���ת��View����View��һ����˾
				view = View.inflate(getApplicationContext(), R.layout.show_address, null);
				//��ȡ����ʾ��������ز��ֵĸ�����LinearLayout
				LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_show_address);
				//��sp�ļ��л�ȡ��ʾ�����ط���Item��id
				int which = sp.getInt("which", 0);
				//���������������ʾ�ı���ͼƬ
				ll.setBackgroundResource(bgs[which]);
				//����view�е�������ʾ�����ص�TextView
				TextView tv = (TextView) view.findViewById(R.id.tv_show_address);
				//����������Ϣ���õ�TextView
				tv.setText(address);
				//��ȡ���봰����صĲ��ֵĲ����������������ô�������ʾ��������ص���˾�Ĳ�����Ϣ��
	            final WindowManager.LayoutParams params = new LayoutParams();
	            //ָ����˾������Ϊͼ�ε����ϽǶ�Ӧ�ĵ�
	            params.gravity = Gravity.LEFT | Gravity.TOP;
	            //������˾�ڴ����е���ʾλ�á���ȡ����˾�봰����˵�Xֵ����ȡ����˾�봰�嶥�˵�Yֵ
	            params.x = sp.getInt("lastx", 0);
	            params.y = sp.getInt("lasty", 0);
	            //���ô��岼��View�ĸ߶�
	            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	            //���ô��岼��View�Ŀ��
	            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	            //����View�����Ի�ȡ���㡢�����Ա���������������Ļ��
	            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	            //��ʾ�ڴ����ϵ�styleΪ��͸��
	            params.format = PixelFormat.TRANSLUCENT;
	            //����View������Ϊ��˾
	            params.type = WindowManager.LayoutParams.TYPE_TOAST;
	            //����˾�����ڴ����ϡ����������һ��ȫ�ֵ�ϵͳ���񣬸÷���������ں�̨���С�һ������£��ڴ�����һ������һ��View����ʾ�󣬲������Զ���ʧ
				windowManager.addView(view, params);
				break;

			case TelephonyManager.CALL_STATE_IDLE: // �ֻ��Ŀ���״̬
				if(view!=null){
					//�������ϵ���˾�Ƴ���
					windowManager.removeView(view);
					view = null;
				}
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:// �ֻ���ͨͨ��ʱ��״̬
				/*if(view!=null){
					//�������ϵ���˾�Ƴ���
					windowManager.removeView(view);
					view = null;
				}*/
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
}
