package com.guoshisp.mobilesafe.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.guoshisp.mobilesafe.db.BlackNumberDBOpenHelper;

public class TestCreatDB extends AndroidTestCase {
	public void testCreateDB() throws Exception {
		//�������ݿ�blacknumber.db�Ƿ�ᱻ����.
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(
				getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
	}
}
