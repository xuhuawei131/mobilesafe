package com.guoshisp.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.guoshisp.mobilesafe.LostProtectedActivity;

public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//��ȡ���㲥�������Ľ������
		String outnumber = getResultData();
		//�趨���ǲ��Ž����ֻ������ĺ���
		String enterPhoneBakNumber = "110";
		//�ж��趨�ĺ����Ƿ���㲥������������ͬ
		if (enterPhoneBakNumber.equals(outnumber)) {
			//�����ֻ���������
			Intent lostIntent = new Intent(context, LostProtectedActivity.class);
			//Ϊ�ֻ�������Ӧ��Activity����һ���µ�����ջ
			lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(lostIntent);
			// ���ص��Ⲧ�ĵ绰���룬�ڲ��ż�¼�в�����ʾ�ú���
			setResultData(null);
		}
	}
}
