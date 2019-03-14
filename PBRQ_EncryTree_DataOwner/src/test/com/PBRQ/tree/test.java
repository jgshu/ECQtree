package test.com.PBRQ.tree;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import org.junit.Test;

import com.Encry.PBRQ.tree.EncryNode;
import com.Encry.PBRQ.tree.EncryPBRQTree;
import com.PBRQ.tree.Data;
import com.PBRQ.tree.Entity;
import com.PBRQ.tree.KeyInfo;
import com.PBRQ.tree.Node;
import com.PBRQ.tree.Point;
import com.PBRQ.tree.QuadTree;
import com.PBRQ.util.ReadData;

public class test {

	public QuadTree build() {
		List<Entity> PList = new ArrayList<Entity>();
		String resourcePath = "E:\\workspace\\eclipse-workspace\\PBRQ_EncryTree_DataOwner\\src\\test\\resources\\points.txt";
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
	public void TestInsert() {
		
		long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		QuadTree tree = build();
		
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��

		System.out.println("��������ʱ�䣺" + (endTime - startTime) + "ms");    //�����������ʱ��
//		System.out.println("���Ľڵ�����" + tree.getCount());
//		System.out.println("�����в���Ľڵ���=" + tree.SumTreeNode(tree.getRoot()));
	}

	@Test
	public void TestSquareRangSearch() {
		QuadTree qTree = build();
		Data start = new Data("1001001001001110101100100111001001001111", "12345");
		Data end = new Data("1100011110011000101100001110111001000011", "45678");
		LinkedList<Data> rangeList = qTree.SquareRangeSearch(qTree.getRoot(), start, end);
		if (rangeList.size() == 0) {
			System.out.println("û�и÷�Χ�����ݣ�����");
		} else {
			Iterator<Data> it = rangeList.iterator();
			while (it.hasNext()) {
				Data tt = it.next();
				System.out.println("�����" + tt.getStr());
			}
			System.out.println("�����Ŀ��" + rangeList.size());
		}
		System.out.println("��������������");
	}

	@Test
	public void TestLBS_Square() {
		QuadTree qTree = build();
		System.out.println("===========��ʼ����===========");
		Point center = new Point(27.320000,-81.367043);
		double R = 1000;
		LinkedList<Data> rangeList = qTree.LBS_Square(qTree.getRoot(), center, R);
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
	/*
	@Test
	public void TestDecode() {
		List<Coordinate> PList = new ArrayList<Coordinate>();
		String resourcePath = "E:\\workspace\\eclipse-workspace\\PBRQ-Tree\\src\\test\\resources\\beijing.csv";
		ReadData rd = new ReadData();
		rd.LoadPointsFromFile(resourcePath);
		PList = rd.get_pointList();
		System.out.println("���صĽڵ�����" + PList.size());
		QuadTree qTree = new QuadTree();
		qTree.CreateQuadTree();
		System.out.println("=========��ʼ����==========");
		int flag = 0;
		HashMap<String, Integer> isContain = new HashMap<String, Integer>();
		for (int i = 0; i < PList.size() && flag < 3; i++) {
			Point point = new Point();
			System.out.println("ԭʼ���꣺" + PList.get(i));
			point.setStr(PList.get(i));
			System.out.println("str=" + point.getStr());
			System.out.println("����Ϊ��" + point.XYDecoding(point.getStr()).toString());
			flag++;
		}
	}
*/
	/*
	 * ��ӡ��һ��LinkedList<Data>���е�����
	 */
	public void printList(LinkedList<Data> temp) {
		// System.out.println("==============��ʼ��ӡ============");
		Iterator<Data> iterator = temp.iterator();
		while (iterator.hasNext()) {
			Data data = iterator.next();
			System.out.println(data.getStr());
		}
		// System.out.println("==============������ӡ============");
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

	@Test
	public void TestDrawTree() throws IOException {
		QuadTree qTree = build();
		 Node root = qTree.getRoot();
		 System.out.println("==============��ʼ��ӡ��============");
		 DrawTree(root);
	}
	/*
	 * @Test public void TestHMAC() { String result = hmac.hamcsha1("123456",
	 * "12345678"); System.out.println(result); }
	 * 
	 * @Test public void TestEncrpTree() { QuadTree qTree = build();
	 * qTree.setKey("123456"); System.out.println("===========��ʼ����===========");
	 * Coordinate center = new Coordinate(113.7632, 33.45048, "֣�����Ͻ�ɽ·"); double R =
	 * 1000; LinkedList<Point> rangeList = qTree.LBS_Square(qTree.getRoot(), center,
	 * R,true); if (rangeList.size() == 0) System.out.println("û�и÷�Χ�����ݣ�����");
	 * Iterator<Point> it = rangeList.iterator(); while (it.hasNext()) { Point tt =
	 * it.next(); System.out.println("�����"+tt.getStr());
	 * //System.out.println("�����Ľ��"+tt.XYDecoding(tt.getStr())); }
	 * System.out.println("�����Ŀ��" + rangeList.size());
	 * 
	 * }
	 */
	
	@Test
	public void TestSerializable() throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File("QuadTree.out");
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));
        QuadTree tree = build();
        oout.writeObject(tree);
        oout.close();

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
        QuadTree newTree = (QuadTree) oin.readObject(); // û��ǿ��ת����Person����
        oin.close();
        System.out.println(newTree.getCount());

	}
	
