package com.PBRQ.tree;

import java.io.Serializable;

import com.Encry.PBRQ.tree.EncryData;

public class Data implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1766526903716039520L;
	private Entity entity;
	private String str;//�洢entity�еģ�x,y��,����ת�����ַ�������ַ���
	private int len=20;//��x,y��ת�����ַ�������ַ�������
	
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
	 * ����ά(x,y)����ת�����ַ�����������ͨ�����꾫������ֹѭ��
	 * ���е�һ������11���ڶ�������01������������00������������10
	 * ��γ�ȷ�Χ minX=-180;maxX=180;minY=-90;maxY=90;
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
	
}