package com.guoshisp.mobilesafe.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoshisp.mobilesafe.R;

public class MainAdapter extends BaseAdapter {
	// ���������
	private LayoutInflater inflater;
	// ����MainActivity���ݹ����������Ķ���
	private Context context;
	//�����滻���ֻ����������±���
	private String newname;
	private static final int[] icons = { R.drawable.widget01,
			R.drawable.widget02, R.drawable.widget03, R.drawable.widget04,
			R.drawable.widget05, R.drawable.widget06, R.drawable.widget07,
			R.drawable.widget08, R.drawable.widget09 };
	// ���Ÿ�item��ÿһ�����ⶼ�����������
	private static final String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "�ֻ�ɱ��", "ϵͳ�Ż�", "�߼�����", "��������" };

	public MainAdapter(Context context) {
		this.context = context;

		// ��ȡϵͳ�еĲ��������
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//��ȡ�����滻���ֻ����������±��⣬Ĭ��ֵΪ��
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		newname = sp.getString("newname", "");
	}

	/**
	 * ����gridview�ж��ٸ�item
	 */
	public int getCount() {
		return names.length;
	}

	/**
	 * ��ȡÿ��item����������ǲ���������ص�item��������Ӧ�Ĳ����Ļ��� ���ǿ��Է���һ��null�� �������Ǽ򵥴���һ�£�����position
	 */
	public Object getItem(int position) {
		return position;
	}

	/**
	 * ���ص�ǰitem��id
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ����ÿһ��gridview����Ŀ�е�view����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.main_item, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_main_item_name);
		ImageView iv_icon = (ImageView) view
				.findViewById(R.id.iv_main_item_icon);
		tv_name.setText(names[position]);
		iv_icon.setImageResource(icons[position]);
		//��һ��Item��Ҳ�����ֻ���������Ӧ��Item
		if (position == 0) {
			//�ж�sp��ȡ����newname�Ƿ�Ϊ�գ������Ϊ�յĻ��������ֻ���������Ӧ�ı����޸�Ϊsp���޸ĺ�ı���
			if (!TextUtils.isEmpty(newname)) {
				tv_name.setText(newname);
			}
		}

		return view;
	}
}
