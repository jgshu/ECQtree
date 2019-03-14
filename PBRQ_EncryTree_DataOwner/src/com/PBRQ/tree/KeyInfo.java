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
	private int treekeyNum ;// ��������Կ����
	private int treeKeyLength ;//��������Կ���ȣ�Ҫ��������ӵ���ߵ���Կ���Ⱥ��Ʒ���������ĳ���һ��
	private LinkedList<String> treeKey;// �����ڵ���t��hmac���ܵ���keyNum����Կ
	
	private String dataEncryType;//���ܵķ�����AES��DES
	private int dataKeyLength;//��Կ�Ƕ���λ���ȵ�
	private SecretKey dataKey;//�����ݼ��ܵ���Կ
	
	private String AllKeyPath;//������Կ��ŵ�·��������·�����ļ���
	
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
//			System.out.println("���سɹ�����Կ��Ϣ���£�"+this.toString());
//		}else {
//			System.out.println("������ȷ����Կ�Ƿ��ڸ�·���£�"+this.getAllKeyPath());
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
			KeyInfo tmp =(KeyInfo)oin.readObject(); // ǿ��ת����KeyInfo����
			
			this.treekeyNum=tmp.treekeyNum;
			this.treeKeyLength = tmp.treeKeyLength;
			this.treeKey=tmp.treeKey;
			
			this.dataKey = tmp.dataKey;
			this.dataEncryType = tmp.dataEncryType;
			this.dataKeyLength = tmp.dataKeyLength;
	        oin.close();
	        System.out.println("��Կ��ȡ�ɹ�����");
	        System.out.println("��Կ��������:"+this.toString());
		} catch (FileNotFoundException e) {
			System.out.println("δ�ҵ�key.out�ļ�");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ����Key��filename·���ļ���
	 */
	public void saveKey(String keyfilename,KeyInfo key) {
		//�ǵð���Կ��������
        //String keyfilename="key.dat";
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new
			        BufferedOutputStream(new FileOutputStream(keyfilename)));
			out.writeObject(key);
            out.close();
            System.out.println("��Կ����ɹ�������");
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
