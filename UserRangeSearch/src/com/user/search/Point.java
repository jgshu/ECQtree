package com.user.search;

import java.io.Serializable;


/*
 * 用Point来代替Entity只记录坐标来便于使用
 */
public class Point implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 667559364693993403L;
	private double x;
	private double y;
	public Point() {
		super();
		this.x = 0;
		this.y = 0;
	}
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	

	public String PositionEncodeByAccuracy() {
		double x = this.getX();
		double y = this.getY();
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
		double deep = 20;//设置字符串精细到几层
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
	 * 把给定字符串转换成坐标
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
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	public int compareTo(Object o) {
		Point tmp = (Point) o;
	        if (this.x < tmp.x) {
	            return -1;
	        } else if (this.x > tmp.x) {
	            return 1;
	        } else {
	            if (this.y < tmp.y) {
	                return -1;
	            } else if (this.y > tmp.y) {
	                return 1;
	            }
	            return 0;
	        }
	}
}
