package com.guoshisp.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guoshisp.mobilesafe.db.dao.AppLockDao;
import com.guoshisp.mobilesafe.domain.AppInfo;
import com.guoshisp.mobilesafe.engine.AppInfoProvider;

public class AppLockActivity extends Activity {
	//չʾ�ֻ��е�����Ӧ��
	private ListView lv_applock;
	//ProgressBar��TextView��Ӧ�ĸ��ؼ������ڿ���ProgressBar��TextView����ʾ
	private LinearLayout ll_loading;
	//��ȡ�ֻ����Ѱ�װ��Ӧ�ó���
	private AppInfoProvider provider;
	//��ŵ�ǰ�ֻ�������Ӧ�ó������Ϣ
	private List<AppInfo> appinfos;
	//���������������Ӧ�ó�������ݿ�
	private AppLockDao dao;
	//��������Ѿ���������Ӧ�ó���İ�����Ϣ
	private List<String> lockedPacknames;
	//�������߳��л�ȡ���ĵ�ǰ�ֻ�������Ӧ�ó���
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			//ΪListView��������
			lv_applock.setAdapter(new AppLockAdapter());
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.app_lock);
		super.onCreate(savedInstanceState);
		provider = new AppInfoProvider(this);
		lv_applock = (ListView) findViewById(R.id.lv_applock);
		ll_loading = (LinearLayout) findViewById(R.id.ll_applock_loading);
		dao =new AppLockDao(this);
		//�����ݿ��л�ȡ�����б�������Ӧ�ó������
		lockedPacknames = dao.findAll();
		//���ڴ����ݿ��л�ȡ����ʱ��Ӧ����ʾProgressBar��TextView��Ӧ�ġ����ڼ���...������
		ll_loading.setVisibility(View.VISIBLE);
		//����һ�����̻߳�ȡ�ֻ�������Ӧ�ó������Ϣ
		new Thread(){
			public void run() {
				appinfos = provider.getInstalledApps();
				//�����߳��з���һ������Ϣ��֪ͨ���̸߳�������
				handler.sendEmptyMessage(0);
			};
		}.start();
		//ΪListView�е�Item���õ���¼��ļ�����
		lv_applock.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//��ȡ��ǰItem�Ķ���
				AppInfo appinfo = (AppInfo) lv_applock.getItemAtPosition(position);
				//��ȡ����ǰItem����İ�����Ϣ
				String packname = appinfo.getPackname();
				//���ҵ�Item��Ӧ�����ؼ���ImageView��
				ImageView iv = (ImageView) view.findViewById(R.id.iv_applock_status);
				//����һ�������ƶ��Ķ���
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
				//���ö������ŵ�ʱ�������룩
				ta.setDuration(200);
				//�жϵ�ǰ��Item�Ƿ�������״̬������ǣ���Ӧ�ý���������Ӧ�ü�����
				if(lockedPacknames.contains(packname)){//����״̬
					//dao.delete(packname);
					//���������ṩ�����۲����ݿ��е����ݱ仯
					Uri uri = Uri.parse("content://com.guoshisp.applock/DELETE");
					getContentResolver().delete(uri, null, new String[]{packname});
					//����
					iv.setImageResource(R.drawable.unlock);
					//����ǰӦ�ó���İ����Ӽ��ϣ����������Ӧ�ó���İ��������Ƴ����Ա�����ˢ��
					lockedPacknames.remove(packname);
				}else{//δ����״̬
					//dao.add(packname);
					Uri uri = Uri.parse("content://com.guoshisp.applock/ADD");
					ContentValues values = new ContentValues();
					values.put("packname", packname);
					getContentResolver().insert(uri, values);
					iv.setImageResource(R.drawable.lock);
					lockedPacknames.add(packname);
				}
				//Ϊ��ǰ��Item���Ŷ���
				view.startAnimation(ta);
			}
		});
		
	}
	//�Զ�������������
	private class AppLockAdapter extends BaseAdapter{

		public int getCount() {
			return appinfos.size();
		}

		public Object getItem(int position) {
			return appinfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			//������ʷ�����View����
			if(convertView==null){
				view = View.inflate(getApplicationContext(),R.layout.app_lock_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView)view.findViewById(R.id.iv_applock_icon);
				holder.iv_status = (ImageView)view.findViewById(R.id.iv_applock_status);
				holder.tv_name = (TextView)view.findViewById(R.id.tv_applock_appname);
				view.setTag(holder);
			}else{//ΪView��һ����ǣ��Ա㸴��
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			//��ȡ����ǰӦ�ó������
			AppInfo appInfo = appinfos.get(position);
			holder.iv_icon.setImageDrawable(appInfo.getAppicon());
			holder.tv_name.setText(appInfo.getAppname());
			//�鿴����ǰ��Item�Ƿ��Ǳ��󶨵�Ӧ�ã��Դ���ΪItem���ö�Ӧ������������δ������
			if(lockedPacknames.contains(appInfo.getPackname())){
				holder.iv_status.setImageResource(R.drawable.lock);
			}else{
				holder.iv_status.setImageResource(R.drawable.unlock);
			}
			return view;
		}
	}
	//View��Ӧ��View����ֻ���ڶ��ڴ��д���һ�ݣ����е�Item�����ø�View
	public static class ViewHolder{
		ImageView iv_icon;
		ImageView iv_status;
		TextView tv_name;
	}
}
