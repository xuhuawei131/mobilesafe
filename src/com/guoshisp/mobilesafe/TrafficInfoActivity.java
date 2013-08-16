package com.guoshisp.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guoshisp.mobilesafe.domain.TrafficInfo;
import com.guoshisp.mobilesafe.engine.TrafficInfoProvider;

public class TrafficInfoActivity extends Activity {
	//չʾ�����б�
	private ListView lv;
	//��ȡ�����о���IntenetȨ�޵�Ӧ�õ�������Ϣ
	private TrafficInfoProvider provider;
	//ProgressBar��TextView�����ڼ���...���ĸ��ؼ������ڿ�������ʾ
	private LinearLayout ll_loading;
	//��װ��������IntenetȨ�޵�Ӧ�õ�������Ϣ
	private List<TrafficInfo>  trafficInfos;
	//�������̷߳��͹�������Ϣ������UI
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			lv.setAdapter(new TrafficAdapter());
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.traffic_info);
		super.onCreate(savedInstanceState);
		lv = (ListView) findViewById(R.id.lv_traffic_manager);
		provider = new TrafficInfoProvider(this);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		ll_loading.setVisibility(View.VISIBLE);
		//��ȡ������InternetȨ�޵�Ӧ��������������
		new Thread(){
			public void run() {
				trafficInfos = provider.getTrafficInfos();
				//�����߳��з���һ������Ϣ������֪ͨ���̸߳�������
				handler.sendEmptyMessage(0);
			};
		}.start();
	}
	//����������
	private class TrafficAdapter extends BaseAdapter{

		public int getCount() {
			return trafficInfos.size();
		}

		public Object getItem(int position) {
			return trafficInfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
		//ListView����ʾ���ٸ�Item���÷����ͱ����ö��ٴ�
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder = new ViewHolder();
			TrafficInfo info = trafficInfos.get(position);
			//���û����View
			if(convertView==null){
				view = View.inflate(getApplicationContext(), R.layout.traffic_item, null);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_traffic_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_traffic_name);
				holder.tv_rx = (TextView) view.findViewById(R.id.tv_traffic_rx);
				holder.tv_tx = (TextView) view.findViewById(R.id.tv_traffic_tx);
				holder.tv_total = (TextView) view.findViewById(R.id.tv_traffic_total);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_name.setText(info.getAppname());
			//����������������
			long rx = info.getRx();
			//�ϴ�������������
			long tx = info.getTx();
			//��ǿ����Ľ�׳�ԡ���Ϊ��ģ����������ʱ����ֵΪ-1.
			if(rx<0){
				rx = 0;
			}
			if(tx<0){
				tx = 0;
			}
			holder.tv_rx.setText(Formatter.formatFileSize(getApplicationContext(), rx));
			holder.tv_tx.setText(Formatter.formatFileSize(getApplicationContext(), tx));
			//������
			long total = rx + tx;
			//ͨ��Formatter��long���͵�����ת��ΪMB����KB�������ֽ�Сʱ���Զ�����KB
			holder.tv_total.setText(Formatter.formatFileSize(getApplicationContext(), total));
			return view;
		}
	}
	//ͨ��static�����Σ���֤��ջ�ڴ��д���Ψһһ���ֽ����ұ�����
	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_tx;
		TextView tv_rx;
		TextView tv_total;
	}
}
