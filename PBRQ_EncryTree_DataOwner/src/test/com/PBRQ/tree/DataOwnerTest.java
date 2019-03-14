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
		System.out.println("���صĽڵ�����" + PList.size());
		QuadTree qTree = new QuadTree();
		System.out.println("=========��ʼ����==========");
		HashMap<String, Integer> isContain = new HashMap<String, Integer>();
		for (int i = 0; i < PList.size(); i++) {
			Data data = new Data(PList.get(i));
			if (isContain.containsKey(data.getStr())) {
				// System.out.println("���ظ��ģ���=" + point.getStr());
			} else {
				isContain.put(data.getStr(), 1);
			}
			qTree.Insert(qTree.getRoot(), data);
		}
		System.out.println("=========�������==========");
		return qTree;
	}
	
	@Test
	public void TestCreatTime() {
		long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		QuadTree tree = build();
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println("��������ʱ�䣺" + (endTime - startTime) );    //�����������ʱ��
	}
	
	@Test
	public void TestTreeEncry() {
		long startTree = System.currentTimeMillis();    //��ȡ��ʼʱ��
		QuadTree tree = build();
		long endTree = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println("��������ʱ�䣺" + (endTree - startTree) );    //�����������ʱ��
		EncryNode encryNode = new EncryNode();
		long startEncryTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		tree.Node_to_EncryNode(tree.getRoot(),encryNode);
		long endEncryTime = System.currentTimeMillis();    //��ȡ����ʱ��
		EncryPBRQTree encryTree = new EncryPBRQTree();
		encryTree.setRoot(encryNode);
		//EncryPBRQTree encryTree = new EncryPBRQTree(encryNode,tree.getKeyLength(),tree.getKeyNum());
		encryTree.EncryDrawTree(encryNode);
		System.out.println("��������ʱ�䣺" + (endEncryTime - startEncryTime) );    //�����������ʱ��
		
long startSave = System.currentTimeMillis();    //��ȡ��ʼʱ��
		
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
	        System.out.println("����������ɹ�����");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endSave = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println("�����ļ�ʱ�䣺" + (endSave - startSave) + "ms");    //�����������ʱ��
	}
	
	@Test
	public void TestEncryTree() {
		long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		
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
	        System.out.println("����������ɹ�����");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println("��������ʱ�䣺" + (endTime - startTime) + "ms");    //�����������ʱ��
/*
        ObjectInputStream oin;
		try {	
			oin = new ObjectInputStream(new FileInputStream("F:\\proper\\key.out"));
			KeyInfo kk = (KeyInfo) oin.readObject();
			System.out.println("getDataEncryType:"+kk.getDataEncryType());
	        oin.close();
	        System.out.println("��������ȡ�ɹ�����");
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
		System.out.println("===========��ʼ����===========");
		Point center = new Point(27.320000,-81.367043);
		double R = 1000;
		LinkedList<Data> rangeList = tree.LBS_Square(tree.getRoot(), center, R);
		if (rangeList.size() == 0)
			System.out.println("û�и÷�Χ�����ݣ�����");
		Iterator<Data> it = rangeList.iterator();
		while (it.hasNext()) {
			Data tt = it.next();
			System.out.println("�����" + tt.getStr());
			System.out.println("�����Ľ��" + tt.DeepPointDecoding(tt.getStr()));
		}
		System.out.println("�����Ŀ��" + rangeList.size());
	}

}
