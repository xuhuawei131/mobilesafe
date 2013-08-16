package com.guoshisp.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ��������ز�ѯ-�������ݿ�
 * @author
 */
public class NumberAddressDao {

	/**
	 * ��ȡ�绰����Ĺ�����
	 * @param number
	 * @return
	 */
	public static String getAddress(String number) {
		// ���û�в�ѯ������Ĺ����� ���ͷ��ص�ǰ�ĵ绰����
		String address = number;
		//���ݿ����ֻ�ϵͳ�е�ȫ·��
		String path = "/data/data/com.guoshisp.mobilesafe/files/address.db";
		//�����ݿ⡣��������CursorFactory�α깤����null��ʾʹ��ϵͳĬ�ϵ�
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		//�ж����ݿ��Ƿ񱻴�
		if (db.isOpen()) {
			// �жϺ��������.
			if (number.matches("^1[3458]\\d{9}$")) {// �ֻ����� ^����ʼ   ��ʼΪ1  �ڶ�λ������3458  \d��ʾ0-9��һ������ \������ת�⡰\�� ��9����ʾ��9�����֣������ʾ������9��0-9������
				//����һ����ѯ�Ľ����
				Cursor cursor = db
						.rawQuery(
								"select city from address_tb where _id=(select outkey from numinfo where mobileprefix =?)",
								new String[] { number.substring(0, 7) });//ƥ���ֻ���ǰ��λ
				if (cursor.moveToFirst()) {
					address = cursor.getString(0);//��ȡ��0�м���
				}
				cursor.close();

			} else {// �������� (�̶��绰)  
				Cursor cursor;
				switch (number.length()) {//����ĳ���
				case 4:
					address = "ģ����";
					break;
				case 7://���غ��벻��ʾ����
					address = "���غ���";
					break;
				case 8:
					address = "���غ���";
					break;
				case 10:
					//�Ӳ�ѯ���ؽ���л�ȡ��һ������  ��limit��ʾֻ��ȡ��һ�����ݣ�
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 3) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					break;
				case 12://4λ������+8λ����
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 4) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					break;
				case 11://3λ����+8λ�ĺ��룬������4λ������+7λ����
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 3) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					cursor = db
							.rawQuery(
									"select city from address_tb where area = ? limit 1",
									new String[] { number.substring(0, 4) });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					break;
				}
			}

			db.close();
		}

		return address;
	}
}
