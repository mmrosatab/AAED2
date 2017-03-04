package hashtable;

import java.io.Serializable;
import java.math.BigInteger;

public class Hash implements Serializable
{	
	private BigInteger chave;
	private long deslocamento;
	
	public Hash(String chave, long deslocamento) 
	{
		this.chave = new BigInteger(chave);
		this.deslocamento = deslocamento;
	}
	
	public Hash(BigInteger chave) 
	{
		this.chave = chave;
	}
	
	public BigInteger getChave() 
	{
		return chave;
	}
	
	public void setChave(BigInteger chave) 
	{
		this.chave = chave;
	}
	
	public long getdeslocamento() 
	{
		return deslocamento;
	}
	
	public void setdeslocamento(long deslocamento) 
	{
		this.deslocamento = deslocamento;
	}

}
