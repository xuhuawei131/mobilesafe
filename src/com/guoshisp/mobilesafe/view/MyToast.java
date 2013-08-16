package com.guoshisp.mobilesafe.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.R;

public class MyToast {

	/**
	 * ��ʾ�Զ������˾
	 * @param text ��ʾ������
	 */
	public static void showToast(Context context, String text) {
		Toast toast = new Toast(context);
		View view = View.inflate(context, R.layout.mytoast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast);
		//������ʾ����
		tv.setText(text);
		toast.setView(view);
		//����Toast��ʾ��ʱ����0��ʾ�̣�1��ʾ��
		toast.setDuration(1);
		//����Toast��ʾ�ڴ����е�λ�ã���������ʾ�ڴ��嶥�������룩
		toast.setGravity(Gravity.TOP, 0, 0);
		//��Toast��ʾ����
		toast.show();
	}
}
