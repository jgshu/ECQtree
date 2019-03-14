package test.com.PBRQ.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.Encry.PBRQ.tree.EncryNode;
import com.Encry.PBRQ.tree.EncryPBRQTree;
import com.PBRQ.tree.Data;
import com.PBRQ.tree.Entity;
import com.PBRQ.tree.KeyInfo;
import com.PBRQ.tree.Point;
import com.PBRQ.tree.QuadTree;
import com.PBRQ.util.ReadData;

public class DataOwnerTest {
	public QuadTree build() {
		List<Entity> PList = new ArrayList<Entity>();
		String resourcePath = "F:\\proper\\data\\20000.txt";
		ReadData rd = new ReadData();
		rd.LoadPointsFromFile(resourcePath);
		PList = rd.get_pointList();
		System.out.println("加载的节点数：" + PList.size());
		QuadTree qTree = new QuadTree();
		System.out.println("=========开始插入==========");
		HashMap<String, Integer> isContain = new HashMap<String, Integer>();
		for (int i = 0; i < PList.size(); i++) {
			Data data = new Data(PList.get(i));
			if (isContain.containsKey(data.getStr())) {
				// System.out.println("有重复的，是=" + point.getStr());
			} else {
				isContain.put(data.getStr(), 1);
			}
			qTree.Insert(qTree.getRoot(), data);
		}
		System.out.println("=========插入完成==========");
		return qTree;
	}
	
	@Test
	public void TestCreatTime() {
		long startTime = System.currentTimeMillis();    //获取开始时间
		QuadTree tree = build();
		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) );    //输出程序运行时间
	}
	
	@Test
	public void TestTreeEncry() {
		long startTree = System.currentTimeMillis();    //获取开始时间
		QuadTree tree = build();
		long endTree = System.currentTimeMillis();    //获取结束时间
		System.out.println("创建树的时间：" + (endTree - startTree) );    //输出程序运行时间
		EncryNode encryNode = new EncryNode();
		long startEncryTime = System.currentTimeMillis();    //获取开始时间
		tree.Node_to_EncryNode(tree.getRoot(),encryNode);
		long endEncryTime = System.currentTimeMillis();    //获取结束时间
		EncryPBRQTree encryTree = new EncryPBRQTree();
		encryTree.setRoot(encryNode);
		//EncryPBRQTree encryTree = new EncryPBRQTree(encryNode,tree.getKeyLength(),tree.getKeyNum());
		encryTree.EncryDrawTree(encryNode);
		System.out.println("加密树的时间：" + (endEncryTime - startEncryTime) );    //输出程序运行时间
		
long startSave = System.currentTimeMillis();    //获取开始时间
		
		File file = new File("F:\\proper\\"+"EncryTree.out");
		File filekey = new File("F:\\proper\\"+"key.out");
        ObjectOutputStream oout;
        ObjectOutputStream okey;
		try {
			oout = new ObjectOutputStream(new FileOutputStream(file));
			okey = new ObjectOutputStream(new FileOutputStream(filekey));
			KeyInfo keyInfo =tree.getKeyInfo();
			oout.writeObject(encryTree);
			okey.writeObject(keyInfo);
	        oout.close();
	        okey.close();
	        System.out.println("加密树保存成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endSave = System.currentTimeMillis();    //获取结束时间
		System.out.println("保存文件时间：" + (endSave - startSave) + "ms");    //输出程序运行时间
	}
	
	@Test
	public void TestEncryTree() {
		long startTime = System.currentTimeMillis();    //获取开始时间
		
		File file = new File("F:\\proper\\"+"EncryTree.out");
		File filekey = new File("F:\\proper\\"+"key.out");
        ObjectOutputStream oout;
        ObjectOutputStream okey;
		try {
			oout = new ObjectOutputStream(new FileOutputStream(file));
			okey = new ObjectOutputStream(new FileOutputStream(filekey));
			QuadTree tree = build();
			KeyInfo keyInfo =tree.getKeyInfo();
			EncryNode encryNode = new EncryNode();
			EncryPBRQTree encryTree = 
					new EncryPBRQTree(tree.Node_to_EncryNode(tree.getRoot(),encryNode),tree.getKeyInfo().getTreeKeyLength(),tree.getKeyInfo().getTreekeyNum());
			encryTree.EncryDrawTree(encryTree.getRoot());
			oout.writeObject(encryTree);
			okey.writeObject(keyInfo);
	        oout.close();
	        okey.close();
	        System.out.println("加密树保存成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
/*
        ObjectInputStream oin;
		try {	
			oin = new ObjectInputStream(new FileInputStream("F:\\proper\\key.out"));
			KeyInfo kk = (KeyInfo) oin.readObject();
			System.out.println("getDataEncryType:"+kk.getDataEncryType());
	        oin.close();
	        System.out.println("加密树读取成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        */
	}
	
	@Test
	public void PreGetSearchResult() {
		QuadTree tree = build();
		EncryNode encryNode = new EncryNode();
		tree.Node_to_EncryNode(tree.getRoot(),encryNode);
		EncryPBRQTree encryTree = new EncryPBRQTree();
		encryTree.setRoot(encryNode);
		System.out.println("===========开始查找===========");
		Point center = new Point(27.320000,-81.367043);
		double R = 1000;
		LinkedList<Data> rangeList = tree.LBS_Square(tree.getRoot(), center, R);
		if (rangeList.size() == 0)
			System.out.println("没有该范围的数据！！！");
		Iterator<Data> it = rangeList.iterator();
		while (it.hasNext()) {
			Data tt = it.next();
			System.out.println("结果：" + tt.getStr());
			System.out.println("解码后的结果" + tt.DeepPointDecoding(tt.getStr()));
		}
		System.out.println("结果数目：" + rangeList.size());
	}

}
