package com.guoshisp.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumDao {

	/**
	 * �������ݿ��ж��ٸ�����
	 * 
	 * @return
	 */
	public static int getGroupCount() {
		int count = 0;
		// ��Ҫ�򿪵����ݿ����ֻ�ϵͳ�е�λ��
		String path = "/data/data/com.guoshisp.mobilesafe/files/commonnum.db";
		// �����ݿ�
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			// ��classlist���в�ѯ���ж����鹫�ú���
			Cursor cursor = db.rawQuery("select * from classlist", null);
			count = cursor.getCount();
			// ʹ�������ݿ����Ҫ�ر�
			cursor.close();
			db.close();
		}
		return count;
	}

	/**
	 * ��ȡ���еķ��鼯����Ϣ��Ҳ��ÿ����������֣�
	 * 
	 * @return
	 */
	public static List<String> getGroupNames() {
		// ���ڴ�Ÿ��������������Ϣ
		List<String> groupNames = new ArrayList<String>();
		String path = "/data/data/com.guoshisp.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			// ��ȡ������������ֵĽ�������α꣩
			Cursor cursor = db.rawQuery("select name from classlist", null);
			// ������ÿ�������Ӧ������
			while (cursor.moveToNext()) {
				String groupName = cursor.getString(0);
				groupNames.add(groupName);
				groupName = null;
			}
			cursor.close();
			db.close();
		}
		return groupNames;
	}

	/**
	 * ͨ������ķ����Ӧ��id����ȡĳ�÷�������
	 * 
	 * @param groupPosition
	 * @return
	 */
	public static String getGroupNameByPosition(int groupPosition) {
		String name = null;
		String path = "/data/data/com.guoshisp.mobilesafe/files/commonnum.db";
		// ��Ϊclasslist���е�name��id�Ǵ�1��ʼ�ģ���ExpandableListView�е�id�Ǵ�0��ʼ��
		int newposition = groupPosition + 1;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			// ͨ��id�Ĳ�ѯ������ȡ��id���Ӧ��name
			Cursor cursor = db.rawQuery(
					"select name from classlist where idx=?",
					new String[] { newposition + "" });
			if (cursor.moveToFirst()) {// cursorָ���Ĭ��λ�����ڵ�һ����������ģ����ԣ����ȡ���ݵĻ���ָ����������ƶ�
				// ��Ϊһ��idֻ�Ƕ�Ӧһ��name������ֻ��Ҫ��ȡ����һ������
				name = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}
		return name;
	}

	/**
	 * ��ȡ��Ӧ�ķ��������ж��ٸ��Ӻ��ӣ�Ҳ��ÿ�����������ж��������룩
	 * ��ÿ�����Ӷ����Զ�Ӧһ�ű������ڲ�ѯ�Ӻ��ӵ���Ϣ��ʱ����Ҫȷ���ǲ�ѯ���ź�������Ӧ�ı�table+position
	 * @param groupPosition
	 * @return
	 */
	public static int getChildrenCount(int groupPosition) {
		int count = 0;
		String path = "/data/data/com.guoshisp.mobilesafe/files/commonnum.db";
		// �����ݿ�
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// ��ΪgroupPosition����ʼֵ�Ǵӿ�ʼ�ģ���table���е�_id�Ǵ�1��ʼ��
		int newposition = groupPosition + 1;
		String sql = "select * from table" + newposition;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, null);
			// ��ȡ����ѯ���������������Ҳ�൱���ж��������룩
			count = cursor.getCount();
			cursor.close();
			db.close();
		}
		return count;
	}

	/**
	 * ��ȡ��Ӧλ�õ��Ӻ��ӵ���Ϣ��
	 * ��ÿ�����Ӷ����Զ�Ӧһ�ű������ڲ�ѯ�Ӻ��ӵ���Ϣ��ʱ����Ҫȷ���ǲ�ѯ���ź�������Ӧ�ı�table+position
	 */
	public static String getChildNameByPosition(int groupPosition,
			int childPosition) {
		String result = null;
		String path = "/data/data/com.guoshisp.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		int newGroupPosition = groupPosition + 1;
		int newChildPosition = childPosition + 1;
		// ��ѯ�Ӻ��ӵ�name��number
		String sql = "select name,number from table" + newGroupPosition
				+ " where _id=?";
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, new String[] { newChildPosition
					+ "" });
			if (cursor.moveToFirst()) {
				//��Ϊ��ѯ����name��number����name��ǰ��number�ں�������������Ϣ
				String name = cursor.getString(0);
				String number = cursor.getString(1);
				result = name + "\n" + number;
			}
			cursor.close();
			db.close();
		}
		return result;
	}

	/**
	 * ��ȡÿһ���������е��Ӻ��ӵ���Ϣ��name��number��
	 * ��ÿ�����Ӷ����Զ�Ӧһ�ű������ڲ�ѯ�Ӻ��ӵ���Ϣ��ʱ����Ҫȷ���ǲ�ѯ���ź�������Ӧ�ı�table+position
	 */
	public static List<String> getChildNameByPosition(int groupPosition) {
		String result = null;
		List<String> results = new ArrayList<String>();
		String path = "/data/data/com.guoshisp.mobilesafe/files/commonnum.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		int newGroupPosition = groupPosition + 1;
		// ��ѯ�Ӻ��ӵ�name��number
		String sql = "select name,number from table" + newGroupPosition;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				//��Ϊ��ѯ����name��number����name��ǰ��number�ں�������������Ϣ
				String name = cursor.getString(0);
				String number = cursor.getString(1);
				result = name + "\n" + number;
				results.add(result);
				result = null;
			}
			cursor.close();
			db.close();
		}
		return results;
	}

}
