package com.PBRQ.tree;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.Encry.PBRQ.tree.EncryNode;
import com.Encry.PBRQ.tree.EncryNodeType;
import com.PBRQ.util.KeyCreate;
import com.PBRQ.util.hmac;

public class QuadTree implements Serializable{

	/**
	 * 
	 */
	private Node root; // ���ڵ��ָ�룬���ڵ��ִ�Ϊ�������ִ������ڵ�Ϊ��һ��
	private int threshold = 500; // ��Ҷ�ӽڵ������ﵽ500֮�����
	private int count = 1; // �ڵ�����
	private int treeDepth = 1; // ��¼���������
	private KeyInfo keyInfo;

	public QuadTree() {
		this.root = new Node();
		this.threshold = 500;
		this.count = 1;
		this.treeDepth = 1;

		this.keyInfo = new KeyInfo();
		this.keyInfo.setTreeKey(create_T_Key());
		this.keyInfo.setDataKey(create_Data_Key(this.keyInfo.getDataEncryType(),this.keyInfo.getDataKeyLength()));
		//this.keyInfo.saveKey("F:\\proper\\key.dat", this.keyInfo);
	}

	public QuadTree(Node root, int threshold, int count, int treeDepth,
			int treekeyNum, int treeKeyLength,String path,String dataEncryType,int dataKeyLength) {//path����·�����ļ�������
		this.root = root;
		this.threshold = threshold;
		this.count = count;
		this.treeDepth = treeDepth;
		
		this.keyInfo = new KeyInfo(treekeyNum,treeKeyLength,create_T_Key(),
				dataEncryType,dataKeyLength,create_Data_Key(dataEncryType, dataKeyLength),path);
		this.keyInfo.saveKey(path, this.keyInfo);
	}

