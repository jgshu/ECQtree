package com.user.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.PBRQ.util.hmac;

public class LocationRangeSearch {

	private User user = new User();

	public LocationRangeSearch() {
//		this.user = new User();
	}

	public LocationRangeSearch(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 根据经纬度和半径计算经纬度范围
	 *
	 * @param raidus
	 *            单位米
	 * @return minLat, minLng, maxLat, maxLng
	 */
	public double[] getAround(double lat, double lon, double raidus) {

		Double latitude = lat;
		Double longitude = lon;

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

	public HashSet getPrefix(double R) {
		Point tmp = this.user.getUserlocation();
		double pp[] = getAround(tmp.getX(), tmp.getX(), R);
		Point start = new Point(pp[0], pp[1]);
		Point end = new Point(pp[2], pp[3]);
		System.out.println("Start:" + start.toString());
		System.out.println("End:" + end.toString());
		return ConvertToPrefix(start, end);
	}

//	public HashSet ConvertPrefix(Point start, Point end) {
//		String ss = start.PositionEncodeByDeep(start);
//		String ee = end.PositionEncodeByDeep(end);
//		HashSet<String> prefix = new HashSet<String>();
//		HashMap<String, Integer> hash = new HashMap<String, Integer>();
//		hash.put("00", 0);
//		hash.put("01", 1);
//		hash.put("10", 2);
//		hash.put("11", 3);
//		for (int i = 0; i < ss.length(); i += 2) {
//			if (hash.get(ss.substring(i, i + 2)) != hash.get(ee.substring(i, i + 2))) {
//				// 先把这一个串的前缀放进去，再把介于这两个串之间的前缀放进去
//				prefix.add(ss.substring(0, i + 2));
//				prefix.add(ee.substring(0, i + 2));
//
//				if (hash.get("00") < hash.get(ss.substring(i, i + 2))
//						&& hash.get("00") > hash.get(ee.substring(i, i + 2))) {
//					prefix.add(ss.substring(0, i) + "00");
//				}
//				if (hash.get("01") < hash.get(ss.substring(i, i + 2))
//						&& hash.get("01") > hash.get(ee.substring(i, i + 2))) {
//					prefix.add(ss.substring(0, i) + "01");
//				}
//				if (hash.get("10") < hash.get(ss.substring(i, i + 2))
//						&& hash.get("10") > hash.get(ee.substring(i, i + 2))) {
//					prefix.add(ss.substring(0, i) + "10");
//				}
//				if (hash.get("11") < hash.get(ss.substring(i, i + 2))
//						&& hash.get("11") > hash.get(ee.substring(i, i + 2))) {
//					prefix.add(ss.substring(0, i) + "11");
//				}
//			} else if (hash.get(ss.substring(i, i + 2)) == hash.get(ee.substring(i, i + 2))) {
//				// 如果相等，则只要记录其中一个就可以了
//				prefix.add(ss.substring(0, i));
//			}
//		}
//		return prefix;
//	}

	public HashSet ConvertToPrefix(Point start, Point end) {
		if (start.compareTo(end)==1) {//start>end,交换位置
			String ss = start.PositionEncodeByDeep(end);
			String ee = end.PositionEncodeByDeep(start);
		}
		String ss = start.PositionEncodeByDeep(start);
		String ee = end.PositionEncodeByDeep(end);
		System.out.println("start:" + ss);
		System.out.println("end :" + ee);
		HashSet<String> prefix = new HashSet<String>();
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("00", 0);
		hash.put("01", 1);
		hash.put("10", 2);
		hash.put("11", 3);
		LinkedList<String> tmp = new LinkedList<String>();
		for (int i = 0; i < ss.length(); i += 2) {
			// 选出符合大小范围的前缀
			if (hash.get(ss.substring(i, i + 2)) != hash.get(ee.substring(i, i + 2))) {
				// 先把这一个串的前缀放进去，再把介于这两个串之间的前缀放进去
				// prefix.add(ss.substring(0, i + 2));
				// prefix.add(ee.substring(0, i + 2));
				tmp.add(ss.substring(i, i + 2));
				tmp.add(ee.substring(i, i + 2));

				if (hash.get(ss.substring(i, i + 2)) < hash.get("00")
						&& hash.get("00") < hash.get(ee.substring(i, i + 2))) {// 如果00在start和end前缀范围内，例如，10是在01与11之间
					// prefix.add("00");
					tmp.add("00");
				}
				if (hash.get(ss.substring(i, i + 2)) < hash.get("01")
						&& hash.get("01") < hash.get(ee.substring(i, i + 2))) {// 如果01在start和end前缀范围内，例如，10是在01与11之间
					// prefix.add("01");
					tmp.add("01");
				}
				if (hash.get(ss.substring(i, i + 2)) < hash.get("10")
						&& hash.get("10") < hash.get(ee.substring(i, i + 2))) {// 如果10在start和end前缀范围内，例如，10是在01与11之间
					// prefix.add("10");
					tmp.add("10");
				}
				if (hash.get(ss.substring(i, i + 2)) < hash.get("11")
						&& hash.get("11") < hash.get(ee.substring(i, i + 2))) {// 如果11在start和end前缀范围内，例如，10是在01与11之间
					// prefix.add("11");
					tmp.add("11");
				}
			} else if (hash.get(ss.substring(i, i + 2)) == hash.get(ee.substring(i, i + 2))) {
				// 如果相等，则只要记录其中一个就可以了
				// prefix.add(ss.substring(0, i));
				tmp.add(ss.substring(i, i + 2));
			}
			// 把符合的前缀与其上一层进行组合
			
			if (i == 0) {
				Iterator<String> tmpIt = tmp.iterator();
				while (tmpIt.hasNext()) {
					String tt = tmpIt.next();
					prefix.add(tt);
				}
			} else {
				Iterator<String> iterator = prefix.iterator();
				HashSet<String> cur = new HashSet<String>();
				while (iterator.hasNext()) {
					String sss = iterator.next();
					if (sss.length() == i) {
						Iterator<String> tmpIt = tmp.iterator();
						while (tmpIt.hasNext()) {
							String tt = tmpIt.next();
							cur.add(sss + tt);
						}
					}
				}
				prefix.addAll(cur);
			}
			tmp.clear();
		}
//		System.out.println("prefix length:"+prefix.size());
		return prefix;
	}

	public LinkedList<LinkedList<String>> EncryPrefix(HashSet<String> prefix,LinkedList<String> treekey) {
//		LinkedList<String> key = this.user.getKeyInfo().getTreeKey();
		LinkedList<LinkedList<String>> result = new LinkedList<LinkedList<String>>();

		Iterator<String> iterator = prefix.iterator();
		while (iterator.hasNext()) {
			String pp = iterator.next();
			LinkedList<String> tmp = new LinkedList<String>();
			Iterator<String> itkey = treekey.iterator();
			while (itkey.hasNext()) {
				tmp.add(hmac.hamcsha1(pp, itkey.next()));
			}
			result.add(tmp);
		}
		return result;
	}

	/*
	 * 将数据dd解密成明文
	 */
	public String DecryDataList(String dd, SecretKey dataKey, String dataEncryType) {
		// 加密：
		String result = null;
		Cipher cp;
		try {
			cp = Cipher.getInstance(dataEncryType);// 创建密码器
			cp.init(Cipher.DECRYPT_MODE, dataKey); // 初始化
			// byte[] ptext = toByteArray(dd);//把LinkedList<Data>对象转变成字节流，然后进行加密
			byte[] ptext = dd.getBytes();

			byte[] ctext = cp.doFinal(ptext); // 加密
			// System.out.println("ctext:"+ctext);
			return ctext.toString();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public LinkedList<Data> DecodeDataList(LinkedList<String> dd, SecretKey dataKey, String dataEncryType) {
		Iterator<String> iterator = dd.iterator();
		LinkedList<Data> result = new LinkedList<Data>();
		;
		while (iterator.hasNext()) {
			String tmp = iterator.next();
			String out = DecryDataList(tmp, dataKey, dataEncryType);
			Data oout = (Data) toObject(out.getBytes());
			result.add(oout);
		}
		return result;
	}

	/**
	 * 对象转数组
	 * 
	 * @param obj
	 * @return
	 */
	public byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 数组转对象
	 * 
	 * @param bytes
	 * @return
	 */
	public Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
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

}
