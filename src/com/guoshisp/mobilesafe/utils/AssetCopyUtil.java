package com.guoshisp.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * �ʲ��ļ������Ĺ�����
 * 
 * @author
 * 
 */
public class AssetCopyUtil {
	private Context context;

	public AssetCopyUtil(Context context) {
		this.context = context;
	}

	/**
	 * �����ʲ�Ŀ¼�µ��ļ�
	 * 
	 * @param srcfilename
	 *            Դ�ļ�������
	 * @param file
	 *            Ŀ���ļ��Ķ���
	 * @param pd
	 *            �������Ի���
	 * @return �Ƿ񿽱��ɹ�
	 */
	public boolean copyFile(String srcfilename, File file, ProgressDialog pd) {
		try {
			// ��ȡ���ʲ�Ŀ¼�Ĺ���������Ϊ���ݿ����ڸ�Ŀ¼��
			AssetManager am = context.getAssets();
			// ���ʲ�Ŀ¼�µ���Դ�ļ�����ȡһ������������
			InputStream is = am.open(srcfilename);
			// ��ȡ�����ļ����ֽ���
			int max = is.available();
			// ���ý�������ʾ��������
			pd.setMax(max);
			// ����һ��������ļ������ڽ���������
			FileOutputStream fos = new FileOutputStream(file);
			// ����һ��������
			byte[] buffer = new byte[1024];
			int len = 0;
			// ���������ʼ��λ��Ӧ��Ϊ0
			int process = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				// �ý��������ϵĶ�̬��ʾ��ǰ�Ŀ�������
				process += len;
				pd.setProgress(process);
			}
			// ˢ�»�����������
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * ���ʲ�Ŀ¼�����ļ�
	 * 
	 * @param context
	 * @param filename
	 *            �ʲ�Ŀ¼���ļ�������
	 * @param destfilename
	 *            Ŀ���ļ���·��
	 * @return
	 */
	public static File copy1(Context context, String filename,
			String destfilename, ProgressDialog pd) {

		try {
			InputStream in = context.getAssets().open(filename);
			int max = in.available();
			if (pd != null) {
				pd.setMax(max);
			}

			File file = new File(destfilename);
			OutputStream out = new FileOutputStream(file);
			byte[] byt = new byte[1024];
			int len = 0;
			int total = 0;
			while ((len = in.read(byt)) != -1) {
				out.write(byt, 0, len);
				total += len;
				if (pd != null) {
					pd.setProgress(total);
				}
			}
			out.flush();
			out.close();
			in.close();

			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
