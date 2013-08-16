package com.guoshisp.mobilesafe.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.guoshisp.mobilesafe.db.BlackNumberDBOpenHelper;

public class TestCreatDB extends AndroidTestCase {
	public void testCreateDB() throws Exception {
		//测试数据库blacknumber.db是否会被创建.
		BlackNumberDBOpenHelper helper = new BlackNumberDBOpenHelper(
				getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
	}
}
