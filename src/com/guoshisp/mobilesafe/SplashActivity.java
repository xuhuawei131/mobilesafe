package com.guoshisp.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.domain.UpdateInfo;
import com.guoshisp.mobilesafe.engine.UpdateInfoParser;
import com.guoshisp.mobilesafe.utils.AssetCopyUtil;
import com.guoshisp.mobilesafe.utils.DownLoadUtil;

public class SplashActivity extends Activity {
	private TextView tv_splash_version;
	private UpdateInfo info;

	private static final int GET_INFO_SUCCESS = 10;
	private static final int SERVER_ERROR = 11;
	private static final int SERVER_URL_ERROR = 12;
	private static final int PROTOCOL_ERROR = 13;
	private static final int IO_ERROR = 14;
	private static final int XML_PARSE_ERROR = 15;
	private static final int DOWNLOAD_SUCCESS = 16;
	private static final int DOWNLOAD_ERROR = 17;
	protected static final String TAG = "SplashActivity";
	private long startTime;
	private RelativeLayout rl_splash;
	private long endTime;
	private ProgressDialog pd;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case XML_PARSE_ERROR:
				Toast.makeText(getApplicationContext(), "xml��������", 1).show();
				loadMainUI();
				break;
			case IO_ERROR:
				Toast.makeText(getApplicationContext(), "I/O����", 1).show();
				loadMainUI();
				break;
			case PROTOCOL_ERROR:
				Toast.makeText(getApplicationContext(), "Э�鲻֧��", 1).show();
				loadMainUI();
				break;
			case SERVER_URL_ERROR:
				Toast.makeText(getApplicationContext(), "������·������ȷ", 1).show();
				loadMainUI();
				break;
			case SERVER_ERROR:
				Toast.makeText(getApplicationContext(), "�������ڲ��쳣", 1).show();
				loadMainUI();
				break;
			case GET_INFO_SUCCESS:
				String serverversion = info.getVersion();
				String currentversion = getVersion();
				if (currentversion.equals(serverversion)) {
					Log.i(TAG, "�汾����ͬ����������");
					loadMainUI();
				} else {
					Log.i(TAG, "�汾�Ų���ͬ,�����Ի���");
					showUpdateDialog();
				}
				break;

