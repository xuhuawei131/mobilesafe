package com.guoshisp.mobilesafe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.domain.AppInfo;
import com.guoshisp.mobilesafe.engine.AppInfoProvider;
import com.guoshisp.mobilesafe.utils.DensityUtil;

public class AppManagerActivity extends Activity implements OnClickListener{
	protected static final int LOAD_APP_FINSISH = 50;
	private static final String TAG = "AppManagerActivity";
	private TextView tv_appmanager_mem_avail;//��ʾ�ֻ������ڴ�
	private TextView tv_appmanager_sd_avail;//��ʾSdcard�����ڴ�
	private ListView lv_appmanager;//չʾ�û�����ϵͳ����
	private LinearLayout ll_loading;//ProgressBar�ĸ��ؼ������ڿ��Ƹÿؼ��е��ӿؼ�����ʾ
	private PackageManager pm; // �൱��windowsϵͳ����ĳ�������������Ի�ȡ�ֻ������е�Ӧ�ó���
	private List<AppInfo> appinfos;//����ֻ������е�Ӧ�ó����û�����+ϵͳ����
	private List<AppInfo> userappInfos;//����û�����
	private List<AppInfo> systemappInfos;//���ϵͳ����
	//PopupWindow��contentView��Ӧ�������ؼ�
	private LinearLayout ll_uninstall;//ж��
	private LinearLayout ll_start;//����
	private LinearLayout ll_share;//����

	private PopupWindow popupWindow;

