package util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Conversao 
{
	
	public static int[] charInt(String[] array)
	{
		
		int[] aux = new int[array.length];
		
		for(int i=0;i<array.length;i++)
		{
			aux[i] = Integer.parseInt(array[i]);
		}
		
		return aux;
	}
	
	public static byte[] stringByte(String conteudo)
	{
		
		char[] arrayChar = conteudo.toCharArray();
		byte[] arrayByte = new byte[2*arrayChar.length];
		
		for (int i = 0; i < arrayChar.length; i++) 
		{
            int posicao = i * 2;
            arrayByte[posicao] = (byte) ((arrayChar[i]&0xFF00)>>8);
            arrayByte[posicao+1] = (byte) ((arrayChar[i]&0x00FF));
		}
		
		return arrayByte;
	}
	
	public static String byteString(byte []arrayByte)
	{
		
		String nome;
		char[] charArray = new char[arrayByte.length];
		
		for (int i = 0; i < arrayByte.length; i++) {
			charArray[i] = (char) arrayByte[i];
		}
		
		nome = charArray.toString();
		
		return nome;
	}
	
	public static String byteString2(byte []arrayByte) throws UnsupportedEncodingException
	{
		
		String string = new String(arrayByte,"UTF-16");
		
		return string;
	}
	
	
	public static int byteArrayToInt(byte[] array)
	{
		int num = ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).getInt();
		return num;
		
	}
	
	public static byte[] intToByteArray(int num)
	{
		byte[] array = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(num).array();
		return array;
	}
	
	 public static byte [] booleanToByte(boolean bool)
	 {
		 byte[] b = new byte[1];

		 if(bool)
			 b[0] = (byte)1;
		 else
			 b[0] = (byte)0;

		 return b;
	 }

	 public static boolean byteToBoolean(byte[] b)
	 {
		 if(b[0] == (byte)1)
		 {
			 return true;
		 }
		 return false;
	 }

	

}