			case DOWNLOAD_SUCCESS:
				Log.i(TAG, "�ļ����سɹ�");
				File file = (File) msg.obj;
				installApk(file);
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(getApplicationContext(), "���������쳣", 1).show();
				loadMainUI();
				break;
			}
		};
	};

	/**
	 * ����������
	 */
	private void loadMainUI() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();// �ѵ�ǰ��Activity������ջ�����Ƴ�
	}

	/**
	 * ��װһ��apk�ļ�
	 * 
	 * @param file
	 *            Ҫ��װ�������ļ���
	 */
	protected void installApk(File file) {
		// ��ʽ��ͼ
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");// ������ͼ�Ķ���
		intent.addCategory("android.intent.category.DEFAULT");// Ϊ��ͼ��Ӷ��������
		// intent.setType("application/vnd.android.package-archive");
		// intent.setData(Uri.fromFile(file));
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");// ������ͼ������������
		startActivity(intent);// �������ͼ
	}

	/**
	 * ��ʾ������ʾ�ĶԻ���
	 */
	protected void showUpdateDialog() {
		// �����˶Ի���Ĺ�����
		AlertDialog.Builder builder = new Builder(this);
		// ���öԻ������ʾ����
		builder.setIcon(getResources().getDrawable(R.drawable.notification));
		// ������������
		builder.setTitle("������ʾ");
		// ����������ʾ����
		builder.setMessage(info.getDescription());
		// �������ؽ�����
		pd = new ProgressDialog(SplashActivity.this);
		// ���ý���������ʾʱ����ʾ��Ϣ
		pd.setMessage("��������");
		// ָ����ʾ���ؽ�����Ϊˮƽ��״
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// ����������ť
		builder.setPositiveButton("����", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "����,����" + info.getApkurl());
				// �ж�Sdcard�Ƿ����
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					pd.show();// ��ʾ���ؽ�����
					// �������߳�����apk
					new Thread() {

						public void run() {
							// ��ȡ������°汾apk�����ص�ַ
							String path = info.getApkurl();
							// ��ȡ����apk���ļ���
							String filename = DownLoadUtil.getFilename(path);
							// ��Sdcard�ĸ�Ŀ¼�ϴ���һ���ļ�����һ�����������ڻ�ȡSdcard�ĸ�Ŀ¼���ڶ�����������Sdcard�ĸ�Ŀ¼�ϴ������ļ����ļ���
							File file = new File(Environment
									.getExternalStorageDirectory(), filename);
							// �õ����غ��apk����������
							file = DownLoadUtil.getFile(path,
									file.getAbsolutePath(), pd);
							if (file != null) {
								// �����̷߳�����Ϣ���سɹ�����Ϣ
								Message msg = Message.obtain();
								msg.what = DOWNLOAD_SUCCESS;
								msg.obj = file;
								handler.sendMessage(msg);
							} else {
								// �����̷߳�����Ϣ����ʧ�ܵ���Ϣ
								Message msg = Message.obtain();
								msg.what = DOWNLOAD_ERROR;
								handler.sendMessage(msg);
							}
							pd.dismiss();// ���ؽ����󣬽����صĽ������رյ�
						};
					}.start();
				} else {
					Toast.makeText(getApplicationContext(), "sd��������", 1).show();
					loadMainUI();// �������������
				}
			}
		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				loadMainUI();
			}
		});
		builder.create().show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����Ϊ�ޱ�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����Ϊȫ��ģʽ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_splash);
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("�汾��:" + getVersion());

		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		rl_splash.startAnimation(aa);

		// 1.���ӷ�������ȡ�������ϵ�������Ϣ.
		new Thread(new CheckVersionTask()) {
		}.start();
		//��������������ݿ��ļ�
		new Thread() {
			public void run() {
				File file = new File(getFilesDir(), "antivirus.db");
				if (file.exists() && file.length() > 0) {//���ݿ��ļ��Ѿ������ɹ�

				} else {
					AssetCopyUtil.copy1(getApplicationContext(),
							"antivirus.db", file.getAbsolutePath(), null);
				}
			};

		}.start();
	}

	/**
	 * �������Ӧ�õİ汾���������ϵİ汾���Ƿ���ͬ
	 * 
	 * @author Administrator
	 * 
	 */
	private class CheckVersionTask implements Runnable {

		public void run() {
			// ��ȡSdcard�µ�config.xml�ļ���������ļ������ڣ���ô�����Զ��������ļ�
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			// ��sp��������ȡautoupdate����Ӧ��booleanֵ������ü������ڣ�Ĭ�Ϸ���true
			boolean autoupdate = sp.getBoolean("autoupdate", true);
			// �Զ�����û�п���
			if (!autoupdate) {
				try {
					// ˯��2���ӵ���Ϊ�˲��Ŷ���
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// ˯��2���Ӳ��Ŷ�����Ϻ�������������
				loadMainUI();
			}
			startTime = System.currentTimeMillis();
			Message msg = Message.obtain();
			try {
				// ��ȡ����˵�������Ϣ�����ӵ�ַ
				String serverurl = getResources().getString(R.string.serverurl);
				URL url = new URL(serverurl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");// ��������ʽ
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();// ��ȡ��Ӧ��
				if (code == 200) {// ��Ӧ��Ϊ200ʱ����ʾ���������ӳɹ�
					InputStream is = conn.getInputStream();
					info = UpdateInfoParser.getUpdateInfo(is);
					endTime = System.currentTimeMillis();
					long resulttime = endTime - startTime;
					if (resulttime < 2000) {
						try {
							Thread.sleep(2000 - resulttime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					msg.what = GET_INFO_SUCCESS;
					handler.sendMessage(msg);
				} else {
					// ������״̬����.
					msg.what = SERVER_ERROR;
					handler.sendMessage(msg);
					endTime = System.currentTimeMillis();
					long resulttime = endTime - startTime;
					if (resulttime < 2000) {
						try {
							Thread.sleep(2000 - resulttime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
				msg.what = SERVER_URL_ERROR;
				handler.sendMessage(msg);
			} catch (ProtocolException e) {
				msg.what = PROTOCOL_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (IOException e) {
				msg.what = IO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				msg.what = XML_PARSE_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡ��ǰӦ�ó���İ汾�š� �汾�Ŵ��������ǵ�APK�ж�Ӧ���嵥�ļ��У�ֱ�ӽ�ѹAPK�󣬼��ɿ�����Ӧ���嵥�ļ�����
	 * �汾����manifest�ڵ��е�android:versionName="1.0" ��һ��Ӧ�ó���װ���ֻ���
	 * ����apk�������ֻ���data/appĿ¼�£�Ҳ����ϵͳ�У�����ͼ6��������õ��汾�ţ�������Ҫ�õ���ϵͳ��صķ��񣬾Ϳ��Եõ�apk�е���Ϣ��
	 * 
	 * @return
	 */
	private String getVersion() {
		// �õ�ϵͳ�İ����������Ѿ��õ���apk���������İ�װ
		PackageManager pm = this.getPackageManager();
		try {
			// ����һ����ǰӦ�ó���İ��� ����������ѡ�ĸ�����Ϣ�����������ò��� �����Զ���Ϊ0
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			// ���ص�ǰӦ�ó���İ汾��
			return info.versionName;
		} catch (Exception e) {// ����δ�ҵ����쳣�������ϣ� ���쳣�����ܻᷢ��
			e.printStackTrace();
			return "";
		}
	}
}
