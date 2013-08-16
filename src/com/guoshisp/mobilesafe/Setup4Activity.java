package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.guoshisp.mobilesafe.receiver.MyAdmin;
public class Setup4Activity extends Activity {
	private CheckBox cb_setup4_protect;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_setup4_protect = (CheckBox)findViewById(R.id.cb_setup4_protect);
		//�������ݵĻ��ԡ��ж��ֻ������Ƿ�����Ĭ�������û�п���
		boolean protecting = sp.getBoolean("protecting", false);
		cb_setup4_protect.setChecked(protecting);
		
		cb_setup4_protect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				if(isChecked){
					editor.putBoolean("protecting", true);
					cb_setup4_protect.setText("���������Ѿ�����");
				}else{
					cb_setup4_protect.setText("��������û�п���");
					editor.putBoolean("protecting", false);
				}
				editor.commit();
			}
		});
	}
	/**
	 * ������������еĵ��ĸ������еġ��������deviceadmin...��ʱ��ִ�еķ�����
	 * �����ֻ����豸����ԱȨ�ޡ�����󣬿���ִ��Զ��������������ݻָ�����������
	 * @param view
	 */
	public void activeDeviceAdmin(View view){
		//������һ����MyAdmin����������
		ComponentName mAdminName = new ComponentName(this, MyAdmin.class);
		///��ȡ�ֻ��豸������
		DevicePolicyManager dm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		//�ж�����Ƿ��Ѿ���ȡ��������Ա��Ȩ��
		if (!dm.isAdminActive(mAdminName)) {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//������ĳ�������ԱȨ�޼���
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
			startActivity(intent);
		}
	}
	
	
	
	/**
	 * ��������򵼵ĵ��ĸ������еġ�������ɡ�ʱ��ִ�еķ�������ִ�и÷���ʱ��˵���������Ѿ����
	 * @param view
	 */
	public void next(View view){
		if(!cb_setup4_protect.isChecked()){//�����������û�п���������һ���Ի�����ʾ��������
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("��ܰ��ʾ");
			builder.setMessage("�ֻ���������ı���������ֻ���ȫ,ǿ�ҽ��鿪��!");
			builder.setPositiveButton("����", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					//��������������
					cb_setup4_protect.setChecked(true);
					//�������Ѿ���ɣ����û��´ν���ʱ�ж�ʱ��ֵΪtrue��˵���Ѿ����й�������
					Editor editor = sp.edit();
					editor.putBoolean("issetup", true);
					editor.commit();
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					finish();
					//�������Ѿ���ɣ����û��´ν���ʱ�ж�ʱ��ֵΪtrue��˵���Ѿ����й�������
					Editor editor = sp.edit();
					editor.putBoolean("issetup", true);
					editor.commit();
				}
			});
			builder.create().show();
			
			
			return ;
		}
		//�������Ѿ���ɣ����û��´ν���ʱ�ж�ʱ��ֵΪtrue��˵���Ѿ����й�������
		Editor editor = sp.edit();
		editor.putBoolean("issetup", true);
		editor.commit();
		
		Intent intent = new Intent(this,LostProtectedActivity.class);
		startActivity(intent);
		finish();
		//�Զ���һ��ƽ�ƵĶ���Ч��������һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
	public void pre(View view){
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		//�Զ���һ��͸���ȱ仯�Ķ���Ч��������һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч��
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}
}
