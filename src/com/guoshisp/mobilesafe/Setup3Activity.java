package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends Activity {
	private EditText et_setup3_number;//���ð󶨵İ�ȫ����
	private SharedPreferences sp;//���ڴ洢��ȫ���뼰��ȫ����Ļ���
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup3);
		et_setup3_number = (EditText) findViewById(R.id.et_setup3_number);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//���ݵĻ��ԡ����û�б������ȫ���룬���Ե��ǿ�
		String number = sp.getString("safemuber", "");
		et_setup3_number.setText(number);
	}
	/**
	 * �ڵ���������ġ�ѡ����ϵ�ˡ���ť�����������ԣ�android:onClick="selectContact"�����ԣ��������ѡ����ϵ�ˡ�ʱ��ִ�и÷���
	 * @param view
	 */
	public void selectContact(View view){
		Intent intent = new Intent(this,SelectContactActivity.class);
		//����һ��������ֵ��activity����������������
		startActivityForResult(intent, 0);
	}
	/**
	 * �������Activity�����صĽ�����ݴ����Intent�У������Intent�ͱ������Activity����
	 * ����ʱ��ʹ�õ���ͬһ��Intent��
	 * ע�⣺���ϣ�������ܹ��������أ�Activity������ģʽ���ܹ�����Ϊsingletaskģʽ
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			//��ȡ�����ص�����
			String number = data.getStringExtra("number");
			//�����ص�������ʾ��EditText��
			et_setup3_number.setText(number);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	//�������һ����ִ�еķ���
	public void next(View view){
		String number = et_setup3_number.getText().toString().trim();
		if(TextUtils.isEmpty(number)){
			Toast.makeText(this, "��ȫ���벻��Ϊ��", 0).show();
			return;
		}
		//��EditText�еİ�ȫ����־û�������Ҳ�������ݵĻ���
		Editor editor = sp.edit();
		editor.putString("safemuber", number);
		editor.commit();
		
		Intent intent = new Intent(this,Setup4Activity.class);
		startActivity(intent);
		finish();
		//�Զ���һ��ƽ�ƵĶ���Ч��������һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	//�������һ����ִ�еķ���
	public void pre(View view){
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		//�Զ���һ��͸���ȱ仯�Ķ���Ч��������һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч��
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}
}
