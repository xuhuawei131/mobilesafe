package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.service.WatchDogService1;

public class EnterPwdActivity extends Activity {
	//���������
	private EditText et_password;
	//Ӧ������
	private TextView tv_name;
	//Ӧ��ͼ��
	private ImageView iv_icon;
	//�����������Ź��������ͼ����
	private Intent serviceIntent;
	//ֹͣ����һ��Ӧ�ó��򣨽ӿڣ�
	private IService iService;
	//���ӷ���ʱ��һ�������ڰ󶨷���ʱ��Ҫ���룩
	private MyConn conn;
	//Ӧ�ð���
	private String packname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_pwd);
		et_password = (EditText) findViewById(R.id.et_password);
		//��ȡ�����ǰActivity����ͼ��WatchDogService1�е�pwdintent��
		Intent intent = getIntent();
		//��ȡ����ͼ�д�������ݣ�Ҫ���뱻������Ӧ�õİ�����
		packname = intent.getStringExtra("packname");
		tv_name = (TextView) findViewById(R.id.tv_enterpwd_name);
		iv_icon = (ImageView) findViewById(R.id.iv_enterpwd_icon);
		serviceIntent = new Intent(this,WatchDogService1.class);
		conn = new MyConn();
		//�󶨷��񣨷�startService()����ִ�з����е�onCreate-->onBind�������÷����ķ���ֵ����Ϊnull����
		bindService(serviceIntent, conn, BIND_AUTO_CREATE);
		
		try {
			//���ݰ�����ȡ������Ϣ����
			PackageInfo info = getPackageManager().getPackageInfo(packname, 0);
			//info.applicationInfo.loadLabel(getPackageManager())��ȡ���ð�����Ӧ�ó�������Ӧ��Ӧ������
			tv_name.setText(info.applicationInfo.loadLabel(getPackageManager()));
			//info.applicationInfo.loadIcon(getPackageManager())��ȡ���ð�����Ӧ�ó�������Ӧ��Ӧ��ͼ��
			iv_icon.setImageDrawable(info.applicationInfo.loadIcon(getPackageManager()));
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private class MyConn implements ServiceConnection{
		//�ڲ�����������һ������ɹ�ʱ�����á�IBinder�������onBind(Intent intent)���ص�IBinder����
		public void onServiceConnected(ComponentName name, IBinder service) {
			//��Ϊ���ص�IBinderʵ����iService�ӿڣ�����ת�ͣ�
			iService = (IService) service;
		}
		//�ڷ��������ɱ�����µ������ж�ʱ�����ã�����������Լ������ʱ�򲻻ᱻ����
		public void onServiceDisconnected(ComponentName name) {
			
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//�����
		unbindService(conn);
	}
	/**
	 * �����ȷ������ť��ִ�еķ���
	 */
	public void enterPassword(View view){
		//��ȡ��������е����룬��������ǰ��Ŀո��������
		String pwd = et_password.getText().toString().trim();
		//�ж�����������Ƿ�Ϊ��
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(this, "���벻��Ϊ��", 0).show();
			return ;
		}
		//�ж������Ƿ�Ϊ123����ȷ���룬û���ṩ��������Ľ��棬����򵥵Ĵ���һ�£���
		if("123".equals(pwd)){
			//֪ͨ���Ź� ��ʱ��ֹͣ�� packname�ı���
			iService.callTempStopProtect(packname);
			/*Intent intent = new Intent();
			intent.setAction("cn.itcast.mobilesafe.stopprotect");
			intent.putExtra("packname", packname);
			sendBroadcast(intent);*/
			finish();
			
		}else{
			Toast.makeText(this, "���벻��ȷ", 0).show();
			return ;
		}
	}
	
	/**
	 * �����뵱ǰ�Ľ�������ε�Back��
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN&&event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			return true;//���ѵ���ǰ��Back��
		}
		return super.onKeyDown(keyCode, event);
	}
}
