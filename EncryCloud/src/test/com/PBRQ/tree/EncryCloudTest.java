package test.com.PBRQ.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import com.Encry.PBRQ.tree.EncryPBRQTree;

public class EncryCloudTest {
	
	@Test
	public void TestEncryTree() {
		File file = new File("F:\\proper\\"+"EncryTree.out");
        ObjectInputStream oin;
		try {
			oin = new ObjectInputStream(new FileInputStream(file));
			EncryPBRQTree newTree =(EncryPBRQTree)oin.readObject(); // 强制转换到EncryPBRQTree类型
			System.out.println(newTree.getRoot().getChild_4().getData());
	        oin.close();
	        System.out.println("加密树保存成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
	}
	
	@Test
	public void TestEncryTreeSearch() {
		File file = new File("F:\\proper\\"+"EncryTree.out");
		File keyword = new File("F:\\proper\\"+"EncrySearchWord.out");
		
		EncryPBRQTree encryTree =null;
		LinkedList<LinkedList<String>> encrykeyword =null;
		
        ObjectInputStream oin;
        ObjectInputStream okeyword;
		try {
			oin = new ObjectInputStream(new FileInputStream(file));
			okeyword = new ObjectInputStream(new FileInputStream(keyword));
			
			encryTree =(EncryPBRQTree)oin.readObject(); // 强制转换到EncryPBRQTree类型
			encrykeyword = (LinkedList<LinkedList<String>>)okeyword.readObject();
			
	        oin.close();
	        okeyword.close();
	        System.out.println("加密树保存成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();    //获取开始时间
		LinkedList<String >result = encryTree.TreeSearch(encrykeyword);
		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println("搜索时间时间：" + (endTime - startTime) );    //输出程序运行时间
		System.out.println("搜索结果大小"+result.size());
	}
}
