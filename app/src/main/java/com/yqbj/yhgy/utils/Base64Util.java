package com.yqbj.yhgy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName:  Base64Util   
 * @Description:标准Base64加密解密
 * @author: lsc
 * @date: 2018年7月12日 上午11:15:40     
 * @Copyright:Copyright Taikanglife.All Rights Reserved
 *  注意：本内容仅限于泰康保险集团内部传阅，禁止外泄以及用于其他的商业目
 */
public class Base64Util {
	/**第62和63个字符，不同的base64这两个字符不同*/
	private static char char62='+';
	/**第62和63个字符，不同的base64这两个字符不同*/
	private static char char63='/';
	/**
	 * 基础的BASE64
	 */
	private static char[] infp = {
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
		'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
		'0','1','2','3','4','5','6','7','8','9',
		char62,char63
	};
	
	/** 是否需要每N个字符添加回车换行	 */
	public static final boolean NEED_ADD_ENTER_CODE = true;
	/** 每76个字符添加一个回车换行	 */
	public static final int ENTERCOUNT = 76;
	/**	分隔符的类型 */
	public static final String ENTER = "";
	/** byte掩码，保存完整byte */
	public static final int MASK_FLOW_8 = 0x00000000000000ff;//1个int为8个byte，一个byte为8位，即2^8,由于一个16进制数可以代表2^4，所以1byte为2个16进制数
	/** byte掩码，保存byte的后六位 */
	public static final int MASK_FLOW_6 = 0x000000000000003f;//1个int为8个byte，一个byte为8位，即2^8,由于一个16进制数可以代表2^4，所以1byte为2个16进制数
	/**
	 * 字符与byte的转换,基础的64个字符会被替换成其下标值，而其他的所有字符都会转化为0
	 * @param c
	 * @return
	 */
	private static byte transS2B(char c){
		byte result = 0;
		if(c>='A'&&c<='Z'){
			result = (byte)(c - 'A' );
			return result;
		}
		if(c>='a'&&c<='z'){
			result = (byte)(c- 'a' + 26);
			return result;
		}
		if(c>='0'&&c<='9'){
			result = (byte)(c- '0' + 52);
			return result;
		}
		if(c==char62)return 62;
		if(c==char63)return 63;
		return result;
	}
	
	/**
	 * 对byte[]数组进行base64加密，结果为字符串，每76个字符填充一个\n
	 * @param message
	 * @return
	 */
	public static String encodeMessage(byte[] message){
		//准备字符串StringBuilder
		StringBuilder sb = new StringBuilder();
		int message_byte_length = message.length;
		//计算后部补充的字节的数量
		int end = 3 - message_byte_length % 3;
		if(end == 3) end = 0;
		//实际被操作的数组
		byte[] operate_message_byte = null;
		if(end >0){
			//如果发现需要在末尾补字节，则生成实际的操作数组
			operate_message_byte = new byte[message_byte_length + end];
			System.arraycopy(message, 0, operate_message_byte, 0, message_byte_length);
		}else{
			//如果发现不需要在末尾补字节，则操作数组就是原byte数组
			operate_message_byte = message;
		}
		//循环处理
		//1 int = 8位
		//每读操作数据中的3个字节，生成四个char数组插入stringBuilder
		int line_count = 1;//字符总数的计数，每76个字符增加一个换行符
		for(int i=0,j=operate_message_byte.length/3;i<j;i++){
			int i_byte_real = i*3;
			//[1 123456	  |2         ]		把第一个字节的123456位转化为字符，放入sb
			sb.append(infp[operate_message_byte[i_byte_real]>>2 & MASK_FLOW_6 ]);
			//[1       78 |2 1234    ]		把第一个字节的78，第二个字节的1234位转化为字符，放入sb
			sb.append(infp[((operate_message_byte[i_byte_real]<<4 & 0x30 ) | (operate_message_byte[i_byte_real+1]>>>4 & 0xf))& MASK_FLOW_6 ]);
			//[2     5678 |3 12      ]		把第二个字节的5678，第三个字节的12位转化为字符，放入sb
			//如果操作数据中的倒数第二位不存在（在原数据中不存在），则直接跳出，不做任何处理（无数据则不写）
			if(i_byte_real+1>message_byte_length-1){break;}
			sb.append(infp[((operate_message_byte[i_byte_real+1]<<2 & 0x3C) | (operate_message_byte[i_byte_real+2]>>>6 & 0x3 ))& MASK_FLOW_6 ]);
			//[3   345678 |          ]		把第三个字节的345678位转化为字符，放入sb
			//如果操作数据中的倒数第一位不存在（在原数据中不存在），则直接跳出，不做任何处理（无数据则不写）
			if(i_byte_real+2>message_byte_length-1){break;}
			sb.append(infp[operate_message_byte[i_byte_real+2] &  MASK_FLOW_6 ]);
			//如果发现已经是76个字符的倍数，则插入一个换行符
			if(NEED_ADD_ENTER_CODE && ((line_count+3)%ENTERCOUNT == 0) ){sb.append(ENTER);}
			line_count +=4;
		}
		//添加末尾的等号，如果end为2则添加两个=，如果end为1则添加一个=
		if(end==2){
			sb.append("==");
		}else if(end == 1){
			sb.append('=');
		}else{}
		
		return sb.toString();
	}
	
