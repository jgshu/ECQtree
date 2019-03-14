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
			System.out.println("加载成功，秘钥信息如下："+this.keyInfo.toString());
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
	 * 将二维(x,y)坐标转换成字符串，方法是通过坐标精度来终止循环
	 * 其中第一象限是11，第二象限是01，第三象限是00，第四象限是10
	 * 经纬度范围 minX=-180;maxX=180;minY=-90;maxY=90;
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
			if (d1>0) {//第一四象限
				parent.setX(parent.getX() + w/2);
			}else {//第二三象限
				parent.setX(parent.getX() - w/2);
			}
			w= w/2;
			if (d2>0) {//第一象限
				parent.setY(parent.getY()+ h/2);
			}else {//第四象限
				parent.setY(parent.getY() - h/2);
			}
			h=h/2;
			error = Math.abs(parent.getX() - x);
		}
		return out;
	}
	/*
	 * 通过固定层数来控制精度，层数设置为20层
	 * 把坐标转换成字符串的函数，对x,y的二分，通过转换的层数来确定生成的二进制字符串长度
	 * 好处是，产生的字符串都是固定长度的。坏处是，树的深度如果超过字符串长度，
	 * 查找和插入（当最后的叶子节点还要分裂的话）的时候就可能会报错
	 * 经纬度范围 minX=-180;maxX=180;minY=-90;maxY=90;
	 */
	public  String PositionEncodeByDeep(double x,double y) {
		double deep = this.len;//设置字符串精细到几层
		double w =180;
		double h = 90;
		Point parent = new Point(0.0, 0.0);
		String out = "";
		while (deep>0) {
			int d1 = (x-parent.getX())>0?1:0;
			int d2 = Math.sin((y-parent.getY())/(x-parent.getX()))>0?1:0;
			out = out + String.valueOf(d1)+ String.valueOf(d2);
			if (d1>0) {//第一四象限
				parent.setX(parent.getX() + w/2);
			}else {//第二三象限
				parent.setX(parent.getX() - w/2);
			}
			w= w/2;
			if (d2>0) {//第一象限
				parent.setY(parent.getY()+ h/2);
			}else {//第四象限
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
	 * 把给定字符串
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
	


	// 调用时, 只要 readFile("F:\\proper\\treeKey.dat");
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
