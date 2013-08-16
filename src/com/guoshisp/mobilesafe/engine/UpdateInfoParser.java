package com.guoshisp.mobilesafe.engine;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.guoshisp.mobilesafe.domain.UpdateInfo;

/**
 * 
 * ����XML����
 *
 */
public class UpdateInfoParser {
	/**
	 * @param is xml�ļ���������
	 * @return updateinfo�Ķ���
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static UpdateInfo getUpdateInfo(InputStream is)
			throws XmlPullParserException, IOException {
		//���һ��Pull������ʵ��
		XmlPullParser parser = Xml.newPullParser();
		//��Ҫ�������ļ�������
		parser.setInput(is, "UTF-8");
		//����UpdateInfoʵ�������ڴ�Ž����õ���xml�е����ݣ����ս��ö��󷵻�
		UpdateInfo info = new UpdateInfo();
		//��ȡ��ǰ�������¼�����
		int type = parser.getEventType();
		//ʹ��whileѭ���������õ��¼������ĵ������Ļ�����ô�ͽ�������
		while (type != XmlPullParser.END_DOCUMENT) {
			if (type == XmlPullParser.START_TAG) {//��ʼԪ��
				if ("version".equals(parser.getName())) {//�жϵ�ǰԪ���Ƿ��Ƕ�����Ҫ������Ԫ�أ���ͬ
					//��Ϊ����Ҳ�൱��һ���ڵ㣬���Ի�ȡ����ʱ��Ҫ����parser�����nextText()�����ſ��Եõ�����
					String version = parser.nextText();
					info.setVersion(version);
				} else if ("description".equals(parser.getName())) {
					String description = parser.nextText();
					info.setDescription(description);
				} else if ("apkurl".equals(parser.getName())) {
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
			}
			type = parser.next();
		}
		return info;
	}
}
