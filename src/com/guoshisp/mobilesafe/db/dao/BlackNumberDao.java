package com.guoshisp.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.guoshisp.mobilesafe.db.BlackNumberDBOpenHelper;
import com.guoshisp.mobilesafe.domain.BlackNumber;

public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * ����һ�����������루�䷵��ֵ�������ж����ݿ����Ƿ���ڸú��룩
	 */
	public boolean find(String number) {
		// Ĭ���������û�и�������
		boolean result = false;
		// �����ݿ�
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			// ִ�в�ѯ���󣬷���һ�������
			Cursor cursor = db.rawQuery(
					"select * from blacknumber where number =?",
					new String[] { number });
			// Ĭ������£��α�ָ��ָ���ڵ�һ�����ݵ��Ϸ�
			if (cursor.moveToFirst()) {
				// ����true��˵�����ݿ����Ѿ������˸�������
				result = true;
			}
			// �ر����ݿ�
			cursor.close();
			db.close();
		}
		return result;
	}

	/**
	 * ����һ�����������������ģʽ
	 */
	public int findNumberMode(String number) {
		// ����ģʽֻ��3�֣�0�������ض��ţ�1�������ص绰��2�������ض�����绰�������Ĭ��ֵΪ-1����ʾ����û�б������ģʽ
		int result = -1;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			// ����һ������ֵ��Ӧһ������ģʽ�����ԣ��ý������ֻ��һ������
			Cursor cursor = db.rawQuery(
					"select mode from blacknumber where number =?",
					new String[] { number });
			if (cursor.moveToFirst()) {
				// ��ȡ��һ�����ݣ�Ҳ����һ�����ݣ�
				result = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		}
		return result;
	}

	/**
	 * ���һ������������
	 */
	public boolean add(String number, String mode) {
		// �����ж����ݿ����Ƿ��Ѿ����ڸ������ݣ� ��ֹ����ظ���������ʾ���������б���
		if (find(number))
			// ������ݿ����Ѿ�����Ҫ��ӵ����ݣ�ֱ��ֹͣ���÷�����ִ��
			return false;
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			// ִ��������ݵ�SQL���
			db.execSQL("insert into blacknumber (number,mode) values (?,?)",
					new Object[] { number, mode });
			db.close();
		}
		// ��������ܹ�ִ�е���һ����˵���������Ӳ���Ҳִ���ˡ����Բ�ѯ�ķ���ֵ�ض�Ϊtrue
		return find(number);
	}

	/**
	 * ɾ��һ������������
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			// ִ��ɾ������
			db.execSQL("delete from blacknumber where number=?",
					new Object[] { number });
			db.close();
		}
	}

	/**
	 * ���ĺ���������
	 * 
	 * @param oldnumber
	 *            �ɵĵĵ绰����
	 * @param newnumber
	 *            �µĺ��� ��������
	 * @param mode
	 *            �µ�ģʽ
	 */
	public void update(String oldnumber, String newnumber, String mode) {

		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			if (TextUtils.isEmpty(newnumber)) {
				// ����µĺ���Ϊ�յĻ�����˵���û���û���޸ĸú��루ListView�е�item������ɾ�����ܣ�
				newnumber = oldnumber;
			}
			// ִ�и��²���
			db.execSQL(
					"update blacknumber set number=?, mode=? where number=?",
					new Object[] { newnumber, mode, oldnumber });
			db.close();
		}
	}

	/**
	 * ����ȫ���ĺ���������
	 * 
	 * @return
	 */
	public List<BlackNumber> findAll() {
		//�����Ҫ���صĶ���
		List<BlackNumber> numbers = new ArrayList<BlackNumber>();
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			//��ѯblacknumber���е����к���
			Cursor cursor = db.rawQuery("select number,mode from blacknumber",
					null);
			//ѭ���������������ÿ���������װ����ӵ�������
			while (cursor.moveToNext()) {
				BlackNumber blackNumber = new BlackNumber();
				blackNumber.setNumber(cursor.getString(0));
				blackNumber.setMode(cursor.getInt(1));
				numbers.add(blackNumber);
				blackNumber = null;
			}
			cursor.close();
			db.close();
		}
		return numbers;
	}
}