	/**
	 * 将String的message转化为去除回车换行的字符串
	 * @param message
	 * @return
	 */
	private static char[] clearStringMessage(String message){
		char[] result = null;
		char[] message_chararray = message.toCharArray();
		int message_chararray_length = message_chararray.length;
		int controlCharCount = 0;//去除控制字符回车换行
		//计算控制字符的数量
		for(int i=0;i<message_chararray_length;i++){
			if(message_chararray[i]=='\r'||message_chararray[i]=='\n'){
				controlCharCount++;
			}
		}
		//用控制字符数量计算出最终char数组的长度
		result = new char[message_chararray_length - controlCharCount];
		//生成最终的char数组,i为原char数组的下标,j为新char数组的下标
		for(int i=0,j=0;i<message_chararray_length;i++){
			if(message_chararray[i]!='\r'&&message_chararray[i]!='\n'){
				result[j]=message_chararray[i];
				j++;
			}
		}
		return result;
	}
	/**
	 * 对base64加密后的字符串进行解密，解密结果为byte数组
	 * @param message
	 * @return
	 */
	public static byte[] decodeMessage(String message){
		//去除message中的控制字符并且转化为char数组
		char[] message_char = clearStringMessage(message);
		//计算虚拟的byte数组的长度(带等号的、补0byte的长度)
		int length = message_char.length / 4 * 3 ;
		//计算最终结果的长度，realLength为message字符串中的真实长度(不带等号的，去除0byte的长度)
		int realLength = 0;
		if(message_char[message_char.length-2] == '='){
			realLength = length - 2;
		}else if(message_char[message_char.length-1] == '='){
			realLength = length - 1;
		}else{
			realLength = length;
		}
		
		//生成最终结果数据，
		byte[] result = new byte[realLength];
		//对byse64数组进行拆分，并插入result
		for(int i=0 ; i<length/3;i++){
			//base64的char数组的真实下标
			int i_base64_real = i*4;
			//byte数组的真实下标
			int i_byte___real = i*3;
			//将四个字符转化为其下标，如果是=则转化为0
			byte b1 = transS2B(message_char[i_base64_real]);		//1	00123456		一个byte为8位
			byte b2 = transS2B(message_char[i_base64_real+1]);		//2	00123456		一个byte为8位
			byte b3 = transS2B(message_char[i_base64_real+2]);		//3	00123456		一个byte为8位
			byte b4 = transS2B(message_char[i_base64_real+3]);		//4	00123456		一个byte为8位
			//进行合并，把4个byte值转化为3个byte值
			byte bb1 = (byte)((b1<<2 & 0xfc) | (b2 >>4 & 0x03) &  MASK_FLOW_8 );//1 123456  |  2       12
			byte bb2 = (byte)((b2<<4 & 0xf0) | (b3 >>2 & 0x0f) &  MASK_FLOW_8 );//2 3456    |  3     1234
			byte bb3 = (byte)((b3<<6 & 0xc0) | (b4     & 0x3f) &  MASK_FLOW_8 );//3 56      |  4   123456
			//对byte数组赋值
			//第一个byte
			result[ i_byte___real ] = bb1;
			//第二个byte，如果在最终的result中没有该byte的位置（即该byte为补0的情况），则跳出
			if( (i_byte___real+1) >= realLength ){break;}
			result[i_byte___real+1] = bb2;
			//第三个byte，如果在最终的result中没有该byte的位置（即该byte为补0的情况），则跳出
			if( (i_byte___real+2) >= realLength ){break;}
			result[i_byte___real+2] = bb3;
		}
		return result;
	}
	
	

