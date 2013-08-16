package com.guoshisp.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.guoshisp.mobilesafe.db.AppLockDBOpenHelper;

public class AppLockDao {
	private AppLockDBOpenHelper helper;

	public AppLockDao(Context context) {
		helper = new AppLockDBOpenHelper(context);
	}
	/**
	 * ����һ����������İ���
	 * return true������ҵ��ð���   false����û�в��ҵ��ð���
	 */
	public boolean find(String packname) {
		boolean result = false;
		//�����ݿ�
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			//ִ�в�ѯSQL��䣬����һ���������
			Cursor cursor = db.rawQuery(
					"select * from applock where packname =?",
					new String[] { packname });
			if (cursor.moveToFirst()) {
				result = true;
			}
			//�ر����ݿ�
			cursor.close();
			db.close();
		}
		return result;
	}
	/**
	 * ���һ�������ĳ���İ���
	 */
	public boolean add(String packname) {
		//���Ȳ�ѯһ�����ݿ����Ƿ���ڸ������ݣ���ֹ�ظ����
		if (find(packname))
			return false;
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			//ִ����ӵ�SQL���
			db.execSQL("insert into applock (packname) values (?)",
					new Object[] { packname });
			db.close();
		}
		return find(packname);
	}

	/**
	 * ɾ��һ������
	 */
	public void delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			//ִ��ɾ����SQL���
			db.execSQL("delete from applock where packname=?",
					new Object[] { packname });
			db.close();
		}
	}
	/**
	 * ����ȫ����������Ӧ�ð���
	 * 
	 * @return
	 */
	public List<String> findAll() {
		List<String> packnames = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select packname from applock",
					null);
			while (cursor.moveToNext()) {
				packnames.add(cursor.getString(0));
			}
			cursor.close();
			db.close();
		}
		return packnames;
	}
}
