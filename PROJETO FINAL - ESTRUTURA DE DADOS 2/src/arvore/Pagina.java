package arvore;
import java.io.Serializable;

public class Pagina implements Serializable{

	private static final long serialVersionUID = 1L;
	public Elemento [] elementos;
	public int tamanho;
	private boolean folha;
	private Pagina pai;
	
	public Pagina (int d)
	{
		this.elementos = new Elemento[((2*d)+1)];
		tamanho = 0;
	}
	
	public boolean isFolha()
	{
		return folha;
	}
	public void setFolha(boolean folha)
	{
		this.folha = folha;
	}

	public Pagina getPai() {
		return pai;
	}

	public void setPai(Pagina pai) {
		this.pai = pai;
	}
	
	
}