	public Node getRoot() {
		return root;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTreeDepth() {
		return treeDepth;
	}

	public void setTreeDepth(int treeDepth) {
		this.treeDepth = treeDepth;
	}
	
	public KeyInfo getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(KeyInfo keyInfo) {
		this.keyInfo = keyInfo;
	}

	/*
	 * ����keyNum��key������HMAC
	 */
	public LinkedList<String> create_T_Key() {
		LinkedList<String> treekeys = new LinkedList<String>();
		for (int i = 0; i < this.keyInfo.getTreekeyNum(); i++) {
			treekeys.add(KeyCreate.genRandomNum(this.keyInfo.getTreeKeyLength()));
		}
		// System.out.println(this.key.toString());
		return treekeys;
	}
	
	public SecretKey create_Data_Key(String encryType,int dataKeyLength) {
		KeyGenerator kg = null;
		SecretKey datakey = null;
        try {
            //ָ���㷨,����ΪDES
            //kg = KeyGenerator.getInstance("DES", "SunJCE");
            kg = KeyGenerator.getInstance(encryType);

            //ָ����Կ����,����Խ��,����ǿ��Խ��
            kg.init(dataKeyLength);

            //������Կ
            datakey = kg.generateKey();

            //�ǵð���Կ��������
            //String keyfilename="key.dat";
//            ObjectOutputStream out = new ObjectOutputStream(new
//                BufferedOutputStream(new FileOutputStream(keyfilename)));
//            out.writeObject(key);
//            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return datakey;
	}
	/*
	 * ����Key��filename·���ļ���
	 */
//	public void saveKey(String keyfilename,Object key) {
//		//�ǵð���Կ��������
//        //String keyfilename="key.dat";
//		ObjectOutputStream out;
//		try {
//			out = new ObjectOutputStream(new
//			        BufferedOutputStream(new FileOutputStream(keyfilename)));
//			out.writeObject(key);
//            out.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/*
	 * ��node�ڵ�������data����
	 */
	public Boolean Insert(Node node, Data data) {
		Boolean result = false;
		switch (node.getNodeType()) {
		case EMPTY: // Ϊ�տ�ʼ�����߸շ��ѵĽڵ㣬��û��Point�Ž�ȥ
			node.getData().add(data);
			node.setNodeType(NodeType.LEAF);
			result = true;
			break;
		case LEAF: // Ҷ�ӽڵ㣬������һ������
			if (node.getData().size() < this.threshold) {// �������ڵ����洢��point���ڹ涨��Χ
				node.getData().add(data);
				result = true;
			} else {// �����������ݵ�list����threshold,��ô���ѣ����ӽڵ�洢
				System.out.println("��ʼ����");
				// System.out.println("����ǰ���е����Ŀ�� " + SumTreeNode(node));
				Split(node);// ��һ���ڵ�ֳ��ĸ����֣������ַ������ĸ����ֵģ����µ��ò�����ǲ���
				// System.out.println("���Ѻ����е����Ŀ�� " + SumTreeNode(node));
				node.setNodeType(NodeType.POINTER);
				// parent.getList().add(point);
				int idx = (node.getDepth() - 1) * 2;
				String ss = data.getStr().substring(idx, idx + 2);
				// System.out.println("idx="+idx+"\t"+"ss:"+ss);
				InsertChildData(node, ss, data);
			}
			break;
		case POINTER: // �м�ڵ㣬���洢����
			// �м�ڵ�û�����õķ���
			int idx = (node.getDepth() - 1) * 2;
			String ss = data.getStr().substring(idx, idx + 2);
			InsertChildData(node, ss, data);
			// �м�ڵ����������ķ���
			// if (node.getData().size() < this.threshold) {// �������ڵ����洢��point���ڹ涨��Χ
			// node.getData().add(data);
			// result = true;
			// } else {// �����������ݵ�list����threshold,��ô���ѣ����ӽڵ�洢
			// Split(node);// ��һ���ڵ�ֳ��ĸ����֣������ַ������ĸ����ֵģ����µ��ò�����ǲ���
			// int idx = node.getDepth() * 2;
			// String ss = data.getStr().substring(idx, idx + 2);
			// InsertChildData(node, ss, data);
			// }
			break;
		default:
			try {
				throw new Exception("Invalid nodeType in parent");
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		if (result == true)
			this.count++;
		return result;
	}

	/*
	 * �����ǰnode�ڵ�����ݳ�����ֵ�������node
	 */
	public void Split(Node node) {
		LinkedList<Data> list = node.getData();
		// ��Ϊ�����ˣ�����node�ڵ�����POINTER���ͣ��ӽڵ���EMPTY�ڵ�
		// ����ӽڵ�û�����ݣ���ôΪEMPTY�����ΪEMPTY����²�����һ�����ݣ��ͱ����LEAF�ڵ�
		if ((node.getNodeType() == NodeType.EMPTY) || (node.getNodeType() == NodeType.LEAF)) {
			node.setNodeType((node.getNodeType() == NodeType.LEAF) ? NodeType.LEAF : NodeType.POINTER);
			// �����ĸ�Node�ŵ���Ӧ��ָ����,�ڵ��ʼ������EMPTY����
			Node child_1 = new Node();
			child_1.setDepth(node.getDepth() + 1);
			child_1.setParent(node);
			child_1.setPrefix(node.getPrefix() + "00");
			child_1.setNodeType(NodeType.EMPTY);
			node.setChild_1(child_1);

			Node child_2 = new Node();
			child_2.setDepth(node.getDepth() + 1);
			child_2.setParent(node);
			child_2.setPrefix(node.getPrefix() + "01");
			child_2.setNodeType(NodeType.EMPTY);
			node.setChild_2(child_2);

			Node child_3 = new Node();
			child_3.setDepth(node.getDepth() + 1);
			child_3.setParent(node);
			child_3.setPrefix(node.getPrefix() + "10");
			child_3.setNodeType(NodeType.EMPTY);
			node.setChild_3(child_3);

			Node child_4 = new Node();
			child_4.setDepth(node.getDepth() + 1);
			child_4.setParent(node);
			child_4.setPrefix(node.getPrefix() + "11");
			child_4.setNodeType(NodeType.EMPTY);
			node.setChild_4(child_4);

			while (!list.isEmpty()) {
				Data temp = list.pop();
				String ss = temp.getStr().substring(2 * (node.getDepth() - 1), 2 * node.getDepth());
				switch (ss) {
				case "00":
					node.getChild_1().getData().add(temp);
					node.getChild_1().setNodeType(NodeType.LEAF);
					break;
				case "01":
					node.getChild_2().getData().add(temp);
					node.getChild_2().setNodeType(NodeType.LEAF);
					break;
				case "10":
					node.getChild_3().getData().add(temp);
					node.getChild_3().setNodeType(NodeType.LEAF);
					break;
				case "11":
					node.getChild_4().getData().add(temp);
					node.getChild_4().setNodeType(NodeType.LEAF);
					break;
				}
			}
		} else {
			// Ƿ���ǵ㣺����ڵ������Ѿ������ˣ�ֻ�ǣ����ڸýڵ������ˣ���ôֻҪ����ڵ�Ϳ�����
			while (!list.isEmpty()) {
				Data temp = list.pop();
				String ss = temp.getStr().substring(2 * (node.getDepth() - 1), 2 * node.getDepth());
				switch (ss) {
				case "00":
					if ((node.getChild_1().getNodeType() == NodeType.EMPTY)
							|| (node.getChild_1().getNodeType() == NodeType.LEAF)) {
						if (node.getChild_1().getData().size() < this.threshold) {
							node.getChild_1().getData().add(temp);
							node.getChild_1().setNodeType(NodeType.LEAF);
						}
					}
					Insert(node.getChild_1(), temp);
					break;
				case "01":
					if ((node.getChild_2().getNodeType() == NodeType.EMPTY)
							|| (node.getChild_2().getNodeType() == NodeType.LEAF)) {
						if (node.getChild_2().getData().size() < this.threshold) {
							node.getChild_2().getData().add(temp);
							node.getChild_2().setNodeType(NodeType.LEAF);
						}
					}
					Insert(node.getChild_2(), temp);
					break;
				case "10":
					if ((node.getChild_3().getNodeType() == NodeType.EMPTY)
							|| (node.getChild_3().getNodeType() == NodeType.LEAF)) {
						if (node.getChild_3().getData().size() < this.threshold) {
							node.getChild_3().getData().add(temp);
							node.getChild_3().setNodeType(NodeType.LEAF);
						}
					}
					Insert(node.getChild_3(), temp);
					break;
				case "11":
					if ((node.getChild_4().getNodeType() == NodeType.EMPTY)
							|| (node.getChild_4().getNodeType() == NodeType.LEAF)) {
						if (node.getChild_4().getData().size() < this.threshold) {
							node.getChild_4().getData().add(temp);
							node.getChild_4().setNodeType(NodeType.LEAF);
						}
					}
					Insert(node.getChild_4(), temp);
					break;
				}
			}

		}
	}

	public Boolean InsertChildData(Node node, String ss, Data data) {
		Boolean result = false;
		switch (ss) {
		case "00":
			result = Insert(node.getChild_1(), data);
			break;
		case "01":
			result = Insert(node.getChild_2(), data);
			break;
		case "10":
			result = Insert(node.getChild_3(), data);
			break;
		case "11":
			result = Insert(node.getChild_4(), data);
			break;
		default:
			try {
				throw new Exception("Invalid nodeType in parent");
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = false;
			break;
		}
		return result;
	}

	/*
	 * ����node�����������Ƿ����data
	 */
	public Boolean find(Node node, Data data) {
		Boolean result = false;
		int deep = 2 * node.getDepth() - 2;
		String ss = data.getStr().substring(deep, deep + 2);
		if (node.getData().isEmpty()) {// ����ýڵ��list�ǿյģ���������ӽڵ�
			result = findChildPoint(node, data, ss);
		} else {// ����ǿգ��������ڵ��List��������û�У�����ֱ��return�����������û���ڼ���ִ�к���ģ�������������û��
			Iterator<Data> it = node.getData().iterator();
			while (it.hasNext()) {
				Data tmp = (Data) it.next();
				if (tmp.getStr().equals(data.getStr())) {
					result = true;
					System.out.println("======���ҳɹ�======");
					return true;
				}
			}
			result = findChildPoint(node, data, ss);
		}
		return result;
	}

	/*
	 * ����node�������Ƿ����data���ݣ�����node�ڵ������
	 */
	public Boolean findChildPoint(Node node, Data data, String ss) {
		Boolean result = false;
		switch (ss) {
		case "00":
			if (node.getChild_1() != null)
				result = find(node.getChild_1(), data);
			break;
		case "01":
			if (node.getChild_2() != null)
				result = find(node.getChild_2(), data);
			break;
		case "10":
			if (node.getChild_3() != null)
				result = find(node.getChild_3(), data);
			break;
		case "11":
			if (node.getChild_4() != null)
				result = find(node.getChild_4(), data);
			break;
		default:
			try {
				throw new Exception("Invalid nodeType in parent");
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = false;
			break;
		}
		return result;
	}

	/*
	 * ��ӡ��һ��LinkedList<Data>���е�����
	 */
	public void printList(LinkedList<Data> temp) {
		System.out.println("==============��ʼ��ӡ============");
		Iterator<Data> iterator = temp.iterator();
		while (iterator.hasNext()) {
			Data data = iterator.next();
			System.out.println(data.getStr());
		}
		System.out.println("==============������ӡ============");
	}

	/*
	 * �����ַ����Ƚ�
	 */
	public int compareToStr(Data a, Data b) {// a<b -1; a=b 0 a>b 1
		int i = 0;
		while (i < a.getLen()) {
			if (a.getStr().charAt(i) < b.getStr().charAt(i)) {
				return -1;
			} else if (a.getStr().charAt(i) > b.getStr().charAt(i)) {
				return 1;
			}
			i++;
		}
		return 0;
	}

	// ��������List��ӵ�ʱ���һ��ȫ��
	public LinkedList<Data> addList(LinkedList<Data> result, LinkedList<Data> to) {
		Iterator<Data> it = to.iterator();
		while (it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}

	/*
	 * �����start��endҪ�Ѿ��д�С˳��� ���س�start��end��Χ�ɵĳ����������ڵ�����
	 */
	public LinkedList<Data> SquareRangeSearch(Node node, Data start, Data end) {
		// �ȴӸýڵ��List�����
		LinkedList<Data> result = new LinkedList<Data>();
		LinkedList<Data> nodelist = node.getData();
		if (nodelist.size() > 0) {
			Iterator<Data> it = nodelist.iterator();
			while (it.hasNext()) {
				Data cur = it.next();
				if (compareToStr(cur, start) >= 0 && compareToStr(cur, end) <= 0) {
					result.add(cur);
				}
			}
		}
		// �ٴӸýڵ���ӽڵ�List�����
		int deep = 2 * node.getDepth() - 2;
		String ss = start.getStr().substring(deep, deep + 2);
		String ee = end.getStr().substring(deep, deep + 2);

		HashMap<String, Integer> mm = new HashMap<String, Integer>();
		mm.put("00", 0);
		mm.put("01", 1);
		mm.put("10", 2);
		mm.put("11", 3);
		if (node.getChild_1() != null) {
			if (mm.get(ss) <= mm.get("00") && mm.get("00") <= mm.get(ee)) {
				result = addList(result, SquareRangeSearch(node.getChild_1(), start, end));
			}
		}
		if (node.getChild_2() != null) {// ���01�ڷ�Χ�ڻ����ڶ˵���
			if (mm.get(ss) <= mm.get("01") && mm.get("01") <= mm.get(ee)) {
				result = addList(result, SquareRangeSearch(node.getChild_2(), start, end));
			}
		}
		if (node.getChild_3() != null) {
			if (mm.get(ss) <= mm.get("10") && mm.get("10") <= mm.get(ee)) {
				result = addList(result, SquareRangeSearch(node.getChild_3(), start, end));
			}
		}
		if (node.getChild_4() != null) {
			if (mm.get(ss) <= mm.get("11") && mm.get("11") <= mm.get(ee)) {
				result = addList(result, SquareRangeSearch(node.getChild_4(), start, end));
			}
		}
		return result;
	}

	/**
	 * ���ݾ�γ�ȺͰ뾶���㾭γ�ȷ�Χ
	 *
	 * @param raidus
	 *            ��λ��
	 * @return minLat, minLng, maxLat, maxLng
	 */
	public double[] getAround(double lat, double lon, double raidus) {

		Double latitude = lat;//γ��
		Double longitude = lon;//����

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

	public int SumTreeNode(Node node) {
		int result = 0;
		result += node.getData().size();
		if (node.getChild_1() != null)
			result += SumTreeNode(node.getChild_1());
		if (node.getChild_2() != null)
			result += SumTreeNode(node.getChild_2());
		if (node.getChild_3() != null)
			result += SumTreeNode(node.getChild_3());
		if (node.getChild_4() != null)
			result += SumTreeNode(node.getChild_4());
		return result;
	}
	
	public LinkedList<Data> LBS_Square(Node node,Point center, double R){
		double array[] = getAround(center.getX(),center.getY(),R);
		Data start = new Data(array[0],array[1],"");
		Data end = new Data(array[2],array[3],"");
		LinkedList<Data>result = SquareRangeSearch(node, start, end);
		return result;
	}

	public LinkedList<String> EncryPrefix(String prefix) {
		LinkedList<String> out = new LinkedList<String>();
		Iterator<String> it = this.keyInfo.getTreeKey().iterator();
		while (it.hasNext()) {
			String cur = it.next();
			out.add(hmac.hamcsha1(prefix,cur));
		}
		return out;
	}
	
	/*
	 * ����ΪNodeҶ�ӽڵ�洢������LinkedList<Data>���ݣ�
	 * ͨ����ԿdataKey�Լ����ܷ���dataEncryType���硰AES�������ܺ���ת����ֽ������ٶ��ֽ������ܣ��ٰ��ֽ�������ַ�������
	 * ���ص����ַ���
	 */
	public String EncryDataList(LinkedList<Data>dd,SecretKey dataKey,String dataEncryType) {
		//���ܣ�
		Cipher cp;
		try {
			cp = Cipher.getInstance(dataEncryType);//����������
			cp.init(Cipher.ENCRYPT_MODE, dataKey); //��ʼ��
			byte[] ptext = toByteArray(dd);//��LinkedList<Data>����ת����ֽ�����Ȼ����м���
			byte [] ctext = cp.doFinal(ptext); //����
			//System.out.println("ctext:"+ctext);
			return ctext.toString();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	/*
	 * δ��ɣ����⣺��ΰ����ݶ������DES����
	 * Ŀǰ�뷨�������ݶ������л���Ȼ������л��Ķ������DES����
	 */
	public EncryNode Node_to_EncryNode(Node node,EncryNode encryNode) {
		int flag=0;//����ĸ��ӽڵ㶼�ǿյģ���flag=4����ô�ýڵ�ΪҶ�ӽڵ�
		//���ӽڵ�ת���ɼ��ܽڵ�
		if (node.getChild_1()!=null) {//�ӽڵ�1�ǿ���ͬ������һ�����ܵ��ӽڵ㣬�����ڼ��ܵĸ��ڵ�����
			EncryNode c1 = new EncryNode(node.getChild_1().PrefixEncry(this.keyInfo.getTreeKey()));//ʵ��ǰ׺hmac����
			c1.setDepth(node.getDepth()+1);
			encryNode.setChild_1(c1);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_1(),c1);//�����ӽڵ�Ҳ���м���ת��
		}
		if (node.getChild_2()!=null) {
			EncryNode c2 = new EncryNode(node.getChild_2().PrefixEncry(this.keyInfo.getTreeKey()));
			c2.setDepth(node.getDepth()+1);
			encryNode.setChild_2(c2);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_2(),c2);//�����ӽڵ�Ҳ���м���ת��
		}
		if (node.getChild_3()!=null) {
			EncryNode c3 = new EncryNode(node.getChild_3().PrefixEncry(this.keyInfo.getTreeKey()));
			c3.setDepth(node.getDepth()+1);
			encryNode.setChild_3(c3);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_3(),c3);//�����ӽڵ�Ҳ���м���ת��
		}
		if (node.getChild_4()!=null) {
			EncryNode c4 = new EncryNode(node.getChild_4().PrefixEncry(this.keyInfo.getTreeKey()));
			c4.setDepth(node.getDepth()+1);
			encryNode.setChild_4(c4);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_4(),c4);//�����ӽڵ�Ҳ���м���ת��
		}
		if(flag==0) {//�������ǿյģ���ΪҶ�ӽڵ㣬��ô��ǰ�ڵ������ݣ������ݼ��ܷŽ�ȥ
			if (node.getData().size()>0) {
				//System.out.println("tt:"+tt);
				encryNode.setData(EncryDataList(node.getData(),this.keyInfo.getDataKey(),this.keyInfo.getDataEncryType()));
				encryNode.setNodeType(EncryNodeType.LEAF);
			}else {
				encryNode.setNodeType(EncryNodeType.EMPTY);
			}
		}
		return encryNode;
	}
	/**  
     * ����ת����  
     * @param obj  
     * @return  
     */  
    public byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
    }   
       
    /**  
     * ����ת����  
     * @param bytes  
     * @return  
     */  
    public Object toObject (byte[] bytes) {      
        Object obj = null;      
        try {        
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);        
            ObjectInputStream ois = new ObjectInputStream (bis);        
            obj = ois.readObject();      
            ois.close();   
            bis.close();   
        } catch (IOException ex) {        
            ex.printStackTrace();   
        } catch (ClassNotFoundException ex) {        
            ex.printStackTrace();   
        }      
        return obj;    
    }   
    
    public void DrawTree(Node tmp) {
		for (int i = 0; i < tmp.getDepth(); i++) {
			System.out.print("-");
		}
		System.out.println(tmp.getPrefix());
		if (tmp.getChild_1() == null && tmp.getChild_2() == null && tmp.getChild_3() == null
				&& tmp.getChild_4() == null) {
			printList(tmp.getData());
		}
		if (tmp.getChild_1() != null)
			DrawTree(tmp.getChild_1());
		if (tmp.getChild_2() != null)
			DrawTree(tmp.getChild_2());
		if (tmp.getChild_3() != null)
			DrawTree(tmp.getChild_3());
		if (tmp.getChild_4() != null)
			DrawTree(tmp.getChild_4());
	}
    
}