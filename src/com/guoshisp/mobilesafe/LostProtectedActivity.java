package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.utils.Md5Encoder;

public class LostProtectedActivity extends Activity implements OnClickListener {
	private static final String TAG = "LostProtectedActivity";
	//ƫ�����ô洢����
	private SharedPreferences sp;
	//��һ�ν��롱�ֻ�����������ʱ�Ľ���ؼ�����
	private EditText et_first_dialog_pwd;
	private EditText et_first_dialog_pwd_confirm;
	private Button bt_first_dialog_ok;
	private Button bt_first_dialog_cancle;
	//�ڶ��ν��롱�ֻ�����������ʱ�Ľ���ؼ�����
	private EditText et_normal_dialog_pwd;
	private Button bt_normal_dialog_ok;
	private Button bt_normal_dialog_cancle;
	//�����򵼽�����Ľ�������еĿؼ�
	private TextView tv_lost_protect_number;//�󶨵İ�ȫ����
	private RelativeLayout rl_lost_protect_setting;//�������������Ƿ������ڵĸ��ؼ�����ȡ�ÿؼ���ҪΪ�ÿؼ����õ���¼�������ÿؼ��е�����һ���ؼ�������Ӧ����¼���
	private CheckBox cb_lost_protect_setting;//���������Ƿ���
	private TextView tv_lost_protect_reentry_setup;//�ÿؼ��ĵ���¼�ִ�У����½��������򵼽���
	//�Ի������
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//��ȡSdcard�µ�config.xml�ļ���������ļ������ڣ���ô�����Զ��������ļ�
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �ж��û��Ƿ����ù�����
		if (isSetupPwd()) {
			//����ǵ�һ�ν��롰�ֻ�������ʱҪ��ʾ�ĶԻ���
			showNormalEntryDialog();
		} else {
			//�����һ�ν��롰�ֻ�������ʱҪ��ʾ�ĶԻ���
			showFirstEntryDialog();
		}
	}

	/**
	 * ��һ�ν��롰�ֻ�������ʱҪ��ʾ�ĶԻ���
	 */
	private void showFirstEntryDialog() {
		//�õ��Ի���Ĺ�����
		AlertDialog.Builder builder = new Builder(this);
		//ͨ��View�����inflate(Context context, int resource, ViewGroup root)���󽫵�һ�ν��롰�ֻ�������Ҫ�����Ĵ���Ի���Ĳ����ļ�ת��Ϊһ��View����
		View view = View.inflate(this, R.layout.first_entry_dialog, null);
		//����view�����еĸ����ؼ�
		et_first_dialog_pwd = (EditText) view
				.findViewById(R.id.et_first_dialog_pwd);
		et_first_dialog_pwd_confirm = (EditText) view
				.findViewById(R.id.et_first_dialog_pwd_confirm);
		bt_first_dialog_ok = (Button) view
				.findViewById(R.id.bt_first_dialog_ok);
		bt_first_dialog_cancle = (Button) view
				.findViewById(R.id.bt_first_dialog_cancle);
		//�ֱ�Ϊ��ȡ��������ȷ������ť����һ��������
		bt_first_dialog_cancle.setOnClickListener(this);
		bt_first_dialog_ok.setOnClickListener(this);
		//�������View������ӵ��Ի�����
		builder.setView(view);
		//��ȡ���Ի������
		dialog = builder.create();
		//��ʾ���Ի���
		dialog.show();
	}

	/**
	 * �����ù�������������롰�ֻ�������ʱҪ��ʾ�ĶԻ���
	 */
	private void showNormalEntryDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setOnCancelListener(new OnCancelListener() {
			//�������ȡ������ťʱ��ֱ�ӽ�������ǰ��LostProtectedActivity���������뵽������
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		//ͨ��View�����inflate(Context context, int resource, ViewGroup root)���󽫷ǵ�һ�ν��롰�ֻ�������Ҫ�����Ĵ���Ի���Ĳ����ļ�ת��Ϊһ��View����
		View view = View.inflate(this, R.layout.normal_entry_dialog, null);
		//����view�����еĸ����ؼ�
		et_normal_dialog_pwd = (EditText) view
				.findViewById(R.id.et_normal_dialog_pwd);
		bt_normal_dialog_ok = (Button) view
				.findViewById(R.id.bt_normal_dialog_ok);
		bt_normal_dialog_cancle = (Button) view
				.findViewById(R.id.bt_normal_dialog_cancle);
		//�ֱ�Ϊ��ȡ��������ȷ������ť����һ��������
		bt_normal_dialog_cancle.setOnClickListener(this);
		bt_normal_dialog_ok.setOnClickListener(this);
		//�������View������ӵ��Ի�����
		builder.setView(view);
		//��ȡ���Ի������
		dialog = builder.create();
		//��ʾ���Ի���
		dialog.show();
	}

	/**
	 * �ж��û��Ƿ����ù�����
	 * 
	 * @return
	 */
	private boolean isSetupPwd() {
		String savedpwd = sp.getString("password", "");
		if (TextUtils.isEmpty(savedpwd)) {//ͨ��һ���ı����������ж�String�Ƿ�Ϊ��
			return false;
		} else {
			return true;
		}
		// return (!TextUtils.isEmpty(savedpwd));
	}
	/**
	 * Ϊ�����Ի����еġ�ȷ�����͡�ȡ������ť���õļ�����
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		//��һ�ν��롰�ֻ�������ʱ�����ĶԻ����У��ԡ�ȡ������ť�¼��Ĵ���
		case R.id.bt_first_dialog_cancle:
			dialog.cancel();//ȡ���Ի���
			finish();//������ǰ��Activity����������������
			break;
		//��һ�ν��롰�ֻ�������ʱ�����ĶԻ����У��ԡ�ȷ������ť�¼��Ĵ���	
		case R.id.bt_first_dialog_ok:
			//��ȡ������EditText�е���������룬����EditTextǰ��Ŀո��ȥ����
			String pwd = et_first_dialog_pwd.getText().toString().trim();
			String pwd_confirm = et_first_dialog_pwd_confirm.getText()
					.toString().trim();
			//�ж�����EditText�е������Ƿ�Ϊ��
			if (TextUtils.isEmpty(pwd_confirm) || TextUtils.isEmpty(pwd)) {
				Toast.makeText(this, "���벻��Ϊ��", 1).show();
				return;
			}
			//�ж�����EditText�е������Ƿ���ͬ
			if (pwd.equals(pwd_confirm)) {
				//��ȡ��һ���༭�����󣬴˴�������sp�б༭����
				Editor editor = sp.edit();
				//�����ܺ��������뵽sp����Ӧ���ļ���
				editor.putString("password", Md5Encoder.encode(pwd));
				//���༭�������ύ����������Ĵ����sp��
				editor.commit();
				//���ٵ�ǰ�ĶԻ���
				dialog.dismiss();
				//��������ǰ��Activity����ת��������
				finish();
			} else {
				Toast.makeText(this, "�������벻��ͬ", 1).show();
				return;
			}

			break;
			//�ǵ�һ�ν��롰�ֻ�������ʱ�����ĶԻ����У��ԡ�ȡ������ť�¼��Ĵ���
		case R.id.bt_normal_dialog_cancle:
			dialog.cancel();
			finish();
			break;
			//�ǵ�һ�ν��롰�ֻ�������ʱ�����ĶԻ����У��ԡ�ȷ������ť�¼��Ĵ���	
		case R.id.bt_normal_dialog_ok:
			String userentrypwd = et_normal_dialog_pwd.getText().toString()
					.trim();
			if (TextUtils.isEmpty(userentrypwd)) {
				Toast.makeText(this, "���벻��Ϊ��", 1).show();
				return;
			}
			String savedpwd = sp.getString("password", "");
			//��Ϊ��������������󣬴�����Ǽ��ܺ�����룬���Ե����ǽ���������������õ�����Ƚ�ʱ��Ҫ������������ȼ���
			if (savedpwd.equals(Md5Encoder.encode(userentrypwd))) {
				Toast.makeText(this, "������ȷ�������", 1).show();
				dialog.dismiss();
				// �ж��û��Ƿ���й�������.
				if(isSetupAlready()){
					//���뵽��������򵼺�Ľ���
					Log.i(TAG,"���뵽��������򵼺�Ľ���");
					setContentView(R.layout.lost_protected);
					//�󶨵İ�ȫ����
					tv_lost_protect_number = (TextView) findViewById(R.id.tv_lost_protect_number);
					String safemuber = sp.getString("safemuber", "");
					tv_lost_protect_number.setText(safemuber);
					//�������������Ƿ������ڵĸ��ؼ�����ȡ�ÿؼ���ҪΪ�ÿؼ����õ���¼�������ÿؼ��е�����һ���ؼ�������Ӧ����¼���
					rl_lost_protect_setting = (RelativeLayout)findViewById(R.id.rl_lost_protect_setting);
					//���������Ƿ���
					cb_lost_protect_setting = (CheckBox)findViewById(R.id.cb_lost_protect_setting);
					boolean protecting = sp.getBoolean("protecting", false);
					cb_lost_protect_setting.setChecked(protecting);
					if(protecting){
						cb_lost_protect_setting.setText("���������Ѿ�����");
					}else{
						cb_lost_protect_setting.setText("��������û�п���");
					}
					//�ÿؼ��ĵ���¼�ִ�У����½��������򵼽���
					tv_lost_protect_reentry_setup = (TextView)findViewById(R.id.tv_lost_protect_reentry_setup);
					
					rl_lost_protect_setting.setOnClickListener(this);
					tv_lost_protect_reentry_setup.setOnClickListener(this);
					
				}else{
					//���������򵼽���
					Log.i(TAG,"���뵽�����򵼽���");
					Intent intent = new Intent(this,Setup1Activity.class);
					//ִ�и÷�����ԭ�����ڣ����û���������򵼺�back��ʱ���������֮ǰ�Ľ��棬��ǿ�û�����Ч��
					finish();
					startActivity(intent);
				}
				return;
			} else {
				Toast.makeText(this, "���벻��ȷ", 1).show();
				return;
			}
		case R.id.tv_lost_protect_reentry_setup://���½���������
			Intent reentryIntent = new Intent(this,Setup1Activity.class);
			startActivity(reentryIntent);
			finish();
			break;
		case R.id.rl_lost_protect_setting://�Ƿ�����������
			Editor editor =	sp.edit();
			if(cb_lost_protect_setting.isChecked()){
				cb_lost_protect_setting.setChecked(false);
				cb_lost_protect_setting.setText("��������û�п���");
				editor.putBoolean("protecting", false);
				
			}else{
				cb_lost_protect_setting.setChecked(true);
				cb_lost_protect_setting.setText("���������Ѿ�����");
				editor.putBoolean("protecting", true);
			}
			editor.commit();
			break;
		}
	}
	/**
	 * �ж��û��Ƿ���ɹ�������
	 * 
	 * @return
	 */
	private boolean isSetupAlready() {
		//Ĭ������·���false����ʾ�û�û�н��й�������
		return sp.getBoolean("issetup", false);
	}
	/**
	 * ������Menu��ʱ���һ���˵������ʵ���һ�α���ʱ����ܻص��÷���
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//����һ����ǰItem���ڵ��飬����������ǰItem��id�ţ���Switch��Ҫ�õ�
		//�������������ֶ��Itemʱ�������ֿ��Ծ���Item�ڲ˵��е�ǰ��λ�ã������ģ���ǰItem�ڲ˵��еı���
		menu.add(1, 1, 1, "���ı�������");
		return true;
	}
	/**
	 * ��һ���˵��е�Item��ѡ��ʱ����ܻص��÷�����������������Item����
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//���涨���idΪ1
		if (item.getItemId() == 1) {
			//��ȡһ�����幹����
			AlertDialog.Builder builder = new Builder(this);
			//����һ���ı������
			final EditText et = new EditText(this);
			//�����ı����������ʾ�����ݣ�������ı������ʱ�������ݻ��Զ���ʧ
			et.setHint("�������µı�������,��������");
			//���ı��������ӵ�����Ի�����
			builder.setView(et);
			//Ϊ����Ի������һ����ȷ������ť
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						//�������ȷ������ťʱҪִ�еĻص�����
						public void onClick(DialogInterface dialog, int which) {
							//��ȡ�ı�������е����ݣ������ı�ǰ��Ŀո�ȥ����
							String newname = et.getText().toString().trim();
							//��ȡsp��Ӧ�ı༭��
							Editor editor = sp.edit();
							//���޸ĺ�����Ʊ��浽sp�У���ʱ���ݻ�ֻ�ڻ�����
							editor.putString("newname", newname);
							//���������ı����浽sp��Ӧ���ļ���
							editor.commit();
						}
					});
			//��������ʾ������Ի���
			builder.create().show();
		}
		 return super.onOptionsItemSelected(item);
	}
}
