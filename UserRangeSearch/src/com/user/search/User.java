package com.user.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.PBRQ.tree.KeyInfo;

public class User {

	private Point userlocation;
	private int len;
	private KeyInfo keyInfo;
	
	public User() {
		this.userlocation = new Point();
		this.userlocation.setX(0);
		this.userlocation.setY(0);
		this.len=20;
		this.keyInfo = new KeyInfo();
		if (judeFileExists(keyInfo.getAllKeyPath())) {
			this.keyInfo.Read(this.keyInfo.getAllKeyPath());
			System.out.println("���سɹ�����Կ��Ϣ���£�"+this.keyInfo.toString());
		}
	}
	public User(Point userlocation, int len,KeyInfo key) {
		super();
		this.userlocation = userlocation;
		this.len = len;
		this.keyInfo=key;
	}
	public User(double x,double y) {
		this.userlocation = new Point(x,y);
		this.len=20;
		this.keyInfo = new KeyInfo();
	}
	
	public Point getUserlocation() {
		return userlocation;
	}
	public void setUserlocation(Point userlocation) {
		this.userlocation = userlocation;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public KeyInfo getKeyInfo() {
		return keyInfo;
	}
	public void setKeyInfo(KeyInfo keyInfo) {
		this.keyInfo = keyInfo;
	}
	/*
	 * ����ά(x,y)����ת�����ַ�����������ͨ�����꾫������ֹѭ��
	 * ���е�һ������11���ڶ�������01������������00������������10
	 * ��γ�ȷ�Χ minX=-180;maxX=180;minY=-90;maxY=90;
	 */

	public String PositionEncodeByAccuracy() {
		double x = userlocation.getX();
		double y = userlocation.getY();
		double error = 0.001;
		double w =180;
		double h = 90;
		Point parent = new Point(0.0, 0.0);
		String out = "";
		while (error>0.0001) {
			int d1 = (x-parent.getX())>0?1:0;
			int d2 = Math.sin((y-parent.getY())/(x-parent.getX()))>0?1:0;
			out = out + String.valueOf(d1)+ String.valueOf(d2);
			if (d1>0) {//��һ������
				parent.setX(parent.getX() + w/2);
			}else {//�ڶ�������
				parent.setX(parent.getX() - w/2);
			}
			w= w/2;
			if (d2>0) {//��һ����
				parent.setY(parent.getY()+ h/2);
			}else {//��������
				parent.setY(parent.getY() - h/2);
			}
			h=h/2;
			error = Math.abs(parent.getX() - x);
		}
		return out;
	}
	/*
	 * ͨ���̶����������ƾ��ȣ���������Ϊ20��
	 * ������ת�����ַ����ĺ�������x,y�Ķ��֣�ͨ��ת���Ĳ�����ȷ�����ɵĶ������ַ�������
	 * �ô��ǣ��������ַ������ǹ̶����ȵġ������ǣ����������������ַ������ȣ�
	 * ���ҺͲ��루������Ҷ�ӽڵ㻹Ҫ���ѵĻ�����ʱ��Ϳ��ܻᱨ��
	 * ��γ�ȷ�Χ minX=-180;maxX=180;minY=-90;maxY=90;
	 */
	public  String PositionEncodeByDeep(double x,double y) {
		double deep = this.len;//�����ַ�����ϸ������
		double w =180;
		double h = 90;
		Point parent = new Point(0.0, 0.0);
		String out = "";
		while (deep>0) {
			int d1 = (x-parent.getX())>0?1:0;
			int d2 = Math.sin((y-parent.getY())/(x-parent.getX()))>0?1:0;
			out = out + String.valueOf(d1)+ String.valueOf(d2);
			if (d1>0) {//��һ������
				parent.setX(parent.getX() + w/2);
			}else {//�ڶ�������
				parent.setX(parent.getX() - w/2);
			}
			w= w/2;
			if (d2>0) {//��һ����
				parent.setY(parent.getY()+ h/2);
			}else {//��������
				parent.setY(parent.getY() - h/2);
			}
			h=h/2;
			deep--;
		}
		return out;
	}
	
	public  String PositionEncodeByDeep(Point tmp) {
		return PositionEncodeByDeep(tmp.getX(),tmp.getY());
	}
	
	/*
	 * �Ѹ����ַ���
	 */
	public Point DeepPointDecoding(String ss) {
		Point result = null;
		double w =90;
		double h = 45;
		double x=0;
		double y=0;
		for (int i=0;i<ss.length();i+=2)
		{
			if (ss.charAt(i)=='0') {
				x-=w;
				w/=2;
			}else {
				x+=w;
				w/=2;
			}
			if (ss.charAt(i+1)=='0') {
				y-=h;
				h/=2;
			}else {
				y+=h;
				h/=2;
			}
		}
		result = new Point(x, y);
		return result;
	}
	


	// ����ʱ, ֻҪ readFile("F:\\proper\\treeKey.dat");
		public String readFile(String path) throws IOException {
			File file = new File(path);
			if (!file.exists() || file.isDirectory())
				throw new FileNotFoundException();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp = null;
			StringBuffer sb = new StringBuffer();
			temp = br.readLine();
			while (temp != null) {
				sb.append(temp + " ");
				temp = br.readLine();
			}
			return sb.toString();
		}
		
	public Boolean judeFileExists(String filename) {
		File file = new File(filename);
		if (file.exists()) {
			System.out.println("file exists");
			return true;
		}
		return false;
	}
}
