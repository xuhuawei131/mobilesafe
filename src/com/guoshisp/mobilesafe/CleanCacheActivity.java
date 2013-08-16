package com.guoshisp.mobilesafe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CleanCacheActivity extends Activity {
	// ��ʾɨ��Ľ���
	private ProgressBar pd;
	// ��ʾɨ���״̬
	private TextView tv_clean_cache_status;
	// ϵͳ�İ�������
	private PackageManager pm;
	// �洢���л����Ӧ�õ�����
	private List<String> cachePagenames;
	// ��ʾ���д��л����Ӧ�ó�����Ϣ
	private LinearLayout ll_clean;
	// ��Ż�����Ϣ
	private Map<String, Long> cacheinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ceanl_ce);
		pd = (ProgressBar) findViewById(R.id.progressBar1);
		ll_clean = (LinearLayout) findViewById(R.id.ll_clean_cache_cont);
		tv_clean_cache_status = (TextView) findViewById(R.id.tv_clean_cache_status);
		pm = getPackageManager();
		scanPackages();
	}

	// ɨ������л����Ӧ�ó���
	private void scanPackages() {
		// ����һ���첽����ɨ����л����Ӧ�ó���
		new AsyncTask<Void, Integer, Void>() {
			// �洢�ֻ��������Ѱ�װ��Ӧ�ó���İ���Ϣ
			List<PackageInfo> packinfos;

			@Override
			protected Void doInBackground(Void... params) {
				int i = 0;
				for (PackageInfo info : packinfos) {
					// ��ȡ��Ӧ�ó���İ�����Ϣ
					String packname = info.packageName;
					getSize(pm, packname);
					i++;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					publishProgress(i);
				}
				return null;
			}

			@Override
			protected void onPreExecute() {
				cachePagenames = new ArrayList<String>();
				cacheinfo = new HashMap<String, Long>();
				packinfos = pm.getInstalledPackages(0);
				pd.setMax(packinfos.size());
				tv_clean_cache_status.setText("��ʼɨ��...");

				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(Void result) {

				tv_clean_cache_status.setText("ɨ�����..." + "������"
						+ cachePagenames.size() + "��������Ϣ");
				for (final String packname : cachePagenames) {
					// ��ȡ��ЩӦ�ó����ͼ�꣬���ƣ�չ���ڽ����ϡ�
					View child = View.inflate(getApplicationContext(),
							R.layout.cache_item, null);
					// Ϊchildע��һ����������
					child.setOnClickListener(new OnClickListener() {
						// ���childʱ��Ӧ�ĵ���¼�
						@Override
						public void onClick(View v) {
							// �ж�SDK�İ汾��
							if (Build.VERSION.SDK_INT >= 9) {
								// ��ת���������桱�Ľ��棨����ͨ��������-->Ӧ�ó���-->�������Ӧ�ó����Ľ��棩
								Intent intent = new Intent();
								intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setData(Uri.parse("package:" + packname));
								startActivity(intent);
							} else {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.addCategory("android.intent.category.VOICE_LAUNCH");
								intent.putExtra("pkg", packname);
								startActivity(intent);
							}
						}
					});
					// Ϊchild�еĿؼ���������
					ImageView iv_icon = (ImageView) child
							.findViewById(R.id.iv_cache_icon);
					iv_icon.setImageDrawable(getApplicationIcon(packname));
					TextView tv_name = (TextView) child
							.findViewById(R.id.tv_cache_name);
					tv_name.setText(getApplicationName(packname));
					TextView tv_size = (TextView) child
							.findViewById(R.id.tv_cache_size);
					tv_size.setText("�����С :"
							+ Formatter.formatFileSize(getApplicationContext(),
									cacheinfo.get(packname)));
					// ��child��ӵ�ll_clean�ؼ��ϡ�
					ll_clean.addView(child);
				}
				super.onPostExecute(result);
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				pd.setProgress(values[0]);
				tv_clean_cache_status.setText("����ɨ��" + values[0] + "��Ŀ");
				super.onProgressUpdate(values);
			}
		}.execute();

	}

	// ͨ������ķ�ʽ����packageManager�еķ���
	private void getSize(PackageManager pm, String packname) {

		try {
			// ��ȡ��getPackageSizeInfo������getPackageSizeInfo������Ҫ���嵥�ļ�������Ȩ����Ϣ��<uses-permission
			// android:name="android.permission.GET_PACKAGE_SIZE"/>
			Method method = pm.getClass().getDeclaredMethod(
					"getPackageSizeInfo",
					new Class[] { String.class, IPackageStatsObserver.class });
			// ִ��getPackageSizeInfo����
			method.invoke(pm,
					new Object[] { packname, new MyObersver(packname) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ִ��packageManager�е�getPackageSizeInfo����ʱ��Ҫ����IPackageStatsObserver.Stub�ӿڣ��ýӿ�ͨ��aidl���á�
	private class MyObersver extends IPackageStatsObserver.Stub {
		private String packname;

		public MyObersver(String packname) {
			this.packname = packname;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// �����Ǹ���ApplicationsState�����е�SizeInfo�����ж����
			// �����С
			long cacheSize = pStats.cacheSize;
			// �����С
			long codeSize = pStats.codeSize;
			// ���ݵĴ�С
			long dataSize = pStats.dataSize;
			// �ж����������Ӧ��Ӧ�ó����Ƿ��л��棬����У�����뵽�����С�
			if (cacheSize > 0) {
				cachePagenames.add(packname);
				cacheinfo.put(packname, cacheSize);
			}
		}
	}

	// ��ȡ��Ӧ�ó��������
	private String getApplicationName(String packname) {
		try {
			PackageInfo packinfo = pm.getPackageInfo(packname, 0);
			return packinfo.applicationInfo.loadLabel(pm).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return packname;
		}
	}

	// ��ȡ��Ӧ�ó����ͼ��
	private Drawable getApplicationIcon(String packname) {
		try {
			PackageInfo packinfo = pm.getPackageInfo(packname, 0);
			return packinfo.applicationInfo.loadIcon(pm);

		} catch (Exception e) {
			e.printStackTrace();
			return getResources().getDrawable(R.drawable.ic_launcher);
		}
	}
}
