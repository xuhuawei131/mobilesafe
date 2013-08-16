package com.guoshisp.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class DragViewActivity extends Activity {
	protected static final String TAG = "DragViewActivity";
	private ImageView iv_drag_view;// Ҫ�ƶ���View
	private TextView tv_drag_view;// ��ʾ��
	private int windowHeight;// ������Ļ�ĸ߶�
	private int windowWidth;// ������Ļ�Ŀ��
	private SharedPreferences sp;// ���ڴ洢View��λ����Ϣ
	private long firstclicktime;//��¼��˫�����С�ʱ�ĵ�һ�ε��ʱ�䣬��¼��ԭ�������ж��Ƿ�����˫���¼�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_view);
		iv_drag_view = (ImageView) findViewById(R.id.iv_drag_view);
		tv_drag_view = (TextView) findViewById(R.id.tv_drag_view);
		windowHeight = getWindowManager().getDefaultDisplay().getHeight();
		windowWidth = getWindowManager().getDefaultDisplay().getWidth();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// ��ʼ���ϴ��ƶ����View����ʾλ�á�ע��:onCreate������ʼ�������ʱ�����ڵ�һ���׶Σ��ý׶����������ؼ��Ĵ�С��λ��
		RelativeLayout.LayoutParams params = (LayoutParams) iv_drag_view
				.getLayoutParams();
		params.leftMargin = sp.getInt("lastx", 0);// ��ȡ�����ƶ���View�봰����˵�Xֵ
		params.topMargin = sp.getInt("lasty", 0);// ��ȡ�����ƶ���View�봰�嶥�˵�Yֵ
		iv_drag_view.setLayoutParams(params);
		//����View˫�����еĵ���¼�
		iv_drag_view.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "�ұ������.......................");
				//�ж��ǵ�һ�ε�� ���ǵڶ��ε��.
				if (firstclicktime > 0) {// �ڶ��εĵ���¼�����Ϊfirstclicktime��һ����Ա������Ĭ��ֵΪ0
					long secondclickTime = System.currentTimeMillis();
					if (secondclickTime - firstclicktime < 500) {//�趨˫���ķ�ֵΪ0.5��
						Log.i(TAG, "˫����.......................");
						//˫������Ҫ����һ�εĵ��ʱ������Ϊ0���Ա��´ε����
						firstclicktime = 0;
						//�����View�Ŀ��
						int right = iv_drag_view.getRight();
						int left = iv_drag_view.getLeft();
						int iv_width = right - left;//�����View�ĳ���
						//�����View�ڴ���������ʱ��View��˺��봰����߱߿�ľ����View�Ҷ˺��봰���ұ߱߿�ľ���
						int iv_left = windowWidth / 2 - iv_width / 2;
						int iv_right = windowWidth / 2 + iv_width / 2;
						// ��View��ʾ�������������.
						iv_drag_view.layout(iv_left, iv_drag_view.getTop(),
								iv_right, iv_drag_view.getBottom());
						//��View��������ʾ��λ�����ݴ���sp��
						Editor editor = sp.edit();
						int lasty = iv_drag_view.getTop();
						int lastx = iv_drag_view.getLeft();
						editor.putInt("lastx", lastx);
						editor.putInt("lasty", lasty);
						editor.commit();

					}
				}
				firstclicktime = System.currentTimeMillis();
				//����û�����ֲ���������һ��ͣ���ϳ���Ȼ��˫��
				new Thread() {
					public void run() {
						try {
							Thread.sleep(500);
							firstclicktime = 0;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		});
		// ΪViewע��һ���������¼��ļ�����
		iv_drag_view.setOnTouchListener(new OnTouchListener() {
			// ��¼��ʼ�����������
			int startx;// ��¼��ʼʱ��X����
			int starty;// ��¼��ʼʱ��Y����

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// ��ָ��һ�νӴ���Ļ
					Log.i(TAG, "����");
					startx = (int) event.getRawX();// ��ȡ����ָ�������X����
					starty = (int) event.getRawY();// ��ȡ����ָ�������Y����

					break;

				case MotionEvent.ACTION_MOVE:// ��ָ����Ļ���ƶ�
					int x = (int) event.getRawX();// ��ȡ����ǰ��ָ�������X����
					int y = (int) event.getRawY();// ��ȡ����ǰ��ָ�������Y����
					// ��ȡ��ʾ��ĸ߶�
					int tv_height = tv_drag_view.getBottom()
							- tv_drag_view.getTop();
					// �ж�View�Ǵ��ڴ�����Ϸ������·�
					if (y > (windowHeight / 2)) {// ��ָ�ƶ����˴������һ��
						// ����ʾ���ƶ���������ϰ벿�֡��ĸ������ֱ�Ϊ����ʾ����봰������ϡ��ҡ��¶˵ľ��롣
						tv_drag_view.layout(tv_drag_view.getLeft(), 60,
								tv_drag_view.getRight(), 60 + tv_height);
					} else {// ��ָ�ƶ����˴������һ��.
							// ����ʾ���ƶ���������°벿��
						tv_drag_view.layout(tv_drag_view.getLeft(),
								windowHeight - 20 - tv_height,
								tv_drag_view.getRight(), windowHeight - 20);
					}

					int dx = x - startx;// �����View����ĻX�᷽���ϱ��ƶ��ľ���
					int dy = y - starty;// �����View����ĻY�᷽���ϱ��ƶ��ľ���
					// ��������϶���View���봰���ϡ��¡����ҵľ���
					int t = iv_drag_view.getTop();
					int b = iv_drag_view.getBottom();
					int l = iv_drag_view.getLeft();
					int r = iv_drag_view.getRight();
					// ��ȡ���ƶ����View���ڴ����е�λ��
					int newl = l + dx;
					int newt = t + dy;
					int newr = r + dx;
					int newb = b + dy;
					// ͨ�����ƶ��ս�����View�����ֻ���Ļ���ĸ��߿�Ĵ�С���жϣ�������View���Ƴ���Ļ
					if (newl < 0 || newt < 0 || newr > windowWidth
							|| newb > windowHeight) {
						break;
					}
					// ���ƶ����View�ڴ��������µ���ʾ����
					iv_drag_view.layout(newl, newt, newr, newb);
					// ����������ָ��һ�δ�����Ļ��λ�����꣬�Ա��´μ����ƶ�
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					Log.i(TAG, "�ƶ�");
					break;
				case MotionEvent.ACTION_UP:// ��ָ�뿪��Ļ
					Log.i(TAG, "����");
					// ��¼��ǰimageview�ڴ����е�λ�ã����ϽǵĶ��������Ļ�Ŀ�Ⱥ͸߶ȣ�
					Editor editor = sp.edit();
					int lasty = iv_drag_view.getTop();
					int lastx = iv_drag_view.getLeft();
					editor.putInt("lastx", lastx);
					editor.putInt("lasty", lasty);
					editor.commit();
					break;
				}
				// true �����ѵ���ǰ�Ĵ����¼�����ô������ƶ����뿪�¼��ᱻ��Ӧ��
				// false �������ѵ�ǰ�Ĵ����¼�����ô������ƶ����뿪�¼������ᱻ��Ӧ��
				return true;
			}
		});
	}
}