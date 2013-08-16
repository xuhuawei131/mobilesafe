package com.guoshisp.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {
	private Context  context;

	public AntiVirusDao(Context context) {
		this.context = context;
	}
	
	/**
	 * ��ȡ��������Ϣ  ���û�л�ȡ�����ؿ�
	 * @param md5
	 * @return
	 */
	public String getVirusInfo(String md5){
		//Ĭ������£�û�л�ȡ��������Ϣ
		String result = null;
		//�������ݿ��·��
		String path = "/data/data/com.guoshisp.mobilesafe/files/antivirus.db";
		//�����ݿ�
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if(db.isOpen()){
			//ִ�в�ѯ����������һ�������
			Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
			if(cursor.moveToFirst()){
				result = cursor.getString(0);
			}
			//����ر�ϵͳ���α꣬���û�йرգ���ʹ�ر������ݿ⣬Ҳ���ױ����ڴ�й©���쳣��Ϣ
			cursor.close();
			db.close();
		}
		return result;
	}
}
