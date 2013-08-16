package com.guoshisp.mobilesafe.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Md5Encoder {
	public static String encode(String password){
		try {
			//��ȡ��������Ϣ��ժҪ��
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//ִ�м��ܲ���
			byte[] result = digest.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			//��ÿ��byte�ֽڵ�����ת����16���Ƶ�����
			for(int i= 0 ;i<result.length;i++){
				int number = result[i]&0xff;//����
				String str = Integer.toHexString(number);//��ʮ���Ƶ�numberת����ʮ����������
				if(str.length()==1){//�жϼ��ܺ���ַ��ĳ��ȣ��������Ϊ1�����ڸ��ַ�ǰ�油0
					sb.append("0");
					sb.append(str);
				}else{
					sb.append(str);
				}
			}
			return sb.toString();//�����ܺ���ַ�ת���ַ�������
		} catch (NoSuchAlgorithmException e) {//������û�б��ҵ������쳣�����ܷ�������Ϊ��������ġ�MD5������ȷ��
			e.printStackTrace();
			//CNA'T REACH;
			return "";
		}
	}
}
