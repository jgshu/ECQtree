package com.PBRQ.util;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;

public class BloomFilter implements Serializable{  
    //DEFAULT_SIZEΪ2��22�η������˴�������28λ  
	private static int DEFAULT_SIZE = 2<<21;  
    
    /* 
     * ��ʼ��һ��������С��λ�� 
     * BitSetʵ�����ɡ�������λ�����ɵ�һ��Vector�� 
     * ����ϣ����Ч�ʵر�������������ء���Ϣ����Ӧʹ��BitSet. 
     */  
    private BitSet bitSets = new BitSet(DEFAULT_SIZE);  
  //����hash��������  
    private SimpleHash hashFuns = new SimpleHash(DEFAULT_SIZE,61);  
    
    private static LinkedList<LinkedList<String>> prefix;

    public BloomFilter(LinkedList<LinkedList<String>> input ,int elemCount){  
    	this.DEFAULT_SIZE=elemCount* input.getFirst().getFirst().getBytes().length;
        prefix = input;
    }  
    /** 
     *  
     * ��������add 
     * ���������������ַ�����ǵ�bitSets�У��������ַ�����8������ֵ��λ��Ϊ1 
     * @param value 
     */  
    public synchronized void add(LinkedList<String > valueList){  
    	Iterator<String> iterator = valueList.iterator();
    	while (iterator.hasNext()) {
    		bitSets.set(hashFuns.hash(iterator.next()), true);
    	}
    }  
    /** 
     *  
     * ��������isExit 
     * �������жϸ������ַ����Ƿ��Ѿ�������bloofilter�У�������ڷ���true�������ڷ���false 
     * @param value 
     * @return 
     */  
    public synchronized boolean isExit(LinkedList<String> valueList){  
        //�жϴ����ֵ�Ƿ�Ϊnull  
        if(null == valueList){  
            return false;  
        }  
        
        Iterator<String> iterator = valueList.iterator();
    	while (iterator.hasNext()) {
    		if (bitSets.get(hashFuns.hash(iterator.next()))) {
    			//����ж�t��hash����ֵ����һ��λ�ò����ڼ����ж�Ϊ������Bloofilter��  
    			return false;
    		}
    	}
        return true;  
    }  
    
//
//    /** 
//     *  
//     * ��������init 
//     * ��������ȡ�����ļ� 
//     */  
//    public void init(){  
//        File file = new File(path);  
//        FileInputStream in = null;  
//        try {  
//            in = new FileInputStream(file);  
//            read(in);  
//        }catch(Exception e){  
//            e.printStackTrace();  
//        }finally{  
//            try {  
//                if(in!=null){  
//                    in.close();  
//                    in = null;  
//                }             
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//        }  
//    }  
//
//    /** 
//     *  
//     * ��������read 
//     * ���������ݴ����������ʼ��bloomfilter 
//     * @param in 
//     */  
//    private void read(InputStream in){  
//        if(null == in){ //���inΪnull���򷵻�  
//            return;  
//        }  
//
//        InputStreamReader reader = null;  
//
//        try {  
//            //����������  
//            reader = new InputStreamReader(in, "UTF-8");  
//            BufferedReader buffReader = new BufferedReader(reader, 512);  
//            String theWord = null;            
//            do {  
//                theWord = buffReader.readLine();  
//                //���theWord��Ϊnull�Ϳգ������Bloomfilter��  
//                if(theWord!=null && !theWord.trim().equals("")){  
//                    add(theWord);  
//                }  
//
//            } while (theWord != null);  
//
//        } catch (IOException e){  
//            e.printStackTrace();  
//        } finally{  
//            //�ر���  
//            try {  
//                if(reader != null){  
//                    reader.close();  
//                    reader = null;  
//                }                 
//                if(in != null){  
//                    in.close();  
//                    in = null;  
//                }  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//
//        }  
//    }  

    public static class SimpleHash {  
        /* 
         * capΪDEFAULT_SIZE�������ڽ���Ľϴ��ַ�����ֵ 
         * seedΪ����hashֵ��һ��keyֵ�������Ӧ�����е�seeds���� 
         */  
        private int cap;  
        private int seed;  

        public SimpleHash(int cap, int seed){  
            this.cap = cap;  
            this.seed = seed;  
        }  
        /** 
         *  
         * ��������hash 
         * ����������hash�ĺ������û�����ѡ���������õ�hash���� 
         * @param value 
         * @return 
         */  
        public int hash(String value){  
            int result = 0;  
            int length = value.length();  
            for(int i=0; i<length; i++){  
                result = seed*result + value.charAt(i);  
            }  

            return (cap-1) & result;  
        }  
    }  

}  