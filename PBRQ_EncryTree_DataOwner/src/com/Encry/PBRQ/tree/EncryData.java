package com.Encry.PBRQ.tree;

import java.io.Serializable;

public class EncryData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4860191859276492456L;
	private String data;
	
	public EncryData() {
		this.data = "";
	}
	public EncryData(String ss) {
		this.data = ss;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
