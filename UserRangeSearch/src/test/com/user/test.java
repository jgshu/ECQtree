package test.com.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import com.PBRQ.tree.KeyInfo;
import com.user.search.LocationRangeSearch;
import com.user.search.User;

public class test {

	@SuppressWarnings("rawtypes")
	@Test
	public void TestLocationSearch() {
		LocationRangeSearch lRangeSearch = new LocationRangeSearch(new User(27.320000, -81.367043));
		// LocationRangeSearch lRangeSearch= new LocationRangeSearch();
		HashSet prefix = lRangeSearch.getPrefix(1);
		System.out.println(prefix.size());
		Iterator<String> iterator = prefix.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

	}

	@Test
	public void TestEncryPrefix() {
		User tmp = new User(27.320000,-81.367043);
		System.out.println(tmp.getKeyInfo().toString());
		LocationRangeSearch lRangeSearch = new LocationRangeSearch(tmp);
		HashSet prefix = lRangeSearch.getPrefix(10000);
		System.out.println("prefix:"+prefix.size());
		tmp.getKeyInfo().Read("F:\\proper\\key.out");
		LinkedList<LinkedList<String>> result = lRangeSearch.EncryPrefix(prefix,tmp.getKeyInfo().getTreeKey());
		System.out.println(result.size());
		
		File file = new File("F:\\proper\\" + "EncrySearchWord.out");
		ObjectOutputStream oout;
		try {
			oout = new ObjectOutputStream(new FileOutputStream(file));
			oout.writeObject(result);
			oout.close();
			System.out.println("加密搜索前缀保存成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void TestKeyInfoSeri() {
		ObjectInputStream okey;
		try {
			okey = new ObjectInputStream(new FileInputStream("F:\\proper\\key.out"));
			KeyInfo keyInfo = (KeyInfo) okey.readObject();
			System.out.println("getDataEncryType:" + keyInfo.getDataEncryType());
			okey.close();
			System.out.println("加密树读取成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void Read() {
		ObjectInputStream okey;
		try {
			okey = new ObjectInputStream(new FileInputStream("F:\\proper\\key.out"));
			Object keyInfo = okey.readObject();
			if (keyInfo instanceof KeyInfo) {
				KeyInfo keyInfo2 = (KeyInfo) keyInfo;
				System.out.println("getDataKeyLength:" + keyInfo2.getDataKeyLength());
			}
			okey.close();
			System.out.println("加密树读取成功！！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("密钥读取成功！！");
	}
}
