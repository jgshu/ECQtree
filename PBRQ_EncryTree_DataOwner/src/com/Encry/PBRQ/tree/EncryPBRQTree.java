package com.Encry.PBRQ.tree;

import java.io.Serializable;
import java.util.LinkedList;

import com.PBRQ.util.KeyCreate;
import com.PBRQ.util.BloomFilter;

public class EncryPBRQTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6401619215732282122L;
	private EncryNode root;
	private int depth;

	private String randomKey;
	private int keyLength = 16;// Ҫ��������ӵ���ߵ���Կ���Ⱥ��Ʒ���������ĳ���һ��
	private int keyNum = 7;// ��Կ����

	public EncryPBRQTree() {
		this.root = new EncryNode();
		this.depth = 1;

		this.keyLength = 16;
		this.keyNum = 7;
		this.randomKey = KeyCreate.genRandomNum(keyLength);
	}

	public EncryPBRQTree(EncryNode root, int keyLength, int keyNum) {
		this.root = root;
		this.depth = 1;

		this.randomKey = KeyCreate.genRandomNum(keyLength);
		this.keyLength = keyLength;
		this.keyNum = keyNum;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}

	public int getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(int keyNum) {
		this.keyNum = keyNum;
	}

	public EncryNode getRoot() {
		return root;
	}

	public void setRoot(EncryNode root) {
		this.root = root;
	}

	public String getRandomKey() {
		return randomKey;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void EncryDrawTree(EncryNode tmp) {
		for (int i = 0; i < tmp.getDepth(); i++) {
			System.out.print("-");
		}
		System.out.println(tmp.ArrPrefixToString());
		if (tmp.getChild_1() == null && tmp.getChild_2() == null && tmp.getChild_3() == null
				&& tmp.getChild_4() == null) {
			System.out.println(tmp.getData());
		}
		if (tmp.getChild_1() != null)
			EncryDrawTree(tmp.getChild_1());
		if (tmp.getChild_2() != null)
			EncryDrawTree(tmp.getChild_2());
		if (tmp.getChild_3() != null)
			EncryDrawTree(tmp.getChild_3());
		if (tmp.getChild_4() != null)
			EncryDrawTree(tmp.getChild_4());
	}

	/*
	 * tΪÿ��ǰ׺�뱻t����Կ����HMAC��nΪ�����ϣ����Ҫ�洢��Ԫ�ظ�����CΪÿ��Ԫ��ռ����λ
	 * HMAC-MD5 128	
	 * HMAC-SHA1 160 �����������
	 */
	public BloomFilter BloomFilterBuild(LinkedList<LinkedList<String>> prefix) {
		BloomFilter bloomFilter = new BloomFilter(prefix,prefix.size() * prefix.getFirst().size());
		return bloomFilter;
	}
	
	public LinkedList<String> NodeSearch(EncryNode node,BloomFilter bloomFilter) {
		
		if (node==null) {
			System.out.println("EncryNodeλ�գ�����");
			return null;
		}
		
		LinkedList<String> result = new LinkedList<String>();
		if (node.getChild_1()==null && node.getChild_2()==null && node.getChild_3()==null && node.getChild_4()==null) {
			result.add(node.getData());
			return result;
		}
		if (node.getChild_1()!=null && bloomFilter.isExit(node.getEncry_prefix())) {
			result.addAll(NodeSearch(node.getChild_1(),bloomFilter));
		}
		if (node.getChild_2()!=null && bloomFilter.isExit(node.getEncry_prefix())) {
			result.addAll(NodeSearch(node.getChild_2(),bloomFilter));
		}
		if (node.getChild_3()!=null && bloomFilter.isExit(node.getEncry_prefix())) {
			result.addAll(NodeSearch(node.getChild_3(),bloomFilter));
		}
		if (node.getChild_4()!=null && bloomFilter.isExit(node.getEncry_prefix())) {
			result.addAll(NodeSearch(node.getChild_4(),bloomFilter));
		}
		return result;
	}

	public LinkedList<String> TreeSearch(LinkedList<LinkedList<String>> prefix) {
		
		BloomFilter bloomFilter =new BloomFilter(prefix,prefix.size() * prefix.getFirst().size());
		
		return NodeSearch(this.root,bloomFilter);
	}
}
