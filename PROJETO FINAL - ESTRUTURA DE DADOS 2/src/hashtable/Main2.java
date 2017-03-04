package hashtable;

import java.math.BigInteger;

public class Main2
{
	public static void main(String[] args)
	{
		BigInteger c = new BigInteger("3");
		
		BigInteger[] indice = c.divideAndRemainder(new BigInteger("2"));	
		
		System.out.println(indice[0]);
		System.out.println(indice[1].intValue());
		
	}
}
