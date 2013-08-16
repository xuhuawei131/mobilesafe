package com.guoshisp.mobilesafe.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

/**
 * ���صĹ����� 1.�����ļ���·�� 2.�����ļ��󱣴��·�� 3.���� ������ 4.������
 */
public class DownLoadUtil {

	/**
	 * ����һ���ļ�
	 * 
	 * @param urlpath
	 *            ·��
	 * @param filepath
	 *            ���浽���ص��ļ�·��
	 * @param pd
	 *            �������Ի���
	 * @return	  ���غ��apk
	 */
	public static File getFile(String urlpath, String filepath,
			ProgressDialog pd) {
		try {
			URL url = new URL(urlpath);
			File file = new File(filepath);
			FileOutputStream fos = new FileOutputStream(file);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//���ص�������GET��ʽ��conn��Ĭ�Ϸ�ʽҲ��GET����
			conn.setRequestMethod("GET");
			//����˵���Ӧ��ʱ��
			conn.setConnectTimeout(5000);
			//��ȡ������˵��ļ����ܳ���
			int max = conn.getContentLength();
			//�������������ֵ����ΪҪ���ص��ļ����ܳ���
			pd.setMax(max);
			//��ȡ��Ҫ���ص�apk���ļ���������
			InputStream is = conn.getInputStream();
			//����һ��������
			byte[] buffer = new byte[1024];
			int len = 0;
			int process = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				//ÿ��ȡһ������������ˢ��һ�����ؽ���
				process+=len;
				pd.setProgress(process);
				//����˯��ʱ�䣬�������ǹ۲����ؽ���
				Thread.sleep(30);
			}
			//ˢ�»������ݵ��ļ���
			fos.flush();
			//����
			fos.close();
			is.close();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡһ��·���е��ļ��������磺mobilesafe.apk
	 * 
	 * @param urlpath
	 * @return
	 */
	public static String getFilename(String urlpath) {
		return urlpath
				.substring(urlpath.lastIndexOf("/") + 1, urlpath.length());
	}
}