	@Test
	public void TestEncrySerializable() {
		KeyGenerator kg = null;
        try {
            //ָ���㷨,����ΪDES
            kg = KeyGenerator.getInstance("DES", "SunJCE");

            //ָ����Կ����,����Խ��,����ǿ��Խ��
            kg.init(56);

            //������Կ
            Key key = kg.generateKey();
            System.out.println(key.toString());

            //�ǵð���Կ��������
            String keyfilename="key.dat";
            
            ObjectOutputStream out = new ObjectOutputStream(new
                BufferedOutputStream(new FileOutputStream(keyfilename)));
            out.writeObject(key);
            out.close();

            //����Ҫ��Cipher��ʵ��
            Cipher cipher = Cipher.getInstance("DES");

            String filename = "���ܵĶ���.dat";
            //���벢�����ļ�
            try {
                //���ü���ģʽ
                cipher.init(Cipher.ENCRYPT_MODE, key);
                //�����
                out = new ObjectOutputStream(new CipherOutputStream(new
                    BufferedOutputStream(new FileOutputStream(filename)),
                    cipher));
                QuadTree tree =  build();
                out.writeObject(tree);
                
                out.close();
                System.out.println("������ɣ�");
            }
            catch (Exception ey5) {
                System.out.println("Error when encrypt the file");
                System.exit(0);
            }

            try {
                //��key���ļ�ȡ��
                ObjectInputStream in =new ObjectInputStream(new
                    BufferedInputStream(new FileInputStream(keyfilename)));
                key=(Key)in.readObject();
                in.close();
                
                //���ý���ģʽ
               cipher.init(Cipher.DECRYPT_MODE, key);
                //������
                in =new ObjectInputStream(new CipherInputStream(new
                    BufferedInputStream(
                        new FileInputStream(filename)), cipher));

                QuadTree a=(QuadTree)in.readObject();
                System.out.println(a.getCount());
                in.close();
                System.out.println("������ɣ�");
            }
            catch (Exception ey5) {
                System.out.println("Error when encrypt the file");
                System.exit(0);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
        
	}
	
	@Test
	public void TestQuadToEncry() {
		QuadTree tree = build();
		//tree.DrawTree(tree.getRoot());
		EncryNode encryNode = new EncryNode();
		tree.Node_to_EncryNode(tree.getRoot(),encryNode);
		EncryPBRQTree encryTree = new EncryPBRQTree();
		encryTree.setRoot(encryNode);
		//EncryPBRQTree encryTree = new EncryPBRQTree(encryNode,tree.getKeyLength(),tree.getKeyNum());
		encryTree.EncryDrawTree(encryNode);
	}
	
	@Test
	public void TestByteToArray() {
		QuadTree tree = build();
		tree.printList(tree.getRoot().getChild_4().getData());
		System.out.println("========================");
		byte[] bytes = tree.toByteArray(tree.getRoot().getChild_4().getData());
		System.out.println(bytes);
		System.out.println(((LinkedList<Data>)tree.toObject(bytes)).getLast().getStr());
	}

}
