package com.PBRQ.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.PBRQ.tree.Entity;
import com.PBRQ.tree.Point;

public class ReadData implements Serializable{
	static List<Entity> _pointList = new ArrayList<Entity>();
	
	@Test
	public void testFile() {
		//_pointList = new ArrayList<Point>();
		// TODO Auto-generated method stub
//		URL classpathResource = Thread.currentThread().getContextClassLoader().getResource("");
//        String resourcePath = classpathResource.getPath()+"beijing.csv";
		String resourcePath ="E:\\workspace\\eclipse-workspace\\PBRQ_EncryTree_DataOwner\\src\\test\\resources\\points.txt";
		List<Entity> result = LoadPointsFromFile(resourcePath);
		System.out.println(result.toString());
	}
	
	public List<Entity> LoadPointsFromFile(String source) {
		String [] item;
        String[] lines = readAllTextFileLines(source);
        for (String line : lines) {
            item = line.split(",");
            //System.out.println(item.toString());
//            Point tmp = new Point(Double.parseDouble(item[3]), Double.parseDouble(item[4]), item[2]);
//            System.out.println( tmp.toString());
            //经度，维度，地点名称字符串
            Entity tmp=new Entity(Double.parseDouble(item[1]), Double.parseDouble(item[2]), item[0]);
            _pointList.add(tmp);
        }
        return _pointList;
	}
	
	private static String[] readAllTextFileLines(String fileName) {
        StringBuilder sb = new StringBuilder();

        try {
            String textLine;
            
            FileInputStream in = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            
            int flag=0;

            while ((textLine = br.readLine()) !=null ) {
            	sb.append(textLine);
                sb.append('\n');
//            	System.out.println("textLine:"+textLine);
//            	String [] tmp = textLine.split(",");
//            	System.out.println("temp[2]:"+tmp[2]);//地点名称如"花莲市复兴街102号"
//            	System.out.println("temp[3]:"+tmp[3]);//经度
//            	System.out.println("temp[4]:"+tmp[4]);//维度
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (sb.length() == 0)
                sb.append("\n");
        }
        return sb.toString().split("\n");
    }

	public static List<Entity> get_pointList() {
		return _pointList;
	}

	public static void set_pointList(List<Entity> _pointList) {
		ReadData._pointList = _pointList;
	}	
	
	
}
