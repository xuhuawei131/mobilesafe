package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guoshisp.mobilesafe.service.CallFirewallService;
import com.guoshisp.mobilesafe.service.ShowCallLocationService;
import com.guoshisp.mobilesafe.service.WatchDogService1;
import com.guoshisp.mobilesafe.utils.ServiceStatusUtil;

public class SettingCenterActivity extends Activity implements OnClickListener {
	// ������Զ�����
	private SharedPreferences sp;// ���ڴ洢�Զ������Ƿ�����booleanֵ
	private TextView tv_setting_autoupdate_status;// �Զ����µ��Ƿ�����Ӧ��TextView�ؼ�����ʾ����
	private CheckBox cb_setting_autoupdate;// ��ʾ�Զ������Ƿ����Ĺ�ѡ��
	// ��������ʾ�ؼ�������
	private TextView tv_setting_show_location_status;// ��ʾ���Թ������Ƿ�����״̬
	private CheckBox cb_setting_show_location;// �Ƿ�����������ص�Checkbox
	private RelativeLayout rl_setting_show_location;// ������������Ƿ������ؼ��ĸ��ؼ�
	private Intent showLocationIntent;// ���������������Ϣ��ʾ����ͼ
	// ��������ʾ�����ؼ�������
	private RelativeLayout rl_setting_change_bg;// ����������ط�����á��ؼ��ĸ��ؼ�
	private TextView tv_setting_show_bg;// ����������ط�����á���������ʾ��ǰ�ķ������
	// ��������ʾ���λ��
	private RelativeLayout rl_setting_change_location;// ����������ʾ���λ�á���Ŀ
	// ����������ؼ�������
	private TextView tv_setting_call_firewall_status;// ��������������Ƿ�����Ӧ��TextView�ؼ�����ʾ����
	private CheckBox cb_setting_call_firewall;// ��ʾ������������ط����Ĺ�ѡ��
	private RelativeLayout rl_setting_call_firewall;// ��������������á��ؼ��ĸ��ؼ�
	private Intent callFirewallIntent;// ����������������صķ�����ͼ
	// �������ؼ�������
	private TextView tv_setting_app_lock_status;
	private CheckBox cb_setting_applock;
	private RelativeLayout rl_setting_app_lock;
	private Intent watchDogIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.setting_center);
		super.onCreate(savedInstanceState);
		// ��ȡSdcard�µ�config.xml�ļ���������ļ������ڣ���ô�����Զ��������ļ����ļ��Ĳ�������Ϊ˽������
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// ����Զ�����״̬�Ƿ�����Ӧ��Checkbox�ؼ�
		cb_setting_autoupdate = (CheckBox) findViewById(R.id.cb_setting_autoupdate);
		// ��ʾ��ǰ�Զ������Ƿ�����Ӧ��TextView�ؼ�
		tv_setting_autoupdate_status = (TextView) findViewById(R.id.tv_setting_autoupdate_status);
		// ��ʼ���Զ����µ�ui��Ĭ��״̬���ǿ�����
		boolean autoupdate = sp.getBoolean("autoupdate", true);
		if (autoupdate) {
			tv_setting_autoupdate_status.setText("�Զ������Ѿ�����");
			// ��Ϊautoupdate����Ϊtrue�����ʾ�Զ����¿��������ԣ�Checkbox��״̬Ӧ���ǹ�ѡ״̬�ģ���Ϊtrue
			cb_setting_autoupdate.setChecked(true);
		} else {
			tv_setting_autoupdate_status.setText("�Զ������Ѿ��ر�");
			// ��Ϊautoupdate����Ϊfalse�����ʾ�Զ�����δ���������ԣ�Checkbox��״̬Ӧ����δ��ѡ״̬�ģ���Ϊfalse
			cb_setting_autoupdate.setChecked(false);
		}
		/**
		 * ��Checkbox��״̬�����ı�ʱִ�����´���
		 */
		cb_setting_autoupdate
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					// ����һ����ǰ��Checkbox �ڶ�����������ǰ��Checkbox�Ƿ��ڹ�ѡ״̬
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// ��ȡ�༭��
						Editor editor = sp.edit();
						// �־û��洢��ǰCheckbox��״̬�����´ν���ʱ����Ȼ���Ա��浱ǰ���õ�״̬
						editor.putBoolean("autoupdate", isChecked);
						// �����������ύ��sp����
						editor.commit();
						if (isChecked) {// Checkbox����ѡ��Ч��
							// ��Checkbox���ڹ�ѡ״̬ʱ����ʾ���ǵ��Զ������Ѿ�������ͬʱ�޸�������ɫ
							tv_setting_autoupdate_status
									.setTextColor(Color.WHITE);
							tv_setting_autoupdate_status.setText("�Զ������Ѿ�����");
						} else {// Checkbox����δ��ѡ״̬
								// ��Checkboxδ���ڹ�ѡ״̬ʱ����ʾ���ǵ��Զ������Ѿ�������ͬʱ�޸�������ɫ
							tv_setting_autoupdate_status
									.setTextColor(Color.RED);
							tv_setting_autoupdate_status.setText("�Զ������Ѿ��ر�");
						}
					}
				});
		// ��ʾ��������Ϣ��ui��ʼ��
		tv_setting_show_location_status = (TextView) findViewById(R.id.tv_setting_show_location_status);
		cb_setting_show_location = (CheckBox) findViewById(R.id.cb_setting_show_location);
		rl_setting_show_location = (RelativeLayout) findViewById(R.id.rl_setting_show_location);
		showLocationIntent = new Intent(this, ShowCallLocationService.class);

		rl_setting_show_location.setOnClickListener(this);
		// ��������ʾ����������
		rl_setting_change_bg = (RelativeLayout) findViewById(R.id.rl_setting_change_bg);
		tv_setting_show_bg = (TextView) findViewById(R.id.tv_setting_show_bg);

		rl_setting_change_bg.setOnClickListener(this);
		// ��������ʾ���λ��
		rl_setting_change_location = (RelativeLayout) findViewById(R.id.rl_setting_change_location);
		rl_setting_change_location.setOnClickListener(this);
		// ��������Ϣ��ui��ʼ��
		tv_setting_call_firewall_status = (TextView) findViewById(R.id.tv_setting_call_firewall_status);
		cb_setting_call_firewall = (CheckBox) findViewById(R.id.cb_setting_call_firewall);
		rl_setting_call_firewall = (RelativeLayout) findViewById(R.id.rl_setting_call_firewall);
		callFirewallIntent = new Intent(this, CallFirewallService.class);

		rl_setting_call_firewall.setOnClickListener(this);
		// ����������ui�ĳ�ʼ��
		tv_setting_app_lock_status = (TextView) findViewById(R.id.tv_setting_applock_status);
		cb_setting_applock = (CheckBox) findViewById(R.id.cb_setting_applock);
		rl_setting_app_lock = (RelativeLayout) findViewById(R.id.rl_setting_applock);
		watchDogIntent = new Intent(this, WatchDogService1.class);

		rl_setting_app_lock.setOnClickListener(this);
	}
	/**
	 * ��������ʾ��ǰ̨ʱ����������Checkbox��״̬
	 */
	@Override
	protected void onResume() {
		if (ServiceStatusUtil.isServiceRunning(this,
				"com.guoshisp.mobilesafe.service.CallFirewallService")) {
			cb_setting_call_firewall.setChecked(true);
			tv_setting_call_firewall_status.setText("��������������Ѿ�����");
		} else {
			cb_setting_call_firewall.setChecked(false);
			tv_setting_call_firewall_status.setText("�������������û�п���");
		}
		if (ServiceStatusUtil.isServiceRunning(this,
				"com.guoshisp.mobilesafe.service.ShowCallLocationService")) {
			cb_setting_show_location.setChecked(true);
			tv_setting_show_location_status.setText("�����������ʾ�Ѿ�����");
		} else {
			cb_setting_show_location.setChecked(false);
			tv_setting_show_location_status.setText("�����������ʾû�п���");
		}

		if (ServiceStatusUtil.isServiceRunning(this,
				"com.guoshisp.mobilesafe.service.WatchDogService1")) {
			cb_setting_applock.setChecked(true);
			tv_setting_app_lock_status.setText("�����������Ѿ�����");
		} else {
			cb_setting_applock.setChecked(false);
			tv_setting_app_lock_status.setText("����������û�п���");
		}
		super.onResume();
	}

	// ��Ӧ����¼�
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_setting_show_location:// ����������Ƿ���
			if (cb_setting_show_location.isChecked()) {
				tv_setting_show_location_status.setText("�����������ʾû�п���");
				stopService(showLocationIntent);
				cb_setting_show_location.setChecked(false);
			} else {
				tv_setting_show_location_status.setText("�����������ʾ�Ѿ�����");
				startService(showLocationIntent);
				cb_setting_show_location.setChecked(true);
			}
			break;
		case R.id.rl_setting_change_bg:// ��������ط������
			showChooseBgDialog();
			break;
		case R.id.rl_setting_change_location:// ����һ���µĽ���,����ʵ��View���϶�
			Intent intent = new Intent(this, DragViewActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_setting_call_firewall://�������������

			if (cb_setting_call_firewall.isChecked()) {
				tv_setting_call_firewall_status.setText("�������������û�п���");
				stopService(callFirewallIntent);
				cb_setting_call_firewall.setChecked(false);
			} else {
				tv_setting_call_firewall_status.setText("��������������Ѿ�����");
				startService(callFirewallIntent);
				cb_setting_call_firewall.setChecked(true);
			}
			break;
		case R.id.rl_setting_applock://������

			if (cb_setting_applock.isChecked()) {
				tv_setting_app_lock_status.setText("����������û�п���");
				stopService(watchDogIntent);
				cb_setting_applock.setChecked(false);
			} else {
				tv_setting_app_lock_status.setText("�����������Ѿ�����");
				startService(watchDogIntent);
				cb_setting_applock.setChecked(true);
			}

			break;
		}
	}

	/**
	 * ���ı�����ɫ�ĶԻ���
	 */

	private void showChooseBgDialog() {
		// ��ȡһ���Ի�������
		AlertDialog.Builder builder = new Builder(this);
		// ���öԻ�������ͼ��
		builder.setIcon(R.drawable.notification);
		// ���öԻ���ı���
		builder.setTitle("��������ʾ����");
		// �Ի�����item�Ķ�Ӧ��ʾ����
		final String[] items = { "��͸��", "������", "��ʿ��", "ƻ����", "������" };
		// ������ʾ�Ի�������һ����Ŀ��ѡ�С�Ĭ�ϵ��ǵ�һ����Ŀ
		int which = sp.getInt("which", 0);
		// ���õ���ѡ����Ŀ��Item�У�ֻ����һ������ѡ��״̬
		builder.setSingleChoiceItems(items, which,
				new DialogInterface.OnClickListener() {
					// ����Item�ĵ���¼�
					public void onClick(DialogInterface dialog, int which) {
						// ����Ŀ��id����sp��
						Editor editor = sp.edit();
						editor.putInt("which", which);
						editor.commit();
						// ����Item��������Ϣ
						tv_setting_show_bg.setText(items[which]);
						// �رնԻ���
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

			}
		});
		// ��������ʾ���Ի���
		builder.create().show();
	}
}
