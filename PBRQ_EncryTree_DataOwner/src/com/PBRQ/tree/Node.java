package com.PBRQ.tree;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import com.PBRQ.util.hmac;

public class Node implements Serializable {

	private String prefix = ""; // 树的当前节点所对应的前缀码
	private Node parent = null; // 父节点指针
	private int depth = 1;// 当前节点的深度
	private LinkedList<Data> data; // 当前节点所存储的数据指针，为叶节点状态下才new出来
	private NodeType nodeType = NodeType.EMPTY; // 记录当前节点的状态
	private Node child_1;
	private Node child_2;
	private Node child_3;
	private Node child_4;

	public Node() {
		this.prefix = "";
		this.parent = null;
		this.depth = 1;
		this.data = new LinkedList<Data>();
		this.nodeType = NodeType.EMPTY;
		this.child_1 = null;
		this.child_2 = null;
		this.child_3 = null;
		this.child_4 = null;
		// this.data = new LinkedList<Data>();
	}

	public Node(String prefix, Node parent, int depth, LinkedList<Data> data, NodeType nodeType, Node child_1,
			Node child_2, Node child_3, Node child_4) {
		this.prefix = prefix;
		this.parent = parent;
		this.depth = depth;
		this.data = data;
		this.nodeType = nodeType;
		this.child_1 = child_1;
		this.child_2 = child_2;
		this.child_3 = child_3;
		this.child_4 = child_4;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public LinkedList<Data> getData() {
		return data;
	}

	public void setData(LinkedList<Data> data) {
		this.data = data;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public Node getChild_1() {
		return child_1;
	}

	public void setChild_1(Node child_1) {
		this.child_1 = child_1;
	}

	public Node getChild_2() {
		return child_2;
	}

	public void setChild_2(Node child_2) {
		this.child_2 = child_2;
	}

	public Node getChild_3() {
		return child_3;
	}

	public void setChild_3(Node child_3) {
		this.child_3 = child_3;
	}

	public Node getChild_4() {
		return child_4;
	}

	public void setChild_4(Node child_4) {
		this.child_4 = child_4;
	}

	public LinkedList<String> PrefixEncry(LinkedList<String> key) {
		LinkedList<String> result = new LinkedList<String>();
		if (this.prefix != "") {
			Iterator<String> it = key.iterator();
			while (it.hasNext()) {
				String tmpkey = it.next();
				result.add(hmac.hamcsha1(this.prefix, tmpkey));
			}
		}
		return result;
	}

}
