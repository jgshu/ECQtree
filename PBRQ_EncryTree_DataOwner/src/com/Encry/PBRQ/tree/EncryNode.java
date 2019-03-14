package com.Encry.PBRQ.tree;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class EncryNode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2585519541584991938L;
	private LinkedList<String> Encry_prefix;
	private int depth;
	private EncryNode child_1;
	private EncryNode child_2;
	private EncryNode child_3;
	private EncryNode child_4;
	private EncryNodeType nodeType = EncryNodeType.EMPTY;
	private String data=null;//原始数据加密后一整个对象都变成了一个串传过来，所以只要用一个String接收就可以了
	
	public EncryNode() {
		this.Encry_prefix = new LinkedList<String>();
		this.depth=1;
		
		this.data = null;
		this.child_1 = null;
		this.child_2 = null;
		this.child_3 = null;
		this.child_4 = null;
		this.nodeType=EncryNodeType.EMPTY;
	}
	public EncryNode(LinkedList<String> encryPrefix) {
		this.Encry_prefix = encryPrefix;
		this.depth=1;
		
		this.data = null;
		this.child_1 = null;
		this.child_2 = null;
		this.child_3 = null;
		this.child_4 = null;
		this.nodeType=EncryNodeType.EMPTY;
	}

	public EncryNode(LinkedList<String> encry_prefix, EncryNode child_1, EncryNode child_2, EncryNode child_3,
			EncryNode child_4, EncryNodeType nodeType, String data) {
		this.Encry_prefix = encry_prefix;
		this.depth=1;
		
		this.child_1 = child_1;
		this.child_2 = child_2;
		this.child_3 = child_3;
		this.child_4 = child_4;
		this.nodeType = nodeType;
		this.data = data;
	}

	public LinkedList<String> getEncry_prefix() {
		return Encry_prefix;
	}

	public void setEncry_prefix(LinkedList<String> encry_prefix) {
		Encry_prefix = encry_prefix;
	}

	public EncryNode getChild_1() {
		return child_1;
	}

	public void setChild_1(EncryNode child_1) {
		this.child_1 = child_1;
	}

	public EncryNode getChild_2() {
		return child_2;
	}

	public void setChild_2(EncryNode child_2) {
		this.child_2 = child_2;
	}

	public EncryNode getChild_3() {
		return child_3;
	}

	public void setChild_3(EncryNode child_3) {
		this.child_3 = child_3;
	}

	public EncryNode getChild_4() {
		return child_4;
	}

	public void setChild_4(EncryNode child_4) {
		this.child_4 = child_4;
	}

	public EncryNodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(EncryNodeType nodeType) {
		this.nodeType = nodeType;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public String ArrPrefixToString() {
		String result="";
		Iterator<String>iterator=this.Encry_prefix.iterator();
		while (iterator.hasNext()) {
			result+=iterator.next()+"：";
		}
		return result;
	}

}
