package com.guoshisp.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guoshisp.mobilesafe.domain.ProcessInfo;
import com.guoshisp.mobilesafe.engine.ProcessInfoProvider;
import com.guoshisp.mobilesafe.view.MyToast;

public class TaskManagerActivity extends Activity implements OnClickListener {
	// ��ʾ�û�����
	private ListView lv1;
	// ��ʾϵͳ����
	private ListView lv2;
	// �жϵ�ǰ��ʾ���б����û����̻���ϵͳ����
	private boolean showUserApp;
	// �л��û����̺�ϵͳ���̵İ�ť��������Ӧ��ȫѡ���롰һ��������ťʱ���ж����û����̣�����ϵͳ���̣�
	private Button bt_user, bt_system;
	// �û����������б��������
	private UserAdapter useradapter;
	// ϵͳ���������б��������
	private SystemAdapter systemadapter;
	// ���ڻ�ȡ�ֻ��еĽ���
	private ProcessInfoProvider provider;
	// Ϊϵͳ������ӵ�һ��Item����Item����ʾ��ɱ��ϵͳ���̻ᵼ��ϵͳ���ȶ������֡�
	private TextView tvheader;
	// ����û����̵ļ���
	private List<ProcessInfo> userProcessInfos;
	// ���ϵͳ���̵ļ���
	private List<ProcessInfo> systemProcessInfos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_manager);
		// Ĭ������£���ʾ�����û������б�
		showUserApp = true;
		provider = new ProcessInfoProvider(this);
		userProcessInfos = new ArrayList<ProcessInfo>();
		systemProcessInfos = new ArrayList<ProcessInfo>();
		// ͨ��provider����ȡ�ֻ��е����н���
		List<ProcessInfo> mRunningProcessInfos = provider.getProcessInfos();
		// ����ȡ�������н��̽��з���洢���û����̺�ϵͳ���̣�
		for (ProcessInfo info : mRunningProcessInfos) {
			if (info.isUserprocess()) {
				userProcessInfos.add(info);
			} else {
				systemProcessInfos.add(info);
			}
		}
		// �û����̶�Ӧ��ListView������ListView�ĵ���¼�
		lv1 = (ListView) findViewById(R.id.lv_usertask);
		lv1.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox) view.findViewById(R.id.cb_taskmanager);
				// ��ȡ���������Item����
				ProcessInfo info = (ProcessInfo) lv1
						.getItemAtPosition(position);
				// �жϱ������Item�Ƿ��������Լ����ֻ���ȫ��ʿ��Ŀ�����ǲ�����ɱ���Լ���Ӧ�ó���
				if (info.getPackname().equals(getPackageName())) {
					return;
				}
				// �ֶ�������Checkbox��״̬
				if (info.isChecked()) {
					info.setChecked(false);
					cb.setChecked(false);
				} else {
					info.setChecked(true);
					cb.setChecked(true);
				}

			}
		});
		// ϵͳ���̶�Ӧ��ListView������ListView�ĵ���¼�
		lv2 = (ListView) findViewById(R.id.lv_systemtask);
		lv2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ֹ ��Ӧϵͳ�����еĵ�һ��Item�ĵ���¼�
				if (view instanceof TextView) {
					return;
				}
				CheckBox cb = (CheckBox) view.findViewById(R.id.cb_taskmanager);
				// ��ȡ���������Item����
				ProcessInfo info = (ProcessInfo) lv2
						.getItemAtPosition(position);
				// �ֶ�������Checkbox��״̬
				if (info.isChecked()) {
					info.setChecked(false);
					cb.setChecked(false);
				} else {
					info.setChecked(true);
					cb.setChecked(true);
				}
			}
		});
		// Ϊ���û����̡���ťע��һ��������
		bt_user = (Button) findViewById(R.id.bt_user);
		bt_user.setOnClickListener(this);
		bt_user.setBackgroundResource(R.drawable.bt_pressed);
		// Ϊ��ϵͳ���̡���ťע��һ��������
		bt_system = (Button) findViewById(R.id.bt_system);
		bt_system.setOnClickListener(this);
		bt_system.setBackgroundResource(R.drawable.bg_normal);
		// Ĭ���������ʾ�����û������б���������Ӧ����ϵͳ�����б�����Ϊ���ɼ���View.GONE��View��Ч���Ҳ�ռ�ÿռ�
		lv2.setVisibility(View.GONE);
		// Ϊ�û������б���������������
		useradapter = new UserAdapter();
		lv1.setAdapter(useradapter);
		// ����TextView��Ϊϵͳ���̶�Ӧ��ListView�ĵ�һ��Item
		tvheader = new TextView(getApplicationContext());
		tvheader.setText("ɱ��ϵͳ���̻ᵼ��ϵͳ���ȶ�");
		tvheader.setBackgroundColor(Color.YELLOW);
		// ��tvheader��ӵ�ϵͳ���̶�Ӧ��ListView�У���ʱ��tvheader���ڸ�ListView��һԱ����һ��Item���ǣ�����������������ǰ���
		lv2.addHeaderView(tvheader);
		// Ϊϵͳ�����б���������������
		systemadapter = new SystemAdapter();
		lv2.setAdapter(systemadapter);
	}

	// Ϊ�û����̶�Ӧ��ListView��������
	private class UserAdapter extends BaseAdapter {

		public int getCount() {
			return userProcessInfos.size();
		}

		public Object getItem(int position) {
			return userProcessInfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			// ʹ�ö��ڴ��е�Ψһ��һ���ֽ��루ListView���Ż���
			ViewHolder holder = new ViewHolder();
			// ���û��棨ListView���Ż���
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.task_manager_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_taskmanger_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_taskmanager_appname);
				holder.tv_mem = (TextView) view
						.findViewById(R.id.tv_taskmanager_mem);
				holder.cb = (CheckBox) view.findViewById(R.id.cb_taskmanager);
				view.setTag(holder);
			} else {
				// ʹ�û����view
				view = convertView;
				// ��ȡ�������view�ı��
				holder = (ViewHolder) view.getTag();
			}
			// ���û����̶�Ӧ�ļ�����ȡ����Ӧ��Ԫ�������ݵ�����
			ProcessInfo info = userProcessInfos.get(position);
			// ���Ӧ�ó����������Լ����ֻ���ʿ�����ص�Checkbox������ɱ���Լ���
			if (info.getPackname().equals(getPackageName())) {
				holder.cb.setVisibility(View.INVISIBLE);
			} else {
				holder.cb.setVisibility(View.VISIBLE);
			}
			// ΪItem�������ݣ�Ӧ��ͼ�ꡢ���ơ�ռ���ڴ��С���Ƿ���ѡ��״̬��Ĭ������¶���δѡ��״̬��
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_name.setText(info.getAppname());
			holder.tv_mem.setText(Formatter.formatFileSize(
					getApplicationContext(), info.getMemsize()));
			holder.cb.setChecked(info.isChecked());
			// ����Item��Ӧ��view
			return view;
		}
	}

	// ʹ��static���Σ����Ա�֤�ö����ڶ��ڴ���ֻ����һ���ֽ����ļ������е�Item���ø��ֽ����ļ���
	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_mem;
		CheckBox cb;
	}

	// Ϊϵͳ���̶�Ӧ��ListView��������
	private class SystemAdapter extends BaseAdapter {

		public int getCount() {
			return systemProcessInfos.size();
		}

		public Object getItem(int position) {
			return systemProcessInfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.task_manager_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_taskmanger_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_taskmanager_appname);
				holder.tv_mem = (TextView) view
						.findViewById(R.id.tv_taskmanager_mem);
				holder.cb = (CheckBox) view.findViewById(R.id.cb_taskmanager);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			ProcessInfo info = systemProcessInfos.get(position);
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_name.setText(info.getAppname());
			holder.tv_mem.setText(Formatter.formatFileSize(
					getApplicationContext(), info.getMemsize()));
			holder.cb.setChecked(info.isChecked());

			return view;
		}
	}

	// ��Ӧ�û����̡�ϵͳ���̵İ�ť�ĵ���¼�������û������б��ϵͳ�����б���л���
	public void onClick(View v) {
		switch (v.getId()) {
		// ��ϵͳ�����б��л����û������б����ߵ�ǰ�б��л�����ǰ�б�
		case R.id.bt_user:
			// �Ƴ�ϵͳ�����б��еĵ�һ��������ʾ��Item
			if (tvheader != null) {
				lv2.removeHeaderView(v);
				tvheader = null;
			}
			// ��ǰ��ʾ�����û�����
			showUserApp = true;
			// ����������ť�ı���ɫ����ʾ����
			bt_user.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bt_pressed));
			bt_system.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_normal));
			// ��lv1����Ϊ�ɼ���lv2����Ϊ���ɼ�
			lv1.setVisibility(View.VISIBLE);
			lv2.setVisibility(View.INVISIBLE);

			break;
		// ��ϵ�û����б��л���ϵͳ�����б����ߵ�ǰ�б��л�����ǰ�б�
		case R.id.bt_system:
			showUserApp = false;
			bt_system.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bt_pressed));
			bt_user.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_normal));
			lv2.setVisibility(View.VISIBLE);
			lv1.setVisibility(View.INVISIBLE);

			break;
		}

	}

	/**
	 * ȫѡ��ť�ĵ���¼�
	 */
	public void selectAll(View view) {
		//�ж����û�����ȫѡ������ϵͳ����ȫѡ
		if (showUserApp) {
			//�������̼��ϣ���ÿ��info�е�Checkbox������Ϊtrue��ѡ�У���Ȼ��֪ͨ������ˢ������
			for (ProcessInfo info : userProcessInfos) {
				info.setChecked(true);
				useradapter.notifyDataSetChanged();
			}

		} else {
			//�������̼��ϣ���ÿ��info�е�Checkbox������Ϊtrue��ѡ�У���Ȼ��֪ͨ������ˢ������
			for (ProcessInfo info : systemProcessInfos) {
				info.setChecked(true);
				systemadapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * һ������ĵ���¼�
	 */
	public void oneKeyClear(View v) {
		//��ȡ��ActivityManager���󣬸ö�������ɱ�����̵Ĳ���
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//����Ҫ��ɱ�����ٸ�����
		int count = 0;
		//����Ҫ��ɱ�������н�����ռ�õ��ڴ��С
		long memsize = 0;
		//����ѱ���ɱ���Ľ���
		List<ProcessInfo> killedProcessInfo = new ArrayList<ProcessInfo>();
		//�ж��������û����̻�������ϵͳ����
		if (showUserApp) {//�û�����
			for (ProcessInfo info : userProcessInfos) {
				//����Checkbox�Ĺ�ѡ״̬��ȷ����Щ������Ҫ������
				if (info.isChecked()) {
					//ִ���ۼӲ���
					count++;
					memsize += info.getMemsize();
					//ִ��kill����
					am.killBackgroundProcesses(info.getPackname());
					//��ɱ����Ľ��̴��뼯����
					killedProcessInfo.add(info);
				}
			}

		} else {//ϵͳ����
			for (ProcessInfo info : systemProcessInfos) {
				if (info.isChecked()) {
					count++;
					memsize += info.getMemsize();
					am.killBackgroundProcesses(info.getPackname());
					killedProcessInfo.add(info);

				}
			}
		}
		//��������ɱ���Ľ��̣��ж��ĸ������а����ý��̣�������������Ƴ��������ڸ����б���ʾ��
		for (ProcessInfo info : killedProcessInfo) {
			if (info.isUserprocess()) {
				if (userProcessInfos.contains(info)) {
					userProcessInfos.remove(info);
				}
			} else {
				if (systemProcessInfos.contains(info)) {
					systemProcessInfos.remove(info);
				}
			}
		}
		//����������ʾ
		if (showUserApp) {
			useradapter.notifyDataSetChanged();
		} else {
			systemadapter.notifyDataSetChanged();
		}

		
		/* Toast.makeText( this, "ɱ����" + count + "������,�ͷ���" +
		  Formatter.formatFileSize(this, memsize) + "�ڴ�", 1) .show();*/
		
		//ʹ���Զ����Toast����ʾɱ���Ľ��������Լ��ͷŵ��ڴ�ռ䡣
		MyToast.showToast(
				this,
				"ɱ����" + count + "������,�ͷ���"
						+ Formatter.formatFileSize(this, memsize) + "�ڴ�");
	}
}