package com.PBRQ.tree;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import javax.crypto.SecretKey;


public class KeyInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int treekeyNum ;// 树加密秘钥个数
	private int treeKeyLength ;//树加密密钥长度，要保持数据拥有者的秘钥长度和云服务器随机的长度一致
	private LinkedList<String> treeKey;// 给树节点做t个hmac加密的那keyNum个秘钥
	
	private String dataEncryType;//加密的方法：AES、DES
	private int dataKeyLength;//密钥是多少位长度的
	private SecretKey dataKey;//给数据加密的密钥
	
	private String AllKeyPath;//两个密钥存放的路径，包括路径和文件名
	
	public KeyInfo() {
		this.treekeyNum=7;
		this.treeKeyLength=16;
		this.treeKey = new  LinkedList<String>();
		
		this.dataKey = null;
		this.dataEncryType="AES";
		this.dataKeyLength=128;
		this.AllKeyPath="F:\\proper\\key.out";
		
//		if (judeFileExists(this.getAllKeyPath())) {
//			this.Read(this.getAllKeyPath());
//			System.out.println("加载成功，秘钥信息如下："+this.toString());
//		}else {
//			System.out.println("请重新确认秘钥是否在该路径下："+this.getAllKeyPath());
//		}
	}
	public KeyInfo(int treekeyNum, int treeKeyLength, LinkedList<String> treeKey, String dataEncryType,
			int dataKeyLength, SecretKey dataKey, String allKeyPath) {
		super();
		this.treekeyNum = treekeyNum;
		this.treeKeyLength = treeKeyLength;
		this.treeKey = treeKey;
		this.dataKey = dataKey;
		this.dataEncryType = dataEncryType;
		this.dataKeyLength = dataKeyLength;
		this.AllKeyPath = allKeyPath;
	}
	public KeyInfo(LinkedList<String> treeKey,SecretKey dataKey) {
		this();
		this.treeKey = treeKey;
		this.dataKey = dataKey;
	}

	public void Read(String filename) {
		this.AllKeyPath =filename;
		File file = new File(this.AllKeyPath);
		ObjectInputStream oin;
		try {
			oin = new ObjectInputStream(new FileInputStream(file));
			KeyInfo tmp =(KeyInfo)oin.readObject(); // 强制转换到KeyInfo类型
			
			this.treekeyNum=tmp.treekeyNum;
			this.treeKeyLength = tmp.treeKeyLength;
			this.treeKey=tmp.treeKey;
			
			this.dataKey = tmp.dataKey;
			this.dataEncryType = tmp.dataEncryType;
			this.dataKeyLength = tmp.dataKeyLength;
	        oin.close();
	        System.out.println("密钥读取成功！！");
	        System.out.println("秘钥内容如下:"+this.toString());
		} catch (FileNotFoundException e) {
			System.out.println("未找到key.out文件");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 保存Key到filename路径文件中
	 */
	public void saveKey(String keyfilename,KeyInfo key) {
		//记得把密钥保存起来
        //String keyfilename="key.dat";
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new
			        BufferedOutputStream(new FileOutputStream(keyfilename)));
			out.writeObject(key);
            out.close();
            System.out.println("秘钥保存成功！！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTreekeyNum() {
		return treekeyNum;
	}
	public void setTreekeyNum(int treekeyNum) {
		this.treekeyNum = treekeyNum;
	}
	public int getTreeKeyLength() {
		return treeKeyLength;
	}

	public void setTreeKeyLength(int treeKeyLength) {
		this.treeKeyLength = treeKeyLength;
	}

	public String getDataEncryType() {
		return dataEncryType;
	}

	public void setDataEncryType(String dataEncryType) {
		this.dataEncryType = dataEncryType;
	}

	public int getDataKeyLength() {
		return dataKeyLength;
	}

	public void setDataKeyLength(int dataKeyLength) {
		this.dataKeyLength = dataKeyLength;
	}

	public String getAllKeyPath() {
		return AllKeyPath;
	}

	public void setAllKeyPath(String allKeyPath) {
		AllKeyPath = allKeyPath;
	}

	public LinkedList<String> getTreeKey() {
		return treeKey;
	}

	public void setTreeKey(LinkedList<String> treeKey) {
		this.treeKey = treeKey;
	}

	public void setDataKey(SecretKey dataKey) {
		this.dataKey = dataKey;
	}

	public SecretKey getDataKey() {
		return dataKey;
	}
	@Override
	public String toString() {
		return "KeyInfo [treekeyNum=" + treekeyNum + ", treeKeyLength=" + treeKeyLength + ", treeKey=" + treeKey
				+ ", dataEncryType=" + dataEncryType + ", dataKeyLength=" + dataKeyLength + ", dataKey=" + dataKey
				+ ", AllKeyPath=" + AllKeyPath + "]";
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
