package com.PBRQ.tree;

import java.io.Serializable;

import com.Encry.PBRQ.tree.EncryData;

public class Data implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1766526903716039520L;
	private Entity entity;
	private String str;//存储entity中的（x,y）,坐标转换成字符串后的字符串
	private int len=20;//（x,y）转换成字符串后的字符串长度
	
	public Data() {
		this.entity = new Entity();
		this.str="00000000000000000000";
	}
	public Data(Entity entity) {
		this.entity = entity;
		this.str = PositionEncodeByDeep(this.entity.getX(),this.entity.getY());
	}
	public Data(String ss,String value) {
		this.str=ss;
		Point tmp = DeepPointDecoding(ss);
		this.entity = new Entity(tmp.getX(),tmp.getY(),value);
	}
	public Data(double x,double y,String value) {
		this.entity = new Entity(x,y,value);
		this.str = PositionEncodeByDeep(this.entity.getX(),this.entity.getY());
		
	}

	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	
	/*
	 * 将二维(x,y)坐标转换成字符串，方法是通过坐标精度来终止循环
	 * 其中第一象限是11，第二象限是01，第三象限是00，第四象限是10
	 * 经纬度范围 minX=-180;maxX=180;minY=-90;maxY=90;
	 */
	public String PositionEncodeByAccuracy() {
		double x = entity.getX();
		double y = entity.getY();
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
	
}