	private String clickedpackname;
	//��Ӧ�ó��������߳���ȫ�����سɹ���֪ͨ���߳���ʾ����
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_APP_FINSISH:
				ll_loading.setVisibility(View.INVISIBLE);
				lv_appmanager.setAdapter(new AppManagerAdapter());
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.app_manager);
		super.onCreate(savedInstanceState);
		tv_appmanager_mem_avail = (TextView) findViewById(R.id.tv_appmanager_mem_avail);
		tv_appmanager_sd_avail = (TextView) findViewById(R.id.tv_appmanager_sd_avail);
		ll_loading = (LinearLayout) findViewById(R.id.ll_appmanager_loading);
		lv_appmanager = (ListView) findViewById(R.id.lv_appmanager);
		tv_appmanager_sd_avail.setText("SD������" + getAvailSDSize());
		tv_appmanager_mem_avail.setText("�ڴ����:" + getAvailROMSize());

		pm = getPackageManager();
		//��������Ӧ�ó��������
		fillData();
		//ΪListView���õ���¼�
		lv_appmanager.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//���û������һ��Itemʱ����Ҫ�ر��Ѿ����ڵ�PopupWindow
				dismissPopupWindow();
				//�������ļ�ת��view����view������ʾPopupWindow�е�����
				View contentView = View.inflate(getApplicationContext(),
						R.layout.popup_item, null);
				//�ֱ��ȡ��PopupWindow�����е�"ж�ء�����������"�ؼ�����Ӧ�ĸ��ؼ�
				ll_uninstall = (LinearLayout) contentView
						.findViewById(R.id.ll_popup_uninstall);
				ll_start = (LinearLayout) contentView
						.findViewById(R.id.ll_popup_start);
				ll_share = (LinearLayout) contentView
						.findViewById(R.id.ll_popup_share);
				//Ϊ"ж�ء�����������"���õ���¼�
				ll_share.setOnClickListener(AppManagerActivity.this);
				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				//��ȡ������ʾPopupWindow���ݵ�View�ĸ����֣�������ҪΪ�ò������ö������൱��ΪPopupWindow���ö�����
				LinearLayout ll_popup_container = (LinearLayout) contentView
						.findViewById(R.id.ll_popup_container);
				//����һ�����ŵĶ���Ч��
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				//���ö���ִ�е�ʱ��
				sa.setDuration(300);
				//��ȡ����ǰItem�Ķ���
				Object obj = lv_appmanager.getItemAtPosition(position);
				//��ItemΪϵͳӦ��ʱ����ʱΪPopupWindow�е�"ж��"����һ����ǣ���ж��ʱ�жϸñ�ǣ���ֹж��ϵͳӦ��
				if (obj instanceof AppInfo) {
					AppInfo appinfo = (AppInfo) obj;
					clickedpackname = appinfo.getPackname();
					if (appinfo.isUserapp()) {
						ll_uninstall.setTag(true);
					} else {
						ll_uninstall.setTag(false);
					}
				} else {
					return;
				}
				//��ȡ����ǰItem�붥�����ײ��ľ���
				int top = view.getTop();
				int bottom = view.getBottom();            
				//����PopupWindow����ʱ����Ҫָ������Ĵ�С�����򲻻���ʾ�ڽ����ϡ�����һ��������������ʾ���ݵ�viewContent����������������ʾPopupWindow����Ŀ�͸�
				popupWindow = new PopupWindow(contentView, DensityUtil.dip2px(getApplicationContext(), 200), bottom - top
						+ DensityUtil.dip2px(getApplicationContext(), 20));
				// ע��:һ��Ҫ��popwindow���ñ���ͼƬ�򱳾���Դ,��������ñ�����Դ , ������ ����Ĵ��� ����������⡣
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				//��ȡ��Item�ڴ�������ʾ��λ��
				int[] location = new int[2];
				view.getLocationInWindow(location);
				//����һ��PopupWindow�������Ǹ�View�ϣ�������������PopupWindow��ʾ������λ��
				//��������PopupWindow��View��X���ƫ�����������ģ�PopupWindow��View��Y���ƫ������X��Y���ƫ����������ڵ�ǰActivity���ڵĴ��壬���յ�Ϊ��0��0��
				popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT,
						location[0] + 20, location[1]);
				// ����һ�����ŵĶ���.
				ll_popup_container.startAnimation(sa);
			}
		});

		/**
		 * ���û����������ʱ��,��Ҫ�ر��Ѿ����ڵ�PopupWindow
		 */
		lv_appmanager.setOnScrollListener(new OnScrollListener() {

			// ��listview�Ĺ���״̬�����ı��ʱ�� ���õķ���.
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// ��listview���ڹ���״̬��ʱ�� ���õķ���
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				   dismissPopupWindow();
			}
		});

	}

	/**	
	 * ���ֻ��е�Ӧ�ó���ȫ����ȡ����
	 */
	private void fillData() {
		//��������ʱ��ll_loading�ؼ��е�ProgressBar�Լ�TextView��Ӧ�ġ����ڼ�������...����ʾ����
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				AppInfoProvider provider = new AppInfoProvider(
						AppManagerActivity.this);
				appinfos = provider.getInstalledApps();
				initAppInfo();
				//�����̷߳�����Ϣ
				Message msg = Message.obtain();
				msg.what = LOAD_APP_FINSISH;
				handler.sendMessage(msg);
			};

		}.start();
	}

	/**
	 * ��ʼ��ϵͳ���û�appinfos�ļ���
	 */
	protected void initAppInfo() {
		systemappInfos = new ArrayList<AppInfo>();
		userappInfos = new ArrayList<AppInfo>();
		for (AppInfo appinfo : appinfos) {
			//���ֳ��û������ϵͳ����
			if (appinfo.isUserapp()) {
				userappInfos.add(appinfo);
			} else {
				systemappInfos.add(appinfo);
			}
		}
	}
	/**
	 * ��Activity����ʱ����Ҫ�ر�PopupWindow����ΪPopupWindow������TextView��
	 * ������رոô���Ļ�������Ӱ������ִ�У���Log�л����"AppManagerActivity has leaked window"�ĺ�ɫ������ʾ��
	 */
	@Override
	protected void onDestroy() {
		dismissPopupWindow();
		super.onDestroy();
	}

	//����������
	private class AppManagerAdapter extends BaseAdapter {
		//��ȡListView��Item������
		public int getCount() {
			// ��ΪlistviewҪ����ʾ������Ŀ���û������ϵͳ����
			return userappInfos.size() + 1 + systemappInfos.size() + 1;
		}
		//��ȡ��Item����Ӧ�Ķ���
		public Object getItem(int position) {
			//��position == 0���Ӧ���ǡ��û�������Ŀ
			if (position == 0) {
				return position;
			} else if (position <= userappInfos.size()) {//��position <= userappInfos.size()���Ӧ�����ֻ��������û�������Ŀ
				// Ҫ��ʾ���û��������Ŀ�ڼ����е�λ�ã���Ϊ�û������Ӧ��Item�Ǵ�1��ʼ�ģ��������еĽǱ��Ǵ�0��ʼ�ģ�
				int newpostion = position - 1;
				return userappInfos.get(newpostion);
			} else if (position == (userappInfos.size() + 1)) {//��position == (userappInfos.size() + 1)���Ӧ���ǡ�ϵͳ������Ŀ
				return position;
			} else {//����ϵͳӦ����Ŀ
				int newpostion = position - userappInfos.size() - 2;
				return systemappInfos.get(newpostion);
			}
		}
		//��ȡItem����Ӧ��id
		public long getItemId(int position) {
			return position;
		}
		//��View��ʾ��Item�ϣ�ÿ��ʾһ��Item������һ�θ÷���
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {//��position == 0���Ӧ���ǡ��û�������Ŀ�����Ǵ���������Ŀ��Ӧ��View
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(20);
				tv.setText("�û����� (" + userappInfos.size() + ")");
				return tv;
			} else if (position <= userappInfos.size()) {//��position <= userappInfos.size()���Ӧ�����ֻ��������û�������Ŀ
				// Ҫ��ʾ���û��������Ŀ�ڼ����е�λ�ã���Ϊ�û������Ӧ��Item�Ǵ�1��ʼ�ģ��������еĽǱ��Ǵ�0��ʼ�ģ�
				int newpostion = position - 1;
				View view;
				ViewHolder holder;
				//������ʷ����
				if (convertView == null || convertView instanceof TextView) {
					view = View.inflate(getApplicationContext(),
							R.layout.app_manager_item, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView) view
							.findViewById(R.id.iv_appmanger_icon);
					holder.tv_name = (TextView) view
							.findViewById(R.id.tv_appmanager_appname);
					holder.tv_version = (TextView) view
							.findViewById(R.id.tv_appmanager_appversion);
					view.setTag(holder);
				} else {
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}
				//Ϊ�û�Ӧ�ó�����������
				AppInfo appInfo = userappInfos.get(newpostion); // ���û����򼯺������ȡ���ݵ���Ŀ
				holder.iv_icon.setImageDrawable(appInfo.getAppicon());
				holder.tv_name.setText(appInfo.getAppname());
				holder.tv_version.setText("�汾��:" + appInfo.getVersion());
				return view;

			} else if (position == (userappInfos.size() + 1)) {//��position == (userappInfos.size() + 1)���Ӧ���ǡ�ϵͳ������Ŀ
				TextView tv = new TextView(getApplicationContext());
				tv.setTextSize(20);
				tv.setText("ϵͳ���� (" + systemappInfos.size() + ")");
				return tv;
			} else {//����ϵͳӦ�õ�Item
				int newpostion = position - userappInfos.size() - 2;
				View view;
				ViewHolder holder;
				if (convertView == null || convertView instanceof TextView) {
					view = View.inflate(getApplicationContext(),
							R.layout.app_manager_item, null);
					holder = new ViewHolder();
					holder.iv_icon = (ImageView) view
							.findViewById(R.id.iv_appmanger_icon);
					holder.tv_name = (TextView) view
							.findViewById(R.id.tv_appmanager_appname);
					holder.tv_version = (TextView) view
							.findViewById(R.id.tv_appmanager_appversion);
					view.setTag(holder);
				} else {
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}
				//ΪϵͳӦ�ó�����������
				AppInfo appInfo = systemappInfos.get(newpostion); // ��ϵͳ���򼯺������ȡ���ݵ���Ŀ
				holder.iv_icon.setImageDrawable(appInfo.getAppicon());
				holder.tv_name.setText(appInfo.getAppname());
				holder.tv_version.setText("�汾��:" + appInfo.getVersion());
				return view;

			}
		}

		/**
		 * ���ε�����TextView���û������ϵͳ���򣩱����ʱ�Ľ���
		 */
		@Override
		public boolean isEnabled(int position) {
			if (position == 0 || position == (userappInfos.size() + 1)) {
				return false;
			}
			return super.isEnabled(position);
		}
	}
	//��Item�еĿؼ�ʹ��static���Σ���static���ε�����ֽ�����JVM��ֻ�����һ�ݡ�iv_icon��tv_name��tv_version��ջ��Ҳ��ֻ����һ��
	private static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_version;
	}

	/**
	 * ��ȡsd�����õ��ڴ��С
	 * 
	 * @return
	 */
	private String getAvailSDSize() {
		//��ȡSdcard��Ŀ¼���ڵ��ļ�����
		File path = Environment.getExternalStorageDirectory();
		//״̬�ռ����
		StatFs stat = new StatFs(path.getPath());
		// ��ȡSdcard�����ж��ٿ����������Sdcard�Ŀռ䱻��Ϊ��飩
		long totalBlocks = stat.getBlockCount();
		// ��ȡSdcard�����õķ�������
		long availableBlocks = stat.getAvailableBlocks();
		// ��ȡSdcard��ÿһ��������Դ�ŵ�byte����
		long blockSize = stat.getBlockSize();
		//�������ܵ�byte
		long availSDsize = availableBlocks * blockSize;
		//����Formatter������ת��ΪM
		return Formatter.formatFileSize(this, availSDsize);
	}

	/**
	 * ��ȡ�ֻ�ʣ����õ��ڴ�ռ�
	 * 
	 * @return
	 */
	private String getAvailROMSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(this, availableBlocks * blockSize);
	}
	/**
	 * ���û��ڽ����ϵ����һ��Itemʱ��Ҫ�ر���һ��PopupWindow
	 */
	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	/**
	 * PopupWindow�еĵ���¼�
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_popup_share:
			Log.i(TAG, "����");
			shareApplication();
			break;

		case R.id.ll_popup_start:
			Log.i(TAG, "����");
			startAppliction();

			break;
		case R.id.ll_popup_uninstall:
			//��ȡ��ItemΪ��ll_popup_uninstall�����õı��
			boolean result = (Boolean) v.getTag();
			//��ֹж��ϵͳӦ��
			if (result) {
				Log.i(TAG, "ж��" + clickedpackname);
				uninstallApplication();
			}else{
				Toast.makeText(this, "ϵͳӦ�ò��ܱ�ж��", 1).show();
			}
			break;
		}

	}

	/**
	 * ����һ��Ӧ�ó���
	 */
	private void shareApplication() {
        /*<intent-filter>
        <action android:name="android.intent.action.SEND" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
        </intent-filter>*/
		Intent intent = new Intent();
		//ͨ����ͼ�ĵĶ����������������ֻ��о��з����ܵ�Ӧ�ã����ţ�������...������д���з����ܵ�Ӧ�û����б�ĸ�ʽչ�ֳ���
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		//���������Ϊ�ı�����
		intent.setType("text/plain");
		//���÷���ı���
		intent.putExtra("subject", "����ı���");
		//���÷����Ĭ������
		intent.putExtra("sms_body", "�Ƽ���ʹ��һ�����"+clickedpackname);
		intent.putExtra(Intent.EXTRA_TEXT, "extra_text");
		startActivity(intent);
	}

	/**
	 * ж��һ��Ӧ�ó���
	 */
	private void uninstallApplication() {
		
		/* * <intent-filter> <action android:name="android.intent.action.VIEW" />
		 * <action android:name="android.intent.action.DELETE" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="package" /> </intent-filter>*/
		 
		dismissPopupWindow();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + clickedpackname));
		//ж��һ��Ӧ�ó���󣬶�Ӧ��Sdcard���ڴ�ᷢ���仯����ʱ����Ӧ�����¸���Ϣ��������Ҫ��ж�ص�Ӧ�ô��б����Ƴ�
		startActivityForResult(intent, 1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			// ֪ͨ�����������.
			fillData();
			tv_appmanager_sd_avail.setText("SD������" + getAvailSDSize());
			tv_appmanager_mem_avail.setText("�ڴ����:" + getAvailROMSize());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ����һ��Ӧ�ó���
	 */
	private void startAppliction() {
		dismissPopupWindow();
		Intent intent = new Intent();
		PackageInfo packinfo;
		try {
			//PackageManager.GET_ACTIVITIES���߰������ߣ��ڽ����嵥�ļ�ʱ��ֻ����Activity��Ӧ�Ľڵ�
			packinfo = pm.getPackageInfo(clickedpackname,
					PackageManager.GET_ACTIVITIES);

			ActivityInfo[] activityinfos = packinfo.activities;
			//�ж��嵥�ļ����Ƿ����Activity��Ӧ�Ľڵ�
			if (activityinfos != null && activityinfos.length > 0) {
				//�����嵥�ļ��еĵ�һ��Activity�ڵ�
				String className = activityinfos[0].name;
				intent.setClassName(clickedpackname, className);
				startActivity(intent);
			} else {
				Toast.makeText(this, "����������ǰӦ��", 0).show();
			}
		} catch (NameNotFoundException e) {//ʹ��C����ʵ�ֵ�Ӧ�ó�����DDMS��û�ж�Ӧ�İ���
			e.printStackTrace();
			Toast.makeText(this, "����������ǰӦ��", 0).show();
		}
	}
	/**
	 * ��ȡ�����������Ե�intent ϵͳ����Ӧ��(luncher)
	 */
	public Intent getIntent() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		List<ResolveInfo> resoveInfo = pm.queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS
						| PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo info : resoveInfo) {
			// info.activityInfo.packageName;
		}
		return null;
	}
}
