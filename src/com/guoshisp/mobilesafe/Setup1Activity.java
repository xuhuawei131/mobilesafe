package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//���뵽�����򵼵ĵ�һ������
		setContentView(R.layout.setup1);
	}
	/**
	 * ����������򵼵ĵ�һ�����������½ǰ�ť-��һ��ʱ��Ҫִ�еķ�����
	 * ��Ϊ�ڸ�Button������������android:onClick=next��������Ĵ�����ʡȥ�ý���
	 * @param view
	 */
	public void next(View view){
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		//Activity�л�ʱ���Ŷ������Զ���һ��͸���ȱ仯�Ķ���Ч�����Ҹ÷�������д��startActivity(intent)����finish()����֮��Ż���Ч��
		//����һ���������ʱ�Ķ���Ч�� �� �������������ȥʱ�Ķ���Ч����
		overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
	}
}
