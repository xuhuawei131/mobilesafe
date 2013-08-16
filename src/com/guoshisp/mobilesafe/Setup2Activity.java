package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Setup2Activity extends Activity implements OnClickListener {
	private RelativeLayout rl_setup2_bind;//�������sim�����ĸ��ؼ����ÿؼ��д��������ӿؼ�����ȡ�ÿؼ���Ŀ������Ϊ�����õ���¼������ڵ���ÿؼ��е��κ�һ���ؼ�����Ӧ������¼�
	private ImageView iv_setup2_bind_status;//rl_setup2_bind�е�һ���ӿؼ���������ʾsim���Ƿ񱻰�ʱ�Ĳ�ͬ״̬
	private SharedPreferences sp;//���ڱ���sim���Ƿ񱻰󶨵���Ϣ���Ա�����´μ���ʱʹ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2);
		rl_setup2_bind = (RelativeLayout) findViewById(R.id.rl_setup2_bind);
		rl_setup2_bind.setOnClickListener(this);
		iv_setup2_bind_status = (ImageView) findViewById(R.id.iv_setup2_bind_status);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//��ʼ�����߼�-�ж�sim���Ƿ񱻰�
		String simseral = sp.getString("simserial", "");
		if(TextUtils.isEmpty(simseral)){
			iv_setup2_bind_status.setImageResource(R.drawable.switch_off_normal);
		}else{
			iv_setup2_bind_status.setImageResource(R.drawable.switch_on_normal);
		}
	}
	/**
	 * �������򵼵ĵڶ��������е���������sim����ʱִ�еĵ���¼�
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_setup2_bind:
			//�жϵ�ǰsim����״̬
			String simseral = sp.getString("simserial", "");
			if(TextUtils.isEmpty(simseral)){//sim��δ��
				Editor editor = sp.edit();
				editor.putString("simserial", getSimSerial());
				editor.commit();
				//��Ϊsim����״̬��δ���󶨣����ԣ������Ŀ��Ӧ������Ϊ�󶨵�״̬
				iv_setup2_bind_status.setImageResource(R.drawable.switch_on_normal);
			}else{
				Editor editor = sp.edit();
				editor.putString("simserial", "");
				editor.commit();
				iv_setup2_bind_status.setImageResource(R.drawable.switch_off_normal);
			}
			
			break;
		}
	}
	
	/**
	 * ��ȡ�ֻ���sim������
	 */
	private String getSimSerial(){
		//sim������绰��صġ���Ҫ���嵥�ļ�������Ȩ�ޣ�<uses-permission android:name="android.permission.READ_PHONE_STATE" />
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//����sim���Ĵ���
		return tm.getSimSerialNumber();
	}
	/**
	 * �����������½ǵġ���һ������ť��Ҫִ�еķ���
	 * @param view
	 */
	public void next(View view){
		
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		//�Զ���һ��ƽ�ƵĶ���Ч��������һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	/**
	 * ����������½ǵġ���һ������Ҫִ�еķ���
	 * @param view
	 */
	public void pre(View view){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
		//�Զ���һ��͸���ȱ仯�Ķ���Ч��������һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч��
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}
}
