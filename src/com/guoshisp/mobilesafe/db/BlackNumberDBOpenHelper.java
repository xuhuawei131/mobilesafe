package com.guoshisp.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
	
	public BlackNumberDBOpenHelper(Context context) {
		//����һ�������Ķ��󣬲����������ݿ����ƣ����������α깤������null��ʾʹ��ϵͳĬ�ϵģ������ģ���ǰ���ݿ�İ汾��
		super(context, "blacknumber.db", null, 1);
	}
	
	/**
	 * ���ݿ��һ�α�������ʱ��ִ�� oncreate().
	 * һ������ָ�����ݿ�ı�ṹ
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//����������ı�ṹ ��_id , ����������, ����ģʽ��0 ��ʾ�绰���� ��1��ʾ�������� ��2��ʾȫ�����أ��绰&���ţ���
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, number varchar(20), mode integer)");
	}

	/**
	 * �����ݿ�İ汾��������ʱ�� ���õķ���.
	 * һ���������������,�������ݿ�ı�ṹ.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
