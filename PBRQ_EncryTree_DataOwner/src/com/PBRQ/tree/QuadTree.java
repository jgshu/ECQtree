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
	private Node root; // 根节点的指针，根节点字串为“”空字串，根节点为第一层
	private int threshold = 500; // 树叶子节点数量达到500之后分裂
	private int count = 1; // 节点数量
	private int treeDepth = 1; // 记录下树的深度
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
			int treekeyNum, int treeKeyLength,String path,String dataEncryType,int dataKeyLength) {//path包括路径和文件名名字
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
	 * 创建keyNum个key，用于HMAC
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
            //指定算法,这里为DES
            //kg = KeyGenerator.getInstance("DES", "SunJCE");
            kg = KeyGenerator.getInstance(encryType);

            //指定密钥长度,长度越高,加密强度越大
            kg.init(dataKeyLength);

            //产生密钥
            datakey = kg.generateKey();

            //记得把密钥保存起来
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
	 * 保存Key到filename路径文件中
	 */
//	public void saveKey(String keyfilename,Object key) {
//		//记得把密钥保存起来
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
	 * 向node节点树插入data数据
	 */
	public Boolean Insert(Node node, Data data) {
		Boolean result = false;
		switch (node.getNodeType()) {
		case EMPTY: // 为刚开始，或者刚分裂的节点，还没有Point放进去
			node.getData().add(data);
			node.setNodeType(NodeType.LEAF);
			result = true;
			break;
		case LEAF: // 叶子节点，至少有一个数据
			if (node.getData().size() < this.threshold) {// 如果这个节点所存储的point表在规定范围
				node.getData().add(data);
				result = true;
			} else {// 如果这个存数据的list表超过threshold,那么分裂，用子节点存储
				System.out.println("开始分裂");
				// System.out.println("分裂前所有点的数目： " + SumTreeNode(node));
				Split(node);// 将一个节点分出四个部分，查找字符串是哪个部分的，重新调用插入进那部分
				// System.out.println("分裂后所有点的数目： " + SumTreeNode(node));
				node.setNodeType(NodeType.POINTER);
				// parent.getList().add(point);
				int idx = (node.getDepth() - 1) * 2;
				String ss = data.getStr().substring(idx, idx + 2);
				// System.out.println("idx="+idx+"\t"+"ss:"+ss);
				InsertChildData(node, ss, data);
			}
			break;
		case POINTER: // 中间节点，不存储数据
			// 中间节点没有利用的方法
			int idx = (node.getDepth() - 1) * 2;
			String ss = data.getStr().substring(idx, idx + 2);
			InsertChildData(node, ss, data);
			// 中间节点利用起来的方法
			// if (node.getData().size() < this.threshold) {// 如果这个节点所存储的point表在规定范围
			// node.getData().add(data);
			// result = true;
			// } else {// 如果这个存数据的list表超过threshold,那么分裂，用子节点存储
			// Split(node);// 将一个节点分出四个部分，查找字符串是哪个部分的，重新调用插入进那部分
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
	 * 如果当前node节点的数据超过阈值，则分裂node
	 */
	public void Split(Node node) {
		LinkedList<Data> list = node.getData();
		// 因为分裂了，所以node节点变成了POINTER类型，子节点变成EMPTY节点
		// 如果子节点没有数据，那么为EMPTY，如果为EMPTY情况下插入了一个数据，就变成了LEAF节点
		if ((node.getNodeType() == NodeType.EMPTY) || (node.getNodeType() == NodeType.LEAF)) {
			node.setNodeType((node.getNodeType() == NodeType.LEAF) ? NodeType.LEAF : NodeType.POINTER);
			// 定义四个Node放到对应的指针上,节点初始化都是EMPTY类型
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
			// 欠考虑点：如果节点下面已经分裂了，只是，现在该节点又满了，那么只要分配节点就可以了
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
	 * 查找node及其子树下是否包含data
	 */
	public Boolean find(Node node, Data data) {
		Boolean result = false;
		int deep = 2 * node.getDepth() - 2;
		String ss = data.getStr().substring(deep, deep + 2);
		if (node.getData().isEmpty()) {// 如果该节点的list是空的，则查找其子节点
			result = findChildPoint(node, data, ss);
		} else {// 如果非空，则对这个节点的List遍历看有没有，有则直接return跳出来，如果没有在继续执行后面的，查找其子树有没有
			Iterator<Data> it = node.getData().iterator();
			while (it.hasNext()) {
				Data tmp = (Data) it.next();
				if (tmp.getStr().equals(data.getStr())) {
					result = true;
					System.out.println("======查找成功======");
					return true;
				}
			}
			result = findChildPoint(node, data, ss);
		}
		return result;
	}

	/*
	 * 查找node子树下是否包含data数据，不查node节点的数据
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
	 * 打印出一个LinkedList<Data>所有的数据
	 */
	public void printList(LinkedList<Data> temp) {
		System.out.println("==============开始打印============");
		Iterator<Data> iterator = temp.iterator();
		while (iterator.hasNext()) {
			Data data = iterator.next();
			System.out.println(data.getStr());
		}
		System.out.println("==============结束打印============");
	}

	/*
	 * 用于字符串比较
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

	// 用于两个List相加的时候把一个全部
	public LinkedList<Data> addList(LinkedList<Data> result, LinkedList<Data> to) {
		Iterator<Data> it = to.iterator();
		while (it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}

	/*
	 * 放入的start和end要已经有大小顺序的 返回出start和end所围成的长方形区域内的数据
	 */
	public LinkedList<Data> SquareRangeSearch(Node node, Data start, Data end) {
		// 先从该节点的List里查找
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
		// 再从该节点的子节点List里查找
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
		if (node.getChild_2() != null) {// 如果01在范围内或者在端点上
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
	 * 根据经纬度和半径计算经纬度范围
	 *
	 * @param raidus
	 *            单位米
	 * @return minLat, minLng, maxLat, maxLng
	 */
	public double[] getAround(double lat, double lon, double raidus) {

		Double latitude = lat;//纬度
		Double longitude = lon;//经度

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
	 * 输入为Node叶子节点存储的数据LinkedList<Data>数据，
	 * 通过秘钥dataKey以及加密方法dataEncryType（如“AES”）加密后，先转变成字节流，再对字节流加密，再把字节流变成字符串返回
	 * 返回的是字符串
	 */
	public String EncryDataList(LinkedList<Data>dd,SecretKey dataKey,String dataEncryType) {
		//加密：
		Cipher cp;
		try {
			cp = Cipher.getInstance(dataEncryType);//创建密码器
			cp.init(Cipher.ENCRYPT_MODE, dataKey); //初始化
			byte[] ptext = toByteArray(dd);//把LinkedList<Data>对象转变成字节流，然后进行加密
			byte [] ctext = cp.doFinal(ptext); //加密
			//System.out.println("ctext:"+ctext);
			return ctext.toString();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	/*
	 * 未完成，问题：如何把数据对象进行DES加密
	 * 目前想法：把数据对象序列化，然后对序列化的对象进行DES加密
	 */
	public EncryNode Node_to_EncryNode(Node node,EncryNode encryNode) {
		int flag=0;//如果四个子节点都是空的，则flag=4，那么该节点为叶子节点
		//把子节点转换成加密节点
		if (node.getChild_1()!=null) {//子节点1非空则同样创建一个加密的子节点，把其于加密的父节点连接
			EncryNode c1 = new EncryNode(node.getChild_1().PrefixEncry(this.keyInfo.getTreeKey()));//实行前缀hmac加密
			c1.setDepth(node.getDepth()+1);
			encryNode.setChild_1(c1);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_1(),c1);//对其子节点也进行加密转换
		}
		if (node.getChild_2()!=null) {
			EncryNode c2 = new EncryNode(node.getChild_2().PrefixEncry(this.keyInfo.getTreeKey()));
			c2.setDepth(node.getDepth()+1);
			encryNode.setChild_2(c2);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_2(),c2);//对其子节点也进行加密转换
		}
		if (node.getChild_3()!=null) {
			EncryNode c3 = new EncryNode(node.getChild_3().PrefixEncry(this.keyInfo.getTreeKey()));
			c3.setDepth(node.getDepth()+1);
			encryNode.setChild_3(c3);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_3(),c3);//对其子节点也进行加密转换
		}
		if (node.getChild_4()!=null) {
			EncryNode c4 = new EncryNode(node.getChild_4().PrefixEncry(this.keyInfo.getTreeKey()));
			c4.setDepth(node.getDepth()+1);
			encryNode.setChild_4(c4);
			encryNode.setNodeType(EncryNodeType.POINTER);
			flag++;
			Node_to_EncryNode(node.getChild_4(),c4);//对其子节点也进行加密转换
		}
		if(flag==0) {//子树都是空的，则为叶子节点，那么当前节点有数据，把数据加密放进去
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
     * 对象转数组  
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
     * 数组转对象  
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