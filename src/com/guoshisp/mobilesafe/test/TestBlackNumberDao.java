package com.guoshisp.mobilesafe.test;

import java.util.List;
import java.util.Random;

import android.test.AndroidTestCase;

import com.guoshisp.mobilesafe.db.dao.BlackNumberDao;
import com.guoshisp.mobilesafe.domain.BlackNumber;

public class TestBlackNumberDao extends AndroidTestCase {
	//�����ݿ������50������
	public void testAdd() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		//��һ��Ҫ����ӵĺ���
		int number = 100000;
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
			int result = (number+i);
			//ִ����Ӳ�����random.nextInt(3)��ʾ�������Ϊ0��1��2
			dao.add(result+"", random.nextInt(3)+"");
		}
	}
	//�������ݿ��е�����
	public void testUpdate() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update("100000", "999999", "2");
	}
	//ɾ�����ݿ��е�����
	public void testDelete() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("999999");
	}
	//��ѯ���ݿ���е����еĺ���
	public void testFindAll() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<BlackNumber> numbers = dao.findAll();
		System.out.println(numbers.size());
	}
}
