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
		long startTime = System.currentTimeMillis();    //获取开始时间
		HashSet prefix = lRangeSearch.getPrefix(10000);
		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) );    //输出程序运行时间
//		LocationRangeSearch lRangeSearch= new LocationRangeSearch(new User(27.320000,-81.367043));
//		//LocationRangeSearch lRangeSearch= new LocationRangeSearch();
//		HashSet prefix = lRangeSearch.getPrefix();
//		System.out.println(prefix.size());
//		Iterator<String> iterator = prefix.iterator();
//		while (iterator.hasNext()) {
//			System.out.println(iterator.next());
//		}
	}
	
	//前缀化和t次HMAC的时间和
	@Test
	public void TestEncryPrefix() {
		User tmp = new User(27.320000,-81.367043);
		System.out.println(tmp.getKeyInfo().toString());
		LocationRangeSearch lRangeSearch = new LocationRangeSearch(tmp);
		long startTime = System.currentTimeMillis();    //获取开始时间
		HashSet prefix = lRangeSearch.getPrefix(10000);
		System.out.println("prefix:"+prefix.size());
		tmp.getKeyInfo().Read("F:\\proper\\key.out");
		LinkedList<LinkedList<String>> result = lRangeSearch.EncryPrefix(prefix,tmp.getKeyInfo().getTreeKey());
		long endTime = System.currentTimeMillis();    //获取结束时间
		System.out.println(result.size());
		System.out.println("前缀化和t次HMAC所用的时间：" + (endTime - startTime) );    //输出程序运行时间
		
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
	
}
