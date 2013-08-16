package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.db.dao.NumberAddressDao;
/**
 * ��������ز�ѯ
 * @author Administrator
 *
 */
public class NumberQueryActivity extends Activity {
	private EditText et_number_query;// ����Ҫ��ѯ�ĺ���
	private TextView tv_number_address;// ��ʾ���������λ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.number_query);
		super.onCreate(savedInstanceState);
		et_number_query = (EditText) findViewById(R.id.et_number_query);
		tv_number_address = (TextView) findViewById(R.id.tv_number_address);
	}

	/**
	 * �������ѯ��ʱִ�еļ�������
	 * 
	 * @param view
	 */
	public void query(View view) {
		// ��ѯǰ����Ҫ������ǰ��Ŀո���յ�
		String number = et_number_query.getText().toString().trim();
		// �ж�Ҫ��ѯ�ĺ����Ƿ�Ϊ��
		if (TextUtils.isEmpty(number)) {
			Toast.makeText(this, "���벻��Ϊ��", 1).show();
			// ʹ�ö�������������һ�������뻭��Դһ��������Դ
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			// �������������û�����������������ѯ��ʱ����һ��������������ʾ�û���������ſ���ִ�в�ѯ������
			et_number_query.startAnimation(shake);
			return;
		} else {// ���벻Ϊ��ʱҪ���ع�������Ϣ
				// ���ز�ѯ���Ĺ�������Ϣ
			String address = NumberAddressDao.getAddress(number);
			// ����������Ϣ��ʾ����Ļ��
			tv_number_address.setText(address);
		}
	}
}