	public static  String readTxtFile(String filePath){
		String content="";
        try {        	
        	String encoding="gb2312";
            File file=new File(filePath);          
            FileInputStream in = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(in,encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
            	content=content+lineTxt;
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 	      
		return content;
    }
	
	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
		//long start = System.currentTimeMillis();
		int count = 1;
		//String target = readTxtFile("d:\\yi_test\\yibaoxian2.txt");
		//String ss="JTdCJTIyYWdlbnRDb2RlJTIyJTNBJTIyYXV0b2hvbWUlMjIlMkMlMjJjaGVja1RpbWUlMjIlM0ElMjIyMDE1LTAyLTAyKzE1JTNBNDclM0ExOSUyMiUyQyUyMmRlbGl2ZXJ5SW5mbyUyMiUzQSU3QiUyMmFkZHJlc3MlMjIlM0ElMjIlRTYlQkYlODklRTYlQkElQUElRTglQjclQUYlMjIlMkMlMjJjaXR5JTIyJTNBJTIyJUU1JTkwJTg4JUU4JTgyJUE1JUU1JUI4JTgyJTIyJTJDJTIyY291bnR5JTIyJTNBJTIyJUU1JUJBJTkwJUU5JTk4JUIzJUU1JThDJUJBJTIyJTJDJTIycHJvdmluY2UlMjIlM0ElMjIlRTUlQUUlODklRTUlQkUlQkQlRTclOUMlODElMjIlMkMlMjJyZWNpcGllbnQlMjIlM0ElMjIlRTYlOUMlQjElRTYlOTUlOEYlMjIlMkMlMjJyZWNpcGllbnRQaG9uZSUyMiUzQSUyMjE4NjU1MDU2OTE5JTIyJTdEJTJDJTIyaW5zdXJlQ29tcGFueUxvZ28lMjIlM0ElMjJodHRwJTNBJTJGJTJGd3d3LnlpYmFveGlhbi5jb20lMkZpbWFnZXMlMkZsb2dvX1RQLmpwZyUyMiUyQyUyMmluc3VyZURldGFpbCUyMiUzQSU1QiU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJmb3JjZUZsYWclMjIlMkMlMjJpbnN1cmVOYW1lJTIyJTNBJTIyJUU2JTk4JUFGJUU1JTkwJUE2JUU2JThBJTk1JUU0JUJGJTlEJUU0JUJBJUE0JUU1JUJDJUJBJUU5JTk5JUE5JTIyJTJDJTIyaW5zdXJlVmFsdWUlMjIlM0ExJTJDJTIycHJlbWl1bSUyMiUzQTAlN0QlMkMlN0IlMjJpbnN1cmVDb2RlJTIyJTNBJTIyY292XzIxMCUyMiUyQyUyMmluc3VyZU5hbWUlMjIlM0ElMjIlRTglQkQlQTYlRTglQkElQUIlRTUlODglOTIlRTclOTclOTUlRTklOTklQTklMjIlMkMlMjJpbnN1cmVWYWx1ZSUyMiUzQTAlMkMlMjJwcmVtaXVtJTIyJTNBMCU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJjb3ZfMjkwJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFNSU4RiU5MSVFNSU4QSVBOCVFNiU5QyVCQSVFNiVCNiU4OSVFNiVCMCVCNCVFOSU5OSVBOSUyMiUyQyUyMmluc3VyZVZhbHVlJTIyJTNBMCUyQyUyMnByZW1pdW0lMjIlM0EtNzM0NCU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJjb3ZfNzAyJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFOCVCRCVBNiVFNCVCOCU4QSVFNCVCQSVCQSVFNSU5MSU5OCVFOCVCNCVBMyVFNCVCQiVCQiVFOSU5OSVBOSVFRiVCQyU4OCVFNCVCOSU5OCVFNSVBRSVBMiVFRiVCQyU4OSUyMiUyQyUyMmluc3VyZVZhbHVlJTIyJTNBMTAwMDAlMkMlMjJwcmVtaXVtJTIyJTNBMTI0NDklN0QlMkMlN0IlMjJpbnN1cmVDb2RlJTIyJTNBJTIyZm9yY2VQcmVtaXVtJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFNCVCQSVBNCVFNSVCQyVCQSVFOSU5OSVBOSUyMiUyQyUyMnByZW1pdW0lMjIlM0E4ODAwMCU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJjb3ZfNjAwJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFNyVBQyVBQyVFNCVCOCU4OSVFOCU4MCU4NSVFOCVCNCVBMyVFNCVCQiVCQiVFOSU5OSVBOSUyMiUyQyUyMmluc3VyZVZhbHVlJTIyJTNBNTAwMDAwJTJDJTIycHJlbWl1bSUyMiUzQTEyNzI5NiU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJjb3ZfNzAxJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFOCVCRCVBNiVFNCVCOCU4QSVFNCVCQSVCQSVFNSU5MSU5OCVFOCVCNCVBMyVFNCVCQiVCQiVFOSU5OSVBOSVFRiVCQyU4OCVFNSU4RiVCOCVFNiU5QyVCQSVFRiVCQyU4OSUyMiUyQyUyMmluc3VyZVZhbHVlJTIyJTNBMTAwMDAlMkMlMjJwcmVtaXVtJTIyJTNBMTc3OCU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJ2ZWhpY2xlVGF4UHJlbWl1bSUyMiUyQyUyMmluc3VyZU5hbWUlMjIlM0ElMjIlRTglQkQlQTYlRTglODglQjklRTclQTglOEUlMjIlMkMlMjJwcmVtaXVtJTIyJTNBMzAwMDAlN0QlMkMlN0IlMjJpbnN1cmVDb2RlJTIyJTNBJTIyY292XzIzMSUyMiUyQyUyMmluc3VyZU5hbWUlMjIlM0ElMjIlRTclOEUlQkIlRTclOTIlODMlRTUlOEQlOTUlRTclOEIlQUMlRTclQTAlQjQlRTclQTIlOEUlRTklOTklQTklMjIlMkMlMjJpbnN1cmVWYWx1ZSUyMiUzQTElMkMlMjJwcmVtaXVtJTIyJTNBMjcxMCU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJjb3ZfMzEwJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFOCU4NyVBQSVFNyU4NyU4MyVFOSU5OSVBOSUyMiUyQyUyMmluc3VyZVZhbHVlJTIyJTNBMCUyQyUyMnByZW1pdW0lMjIlM0EtNDUxNyU3RCUyQyU3QiUyMmluc3VyZUNvZGUlMjIlM0ElMjJjb3ZfNTAwJTIyJTJDJTIyaW5zdXJlTmFtZSUyMiUzQSUyMiVFNSU4NSVBOCVFOCVCRCVBNiVFNyU5QiU5NyVFNiU4QSVBMiVFOSU5OSVBOSUyMiUyQyUyMmluc3VyZVZhbHVlJTIyJTNBMSUyQyUyMnByZW1pdW0lMjIlM0E3NjY5JTdEJTJDJTdCJTIyaW5zdXJlQ29kZSUyMiUzQSUyMmNvdl8yMDAlMjIlMkMlMjJpbnN1cmVOYW1lJTIyJTNBJTIyJUU4JUJEJUE2JUU4JUJFJTg2JUU2JThEJTlGJUU1JUE0JUIxJUU5JTk5JUE5JTIyJTJDJTIyaW5zdXJlVmFsdWUlMjIlM0ExJTJDJTIycHJlbWl1bSUyMiUzQTg2NzY4JTdEJTJDJTdCJTIyaW5zdXJlQ29kZSUyMiUzQSUyMmNvdl85MDAlMjIlMkMlMjJpbnN1cmVOYW1lJTIyJTNBJTIyJUU0JUI4JThEJUU4JUFFJUExJUU1JTg1JThEJUU4JUI1JTk0JUU2JTgwJUJCJUU0JUJCJUI3JUU2JUEwJUJDJTIyJTJDJTIyaW5zdXJlVmFsdWUlMjIlM0EwJTJDJTIycHJlbWl1bSUyMiUzQTAlN0QlNUQlMkMlMjJpbnN1cmVJbmZvJTIyJTNBJTdCJTIyYml6QmVnaW5EYXRlJTIyJTNBJTIyMjAxNS0wNC0xMCUyMiUyQyUyMmJpekZsYWclMjIlM0ElMjIxJTIyJTJDJTIyYml6UHJvcG9zYWxObyUyMiUzQSUyMjEwMDAxMDgwMDAwOTA1MTY4NTUlMjIlMkMlMjJiaXpUb3RhbFByZW1pdW0lMjIlM0EyMjY4MDklMkMlMjJmb3JjZUJlZ2luRGF0ZSUyMiUzQSUyMjIwMTUtMDQtMTAlMjIlMkMlMjJmb3JjZUZsYWclMjIlM0ElMjIxJTIyJTJDJTIyZm9yY2VQcm9wb3NhbE5vJTIyJTNBJTIyMTAwMDEwNTAwMDA5MDUxNjg1OCUyMiUyQyUyMmZvcmNlVG90YWxQcmVtaXVtJTIyJTNBMTE4MDAwJTdEJTJDJTIybWFya2V0UHJpY2UlMjIlM0EzODQ4MDklMkMlMjJtcGF5VXJsJTIyJTNBJTIyaHR0cCUzQSUyRiUyRmNoZXhpYW4uYXhhdHAuY29tJTJGcGF5UmVxdWVzdEluaXQuZG8lM0ZlY0luc3VyZUlkJTNEMTA4NTIwMTUwMjAyMDU5MDcxNTglMjZzb3VyY2VUeXBlJTNEd2VDaGF0JTIyJTJDJTIyb3JkZXJObyUyMiUzQSUyMjIwMTUwMjAyMTUzNDA0MTM0MDAxNiUyMiUyQyUyMm9yZGVyUHJpY2UlMjIlM0EzNDQ4MDklMkMlMjJvcmRlclN0YXR1cyUyMiUzQTMlMkMlMjJvcmRlclRpbWUlMjIlM0ElMjIyMDE1LTAyLTAyKzE1JTNBMzQlM0EwNCUyMiUyQyUyMnBheVVybCUyMiUzQSUyMmh0dHAlM0ElMkYlMkZjaGV4aWFuLmF4YXRwLmNvbSUyRnBheVJlcXVlc3RJbml0LmRvJTNGZWNJbnN1cmVJZCUzRDEwODUyMDE1MDIwMjA1OTA3MTU4JTIyJTJDJTIycmVsYXRlZFBlcnNvbnMlMjIlM0ElNUIlN0IlMjJlbWFpbCUyMiUzQSUyMiUyMiUyQyUyMmlkQ29kZSUyMiUzQSUyMjM0MjQyMTE5NzMwNzIwMDQxNyUyMiUyQyUyMmlkVHlwZSUyMiUzQTElMkMlMjJuYW1lJTIyJTNBJTIyJUU2JTlDJUIxJUU2JTk1JThGJTIyJTJDJTIycGhvbmUlMjIlM0ElMjIxODY1NTA1NjkxOSUyMiUyQyUyMnJlbGF0ZWRUeXBlJTIyJTNBMCU3RCUyQyU3QiUyMmVtYWlsJTIyJTNBJTIyJTIyJTJDJTIyaWRDb2RlJTIyJTNBJTIyMzQyNDIxMTk3MzA3MjAwNDE3JTIyJTJDJTIyaWRUeXBlJTIyJTNBMSUyQyUyMm5hbWUlMjIlM0ElMjIlRTYlOUMlQjElRTYlOTUlOEYlMjIlMkMlMjJwaG9uZSUyMiUzQSUyMjE4NjU1MDU2OTE5JTIyJTJDJTIycmVsYXRlZFR5cGUlMjIlM0EyJTdEJTJDJTdCJTIyZW1haWwlMjIlM0ElMjIlMjIlMkMlMjJpZENvZGUlMjIlM0ElMjIzNDI0MjExOTczMDcyMDA0MTclMjIlMkMlMjJpZFR5cGUlMjIlM0ExJTJDJTIybmFtZSUyMiUzQSUyMiVFNiU5QyVCMSVFNiU5NSU4RiUyMiUyQyUyMnBob25lJTIyJTNBJTIyMTg2NTUwNTY5MTklMjIlMkMlMjJyZWxhdGVkVHlwZSUyMiUzQTElN0QlNUQlMkMlMjJ0YnNuJTIyJTNBJTIyMTIzNDUlMjIlMkMlMjJ2ZWhpY2xlSW5mbyUyMiUzQSU3QiUyMmNpdHlDb2RlJTIyJTNBJTIyMzQwMTAwJTIyJTJDJTIyZW5naW5lTm8lMjIlM0ElMjIyMDQ2QzMwMyUyMiUyQyUyMmZyYW1lTm8lMjIlM0ElMjJMQlY1UzMxMDJFU0oyNjY0NCUyMiUyQyUyMmxpY2Vuc2VObyUyMiUzQSUyMiVFNyU5QSU5NkFXN0Q3NyUyMiUyQyUyMm5ld0NhckZsYWclMjIlM0ExJTJDJTIyb3duZXJCaXJ0aGRheSUyMiUzQSUyMjE5NzMtMDctMjAlMjIlMkMlMjJvd25lcklkTm8lMjIlM0ElMjIzNDI0MjExOTczMDcyMDA0MTclMjIlMkMlMjJvd25lck1vYmlsZSUyMiUzQSUyMjE4NjU1MDU2OTE5JTIyJTJDJTIyb3duZXJOYW1lJTIyJTNBJTIyJUU2JTlDJUIxJUU2JTk1JThGJTIyJTJDJTIycmVnaXN0ZXJEYXRlJTIyJTNBJTIyMjAxNC0wMi0yNSUyMiUyQyUyMnNwZWNpYWxDYXJGbGFnJTIyJTNBMSUyQyUyMnZlaGljbGVDb2RlJTIyJTNBMjIwODc4JTJDJTIydmVoaWNsZURlc2MlMjIlM0ElMjIlRTUlQUUlOUQlRTklQTklQUNCTVc3MjAxU0wlMjhCTVc1MjVMaSUyOSVFOCVCRCVCRiVFOCVCRCVBNisrKysyMDE0JUU2JUFDJUJFKzUyNUxpJUU5JUEyJTg2JUU1JTg1JTg4JUU1JTlFJThCKys1JUU1JUJBJUE3JTVDdTAwQTAlNUN1MDBBMCVFNSU4RiU4MiVFOCU4MCU4MyVFNCVCQiVCNyUyODQyMDAwMCUyOSUyMiUyQyUyMnZlaGljbGVOYW1lJTIyJTNBJTIyJUU1JUFFJTlEJUU5JUE5JUFDQk1XNzIwMVNMJTI4Qk1XNTI1TGklMjklRTglQkQlQkYlRTglQkQlQTYlMjIlN0QlN0Q=";

		String target = "京N0DS76";
		for(int i=0;i<count;i++){
			//target =URLEncoder.encode(target,"UTF-8");
			byte [] tmp = target.getBytes("UTF-8");
			String enStr = encodeMessage(tmp);
			enStr="N0DS76";
			tmp = decodeMessage(enStr);
			String deStr = new String(tmp, "utf-8");
			//String ouStr = new String(decodeMessage(enStr),"UTF-8");
			//System.out.println(ouStr);
			//ouStr =URLDecoder.decode(ouStr,"UTF-8");
			//System.out.println(ouStr);
			//System.out.println(ouStr.equals(target));
		}
		//long end = System.currentTimeMillis();
		//System.out.println(end);
		//System.out.println("耗时"+(end-start));
		//用于jProfile性能测试
		Thread.sleep(10000000);
	}
}
	
