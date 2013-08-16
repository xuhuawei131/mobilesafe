package com.guoshisp.mobilesafe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.guoshisp.mobilesafe.db.dao.CommonNumDao;

public class CommonNumActivity extends Activity {
	protected static final String TAG = "CommonNumActivity";
	private ExpandableListView elv_common_num;//����չ��ListView
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_num);
		elv_common_num = (ExpandableListView) findViewById(R.id.elv_common_num);
		elv_common_num.setAdapter(new CommonNumberAdapter());//ΪExpandableListView����һ�����������󣬸ö�����Ҫ��ExpandableListAdapter���������
		//Ϊ�����е�ÿ������ע��һ��������
		elv_common_num.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//��ȡ��TextView�еĵ绰����
				TextView tv = (TextView) v;
				String number = tv.getText().toString().split("\n")[1];
				//ʹ����ʽ��ͼ�������ֻ�ϵͳ�еĲ�����
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				return false;
			}
		});
	}
	//ExpandableListView�����������󣬸ö�����ExpandableListAdapter���������
	private class CommonNumberAdapter extends BaseExpandableListAdapter {
		//�洢��Ӧ���е��Ӻ��ӵ���ϸ��Ϣ
		private List<String> groupNames;
		//���Ӻ��ӵ�������Ϣһ���Դ����ݿ��л�ȡ�������������Ա����ظ���ѯ���ݿ��ڴ滺�漯�ϡ�key�������λ��  value���������������Ӻ��ӵ���Ϣ
		private Map<Integer, List<String>> childrenCache; 

		public CommonNumberAdapter() {
			childrenCache = new HashMap<Integer, List<String>>();
		}

		/**
		 * ���ص�ǰ�б��ж�����
		 */
		public int getGroupCount() {
			// groupNames = CommonNumDao.getGroupNames();
			// return groupNames.size();
			return CommonNumDao.getGroupCount();
		}

		/**
		 * ����ÿһ�������ж��ٸ���Ŀ
		 */
		public int getChildrenCount(int groupPosition) {
			// if(childrenCache.containsKey(groupPosition)){
			// return childrenCache.get(groupPosition).size(); //���ػ��������
			// }else{
			// List<String> results =
			// CommonNumDao.getChildNameByPosition(groupPosition);
			// childrenCache.put(groupPosition, results);//�����ݷ��ڻ�������
			// return results.size();
			// }
			return CommonNumDao.getChildrenCount(groupPosition);
		}
		/**
		 * ���ط�������Ӧ�Ķ������������ò��������Է���null��
		 */
		public Object getGroup(int groupPosition) {
			return null;
		}
		/**
		 * ��ȡ�����е���Ŀ�������������ò��������Է���null��
		 */
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}
		/**
		 * ��ȡ��������Ӧ��id
		 */
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		/**
		 * ��ȡ�����е���Ŀ����Ӧ��id
		 */
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		/**
		 * �Ƿ�ҪΪ�����е���Ŀ����һ��id��false���������á�
		 */
		public boolean hasStableIds() {
			return false;
		}

		/**
		 * ����ÿһ�������view����.
		 * ����һ����ǰ�����id
		 * ����������ǰ�����View�Ƿ����չ
		 * �������������View����
		 * �����ģ���ǰ����ĸ�View����
		 */
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv;
			//ʹ�û����View����
			if (convertView == null) {
				tv = new TextView(getApplicationContext());
			} else {
				tv = (TextView) convertView;
			}
			tv.setTextSize(28);
			if (groupNames != null) {
				tv.setText("          " + groupNames.get(groupPosition));
			} else {
				groupNames = CommonNumDao.getGroupNames();
				tv.setText("          " + groupNames.get(groupPosition));
			}

			return tv;
		}

		/**
		 * ����ÿһ������ ĳһ��λ�ö�Ӧ�ĺ��ӵ�view����
		 * ����һ����ǰ�����id
		 * �������������е��Ӻ��ӵ�id
		 * �������������е��Ӻ����Ƿ������һ��
		 * �����ģ��Ӻ���View�Ļ������
		 * �����壺�����е��Ӻ������ڵĸ�View����
		 */
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {

				tv = new TextView(getApplicationContext());
			} else {
				tv = (TextView) convertView;
			}
			tv.setTextSize(20);
			String result = null;
			if (childrenCache.containsKey(groupPosition)) {
				result = childrenCache.get(groupPosition).get(childPosition);
			} else {
				List<String> results = CommonNumDao
						.getChildNameByPosition(groupPosition);
				childrenCache.put(groupPosition, results);// �����ݷ��ڻ�������
				result = results.get(childPosition);
			}
			tv.setText(result);
			return tv;
		}
		/**
		 * ����ֵ���Ϊtrue�����ʾÿ��������Ӻ��Ӷ�������Ӧ������¼������򣬲�������Ӧ
		 */
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}
}
