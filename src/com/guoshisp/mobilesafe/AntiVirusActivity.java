package com.guoshisp.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.db.dao.AntiVirusDao;
import com.guoshisp.mobilesafe.utils.Md5Encoder;

public class AntiVirusActivity extends Activity {
	protected static final int SCAN_NOT_VIRUS = 90;
	protected static final int FIND_VIRUS = 91;
	protected static final int SCAN_FINISH = 92;
	// ��ɱ����ʱ���״��ϵ�ɨ��ָ��
	private ImageView iv_scan;
	// Ӧ�ó����������
	private PackageManager pm;
	// �������ݿ�Ķ���
	private AntiVirusDao dao;
	// ɨ�������
	private ProgressBar progressBar1;
	// ��ʾ���ֵĲ�����Ŀ
	private TextView tv_scan_status;
	// ��ʾɨ��ĳ�����Ϣ
	private LinearLayout ll_scan_status;
	// �������ɨ�赽�Ĳ�����Ϣ
	private List<PackageInfo> virusPackInfos;
	// ��ת����
	RotateAnimation ra;
	// ��Ų����ļ���
	private Map<String, String> virusMap;
	// ���������߳�ͨ�ţ��������̣߳�UI�̣߳�
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			PackageInfo info = (PackageInfo) msg.obj;
			switch (msg.what) {
			case SCAN_NOT_VIRUS:// δ���ֲ���
				TextView tv = new TextView(getApplicationContext());
				tv.setText("ɨ��" + info.applicationInfo.loadLabel(pm) + " ��ȫ");
				ll_scan_status.addView(tv, 0);// ��ӵ�ll_scan_info�ؼ���������
				break;
			case FIND_VIRUS:// ���ֲ���
				// ��������ӵ�������
				virusPackInfos.add(info);
				break;
			case SCAN_FINISH:// ɨ�����
				// ֹͣ�����Ĳ���
				iv_scan.clearAnimation();
				// �жϲ������ϵĴ�С
				if (virusPackInfos.size() == 0) {
					Toast.makeText(getApplicationContext(), "ɨ�����,����ֻ��ܰ�ȫ", 0)
							.show();
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.anti_virus);
		pm = getPackageManager();
		dao = new AntiVirusDao(this);
		virusPackInfos = new ArrayList<PackageInfo>();
		super.onCreate(savedInstanceState);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status2);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		// ����һ����ת�Ķ���
		ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		ra.setDuration(1000);
		// ������ת���ظ�������һֱ��ת��
		ra.setRepeatCount(Animation.INFINITE);
		// ������ת��ģʽ����תһ���غϺ�������ת��
		ra.setRepeatMode(Animation.RESTART);
		ll_scan_status = (LinearLayout) findViewById(R.id.ll_scan_status);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
	}

	public void kill(View v) {
		// ���ö���
		ra.reset();
		// ��������
		iv_scan.startAnimation(ra);
		// ����һ�����̣߳������ֻ��и���Ӧ�õ�ǩ����Ϣ
		new Thread() {
			public void run() {
				// PackageManager.GET_SIGNATURESӦ�ó����ǩ����Ϣ
				List<PackageInfo> packinfos = pm
						.getInstalledPackages(PackageManager.GET_SIGNATURES);
				progressBar1.setMax(packinfos.size());
				// ������ǰ�Ѿ������˶�����Ӧ�ó�������ʾ��ɱ�Ľ���
				int count = 0;
				// ����������Ӧ�ó����Ӧ��ǩ����Ϣ
				for (PackageInfo info : packinfos) {
					// ��Ӧ�ó����ǩ����Ϣת��MD5ֵ�������벡�����ݿ�ȶ�
					String md5 = Md5Encoder.encode(info.signatures[0]
							.toCharsString());
					// �ڲ������ݿ��в��Ҹ�MD5ֵ�����жϸ�Ӧ�ó����Ƿ����ݲ���
					String result = dao.getVirusInfo(md5);
					// ������ҵĽ��Ϊnull�����ʾ��ǰ������Ӧ�ò��ǲ���
					if (result == null) {
						Message msg = Message.obtain();
						msg.what = SCAN_NOT_VIRUS;
						msg.obj = info;
						handler.sendMessage(msg);
					} else {//��ǰ��������Ӧ�����ڲ���
						Message msg = Message.obtain();
						msg.what = FIND_VIRUS;
						msg.obj = info;
						handler.sendMessage(msg);
					}
					count++;
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progressBar1.setProgress(count);
				}
				// ��������
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;
				handler.sendMessage(msg);

			};
		}.start();

	}

	// "һ������"��ť
	public void clean(View v) {
		// �жϲ������ϵĴ�С
		if (virusPackInfos.size() > 0) {
			for (PackageInfo pinfo : virusPackInfos) {
				// ж��Ӧ�ó���
				String packname = pinfo.packageName;
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DEFAULT);
				intent.setData(Uri.parse("package:" + packname));
				startActivity(intent);
			}
		}else{
			return;
		}
	}
}