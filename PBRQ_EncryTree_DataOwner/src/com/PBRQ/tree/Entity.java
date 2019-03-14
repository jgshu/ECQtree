package com.PBRQ.tree;

import java.io.Serializable;

/*
 * ���ݿ������γ��������Ǹ����ݱ�����Ӧ��ʵ����
 * ����ֻ�����������ԣ�����x��γ��y���Լ���������
 */
public class Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2569355326185778477L;
	private double x;
    private double y;
    private String opt_value;
	
    public Entity() {
    	this.x=0;
    	this.y=0;
    	opt_value="";
    }
	public Entity(double x, double y, String opt_value) {
		this.x = x;
		this.y = y;
		this.opt_value = opt_value;
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
	public String getOpt_value() {
		return opt_value;
	}
	public void setOpt_value(String opt_value) {
		this.opt_value = opt_value;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]" + ", opt_value=" + opt_value ;
	}
	
	public int compareTo(Object o) {
		Entity tmp = (Entity) o;
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
