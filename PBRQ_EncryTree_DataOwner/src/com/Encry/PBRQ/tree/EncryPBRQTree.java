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
	private int keyLength = 16;// 要保持数据拥有者的秘钥长度和云服务器随机的长度一致
	private int keyNum = 7;// 秘钥个数

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
	 * t为每个前缀码被t个密钥进行HMAC，n为这个哈希表所要存储的元素个数，C为每个元素占多少位
	 * HMAC-MD5 128	
	 * HMAC-SHA1 160 （采用这个）
	 */
	public BloomFilter BloomFilterBuild(LinkedList<LinkedList<String>> prefix) {
		BloomFilter bloomFilter = new BloomFilter(prefix,prefix.size() * prefix.getFirst().size());
		return bloomFilter;
	}
	
	public LinkedList<String> NodeSearch(EncryNode node,BloomFilter bloomFilter) {
		
		if (node==null) {
			System.out.println("EncryNode位空！！！");
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
