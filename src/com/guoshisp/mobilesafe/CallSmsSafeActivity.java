package com.guoshisp.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guoshisp.mobilesafe.db.dao.BlackNumberDao;
import com.guoshisp.mobilesafe.domain.BlackNumber;

public class CallSmsSafeActivity extends Activity {
	protected static final int LOAD_DATA_FINISH = 40;
	public static final String TAG = "CallSmsSafeActivity";
	//����չ�ֳ����еĺ���������
	private ListView lv_call_sms_safe;
	//�����������������ݿ�Ķ���
	private BlackNumberDao dao;
	//����������������ݿ���һ����ȡ�����뻺�漯���У���������������Ƶ���Ĳ������ݿ⣩
	private List<BlackNumber> blacknumbers;
	//��ʾ���������������������
	private BlackNumberAdapter adpater;
	//ProgressBar�ؼ��ĸ��ؼ������ڿ����ӿؼ�����ʾ��������ProgressBar��
	private LinearLayout ll_call_sms_safe_loading;
	
	//private String initnumber;
	//���ڽ������̷߳��͹�������Ϣ��ʵ��UI�ĸ���
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_DATA_FINISH://�����ݿ��м��غ������������
				//���������������ڼ�������...������
				ll_call_sms_safe_loading.setVisibility(View.INVISIBLE);
				//Ϊlv_call_sms_safe����������
				adpater = new BlackNumberAdapter();
				lv_call_sms_safe.setAdapter(adpater);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_safe);
		ll_call_sms_safe_loading = (LinearLayout) findViewById(R.id.ll_call_sms_safe_loading);
		dao = new BlackNumberDao(this);
		lv_call_sms_safe = (ListView) findViewById(R.id.lv_call_sms_safe);
		//ll_call_sms_safe_loading�ؼ��е������ӿؼ�����Ϊ�ɼ���ProgressBar�͡����ڼ�������...����
		ll_call_sms_safe_loading.setVisibility(View.VISIBLE);
				
		/*Intent intent  = getIntent();//��ȡ���ǰ�����intent;
		initnumber = intent.getStringExtra("blacknumber");
		Log.i(TAG,"initnumber:"+initnumber);
		if(initnumber!=null){
			showBlackNumberDialog(0, 0);
		}*/

		// 1.Ϊlv_call_sms_safeע��һ�������Ĳ˵�
		registerForContextMenu(lv_call_sms_safe);
		//һ���Ի�ȡ���ݿ��е��������ݵĲ�����һ���ȽϺ�ʱ�Ĳ��������������߳������
		new Thread() {
			public void run() {
				blacknumbers = dao.findAll();
				//֪ͨ���̸߳��½���
				Message msg = Message.obtain();
				msg.what = LOAD_DATA_FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/*@Override
	protected void onNewIntent(Intent intent) {
		initnumber = intent.getStringExtra("blacknumber");
		Log.i(TAG,"initnumber:"+initnumber);
		if(initnumber!=null){
			showBlackNumberDialog(0, 0);
		}
		super.onNewIntent(intent);
	}*/
	
	// 2.��д���������Ĳ˵��ķ���
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//���ó���Item��Ҫ��ʾ�Ĳ���
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.call_sms_safe_menu, menu);
	}

	// 3.��Ӧ�����Ĳ˵��ĵ���¼�
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//��ȡ��Item��Ӧ�Ķ���
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = (int) info.id; // ��ǰ�����Ĳ˵���Ӧ��listview�������һ����Ŀ
		switch (item.getItemId()) {
		case R.id.item_delete:
			Log.i(TAG, "ɾ����������¼");
			deleteBlackNumber(position);
			return true;
		case R.id.item_update:
			Log.i(TAG, "���º�������¼");
			updateBlackNumber(position);

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * ���º���������
	 * 
	 * @param position
	 */
	private void updateBlackNumber(int position) {
		showBlackNumberDialog(1, position);
	}

	/**
	 * ɾ��һ����������¼
	 * 
	 * @param position
	 */
	private void deleteBlackNumber(int position) {
		BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
				.getItemAtPosition(position);
		String number = blackNumber.getNumber();
		dao.delete(number); // ɾ���� ���ݿ�����ļ�¼
		blacknumbers.remove(blackNumber);// ɾ����ǰlistview���������.
		adpater.notifyDataSetChanged();
	}
	/**
	 * Ϊ�����������е�lv_call_sms_safe�е�Item��������
	 * @author Administrator
	 *
	 */
	private class BlackNumberAdapter extends BaseAdapter {
		//��ȡItem����Ŀ
		public int getCount() {
			return blacknumbers.size();
		}
		//��ȡItem�Ķ���
		public Object getItem(int position) {
			return blacknumbers.get(position);
		}
		//��ȡItem��Ӧ��id
		public long getItemId(int position) {
			return position;
		}

		//����Ļ�ϣ�ÿ��ʾһ��Item�͵���һ�θ÷���
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			//������ʷ�����View����
			if (convertView == null) {
				Log.i(TAG, "�����µ�view����");
				//��Itemת��View����
				view = View.inflate(getApplicationContext(),
						R.layout.call_sms_item, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_callsms_item_number);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_callsms_item_mode);
				view.setTag(holder);// �ѿؼ�id������ �����view��������
			} else {
				view = convertView;
				Log.i(TAG, "ʹ����ʷ�����view����");
				holder = (ViewHolder) view.getTag();
			}
			//ΪItem��������ģʽ
			BlackNumber blacknumber = blacknumbers.get(position);
			holder.tv_number.setText(blacknumber.getNumber());
			int mode = blacknumber.getMode();
			if (mode == 0) {
				holder.tv_mode.setText("�绰����");
			} else if (mode == 1) {
				holder.tv_mode.setText("��������");
			} else {
				holder.tv_mode.setText("ȫ������");
			}
			return view;
		}
	}
	//��Item�еĿؼ�ʹ��static���Σ���static���ε�����ֽ�����JVM��ֻ�����һ�ݡ�tv_number��tv_mode��ջ��Ҳ��ֻ����һ��
	private static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
	}

	/**
	 * Ϊ����Ӻ��������롱ע��ĵ���¼�
	 * ���һ������������
	 * @param view
	 */
	public void addBlackNumber(View view) {
		showBlackNumberDialog(0, 0);
	}

	/**
	 * ��ʾ��Ӻ�����ʱ����ӶԻ�������޸ĶԻ������߹���ͬһ���Ի���
	 * 
	 * @param flag
	 *            0 ������ӣ� 1 �����޸�
	 * @param position
	 *            ���޸ĵ�Item�ڴ����е�λ�á������� ���ݣ���ӵ����ݿ���Ϊ��
	 */
	private void showBlackNumberDialog(final int flag, final int position) {
		//���һ�����幹����
		AlertDialog.Builder builder = new Builder(this);
		//����Ӻ���Ĳ����ļ�ת����һ��View
		View dialogview = View.inflate(this, R.layout.add_black_number, null);
		//��ȡ��������������EditText
		final EditText et_number = (EditText) dialogview
				.findViewById(R.id.et_add_black_number);
		/*if(!TextUtils.isEmpty(initnumber)){
			et_number.setText(initnumber);
		}*/
		//��ȡ�������ĶԻ����еĸ������
		final CheckBox cb_phone = (CheckBox) dialogview
				.findViewById(R.id.cb_block_phone);
		final CheckBox cb_sms = (CheckBox) dialogview
				.findViewById(R.id.cb_block_sms);
		TextView tv_title = (TextView) dialogview
				.findViewById(R.id.tv_black_number_title);
		if (flag == 1) {//�޸ĺ���������
			tv_title.setText("�޸�");
			//��Ҫ�޸ĵĺ�����������Ե������������
			BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
					.getItemAtPosition(position);
			String oldnumber = blackNumber.getNumber();
			et_number.setText(oldnumber);
			int m = blackNumber.getMode();
			//ͨ������ģʽ��ָ��Checkbox�Ĺ�ѡ״̬
			if(m==0){//�绰����
				cb_phone.setChecked(true);
				cb_sms.setChecked(false);
			}else if(m==1){//��������
				cb_sms.setChecked(true);
				cb_phone.setChecked(false);
			}else{//�绰���������
				cb_phone.setChecked(true);
				cb_sms.setChecked(true);
			}
		}
		//��ת���Ĳ����ļ���ӵ�������
		builder.setView(dialogview);
		//����Ի����еġ�ȷ������ť
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//��ȡ������ĺ��룬��������ǰ��Ŀո������
				String number = et_number.getText().toString().trim();
				//flag=1��������޸ģ�������ĵ�ʱ�� ������ĳ�����ͬ�ĵ绰����.
				if(flag==1&&dao.find(number)){
					Toast.makeText(getApplicationContext(), "Ҫ�޸ĵĵ绰�����Ѿ�����",0).show();
					return ;
				}
				//���������ǿգ���ֱ�ӽ�����ǰ����
				if (TextUtils.isEmpty(number)) {
					return;
				} else {//����ĺ��벻Ϊ��
					// ��ӽ���������ӳɹ� ����Ҫ֪ͨ������º��������ݡ�Ĭ�ϵ������ʧ��
					boolean result = false;
					BlackNumber blacknumber = new BlackNumber();
					blacknumber.setNumber(number);
					//�绰���ؿ�Ͷ������ؿ񶼱�ѡ�еĻ�������ģʽӦ��Ϊ2
					if (cb_phone.isChecked() && cb_sms.isChecked()) {
						if (flag == 0) {//flag=1��ʾ����Ӻ���������
							result = dao.add(number, "2");
							blacknumber.setMode(2);
						} else {//�޸ĺ���������
							//��ȡ��Ҫ�޸ĵ�Item����
							BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
									.getItemAtPosition(position);
							//�������ݿ���Ҫ�޸ĵ���������
							dao.update(blackNumber.getNumber(), number, "2");
							blackNumber.setMode(2);
							blackNumber.setNumber(number);
							//֪ͨ������������ʾ���ݣ���ʱ�������ϵ����ݱ�ˢ�£�
							adpater.notifyDataSetChanged();
						}
					} else if (cb_phone.isChecked()) {//�绰���أ�����ģʽΪ0
						if (flag == 0) {//��Ӻ���������
							result = dao.add(number, "0");
							blacknumber.setMode(0);
						} else {//�޸ĺ���������
							//��ȡ��Ҫ�޸ĵ�Item����
							BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
									.getItemAtPosition(position);
							//�������ݿ���Ҫ�޸ĵ���������
							dao.update(blackNumber.getNumber(), number, "0");
							blackNumber.setMode(0);
							blackNumber.setNumber(number);
							//֪ͨ������������ʾ���ݣ���ʱ�������ϵ����ݱ�ˢ�£�
							adpater.notifyDataSetChanged();

						}
					} else if (cb_sms.isChecked()) {//����ģʽΪ�������أ���Ӧ������Ϊ1��
						if (flag == 0) {//��Ӻ���������
							result = dao.add(number, "1");
							blacknumber.setMode(1);
						}else{//�޸ĺ���������
							//��ȡ��Ҫ�޸ĵ�Item����
							BlackNumber blackNumber = (BlackNumber) lv_call_sms_safe
									.getItemAtPosition(position);
							//�������ݿ���Ҫ�޸ĵ���������
							dao.update(blackNumber.getNumber(), number, "1");
							blackNumber.setMode(1);
							blackNumber.setNumber(number);
							//֪ͨ������������ʾ���ݣ���ʱ�������ϵ����ݱ�ˢ�£�
							adpater.notifyDataSetChanged();
						}
					} else {//û��ѡ���κ�����ģʽ
						Toast.makeText(getApplicationContext(), "����ģʽ����Ϊ��", 0)
								.show();
						return;
					}
					if (result) {//��ӻ��޸����ݳɹ�����ʱ��Ҫ���½����б��е�����
						//������ӵ�������ӵ������У���Ϊ�������ǴӼ�����ȡ���ݵ�
						blacknumbers.add(blacknumber);
						//֪ͨ������������ʾ���ݣ���ʱ�������ϵ����ݱ�ˢ�£�
						adpater.notifyDataSetChanged();
					}
				}
			}
		});
		//����Ի����еġ�ȡ����ť����Ӧ�ĵ���¼�
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		//��������ʾ������Ի���
		builder.create().show();
	}
}
