package arvore;
import java.io.Serializable;
import java.math.BigInteger;

public class Elemento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public BigInteger chave;
	public String valorChave;
	public Pagina esq;
	public Pagina dir;
	public long deslocamento;

	public Elemento(String chave, long deslocamento)
	{
		this.chave = new BigInteger(chave);
		valorChave = chave;
		this.deslocamento = deslocamento;
	}
		
}
