package test.com.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import com.user.search.LocationRangeSearch;
import com.user.search.User;

public class UserDataTest {

	@Test
	public void TestPrefixTime() {
		LocationRangeSearch lRangeSearch= new LocationRangeSearch(new User(27.320000,-81.367043));
		long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		HashSet prefix = lRangeSearch.getPrefix(10000);
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println("��������ʱ�䣺" + (endTime - startTime) );    //�����������ʱ��
//		LocationRangeSearch lRangeSearch= new LocationRangeSearch(new User(27.320000,-81.367043));
//		//LocationRangeSearch lRangeSearch= new LocationRangeSearch();
//		HashSet prefix = lRangeSearch.getPrefix();
//		System.out.println(prefix.size());
//		Iterator<String> iterator = prefix.iterator();
//		while (iterator.hasNext()) {
//			System.out.println(iterator.next());
//		}
	}
	
	//ǰ׺����t��HMAC��ʱ���
	@Test
	public void TestEncryPrefix() {
		User tmp = new User(27.320000,-81.367043);
		System.out.println(tmp.getKeyInfo().toString());
		LocationRangeSearch lRangeSearch = new LocationRangeSearch(tmp);
		long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		HashSet prefix = lRangeSearch.getPrefix(10000);
		System.out.println("prefix:"+prefix.size());
		tmp.getKeyInfo().Read("F:\\proper\\key.out");
		LinkedList<LinkedList<String>> result = lRangeSearch.EncryPrefix(prefix,tmp.getKeyInfo().getTreeKey());
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println(result.size());
		System.out.println("ǰ׺����t��HMAC���õ�ʱ�䣺" + (endTime - startTime) );    //�����������ʱ��
		
		File file = new File("F:\\proper\\" + "EncrySearchWord.out");
		ObjectOutputStream oout;
		try {
			oout = new ObjectOutputStream(new FileOutputStream(file));
			oout.writeObject(result);
			oout.close();
			System.out.println("��������ǰ׺����ɹ�����");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
