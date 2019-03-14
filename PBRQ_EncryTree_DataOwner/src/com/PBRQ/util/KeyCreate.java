package com.PBRQ.util;

import java.io.Serializable;
import java.util.Random;

public class KeyCreate implements Serializable{
	private static String key;
	
	public KeyCreate() {
		this.key = genRandomNum(16);
	}
	public KeyCreate(int pwd_len) {
		this.key = genRandomNum(pwd_len);
	}
	public static String getKey() {
		return key;
	}
	public static void setKey(String key) {
		KeyCreate.key = key;
	}
	
	/*
	 * ����pwd_len���ȵ�key
	 */
	public static String genRandomNum(int pwd_len) {
		// 35����Ϊ�����Ǵ�0��ʼ�ģ�26����ĸ+10������
		final int maxNum = 36;
		int i; // ���ɵ������
		int count = 0; // ���ɵ�����ĳ���
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// �����������ȡ����ֵ����ֹ���ɸ�����
			i = Math.abs(r.nextInt(maxNum)); // ���ɵ������Ϊ36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

}
