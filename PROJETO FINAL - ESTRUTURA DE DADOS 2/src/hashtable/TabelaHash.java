package hashtable;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import arquivo.Arquivo;
import util.Serializacao;

public class TabelaHash implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	List<List<Hash>> tabela = new LinkedList<List<Hash>>();	
	private int m; //dimensao da tabela
	private static int exclusoes = 0;
	
	public TabelaHash(int m)
	{
		this.m = m;
		
		for (int i = 0; i < m; i++) 
		{
			List<Hash> lista = new LinkedList<>();
			tabela.add(lista);
		}
	}

	public List<List<Hash>> getTabela() 
	{
		return tabela;
	}
	
	public List<Hash> getLista(int indiceTabela)
	{
		return tabela.get(indiceTabela);
	}
	
	// retorna o indice numa lista cujo conteudo corresponde a chave, caso não ache a chave retorna -1
	public int indice(List<Hash> lista, String chave)
	{
		
		for (int i = 0; i <lista.size() ; i++) 
		{
			if(lista.get(i).getChave().compareTo(new BigInteger(chave)) == 0)
			{
				
				return i;
			}
		
		}
		
		return -1;
	}
	
	public void setTabela(List<List<Hash>> tabela)
	{
		this.tabela = tabela;
	}
	

	public int divisao(String chave)
	{
		BigInteger c = new BigInteger(chave);
		//metodo da divisao	
		BigInteger[] indice = c.divideAndRemainder(new BigInteger(Integer.toString(m)));
		
		return indice[1].intValue();

	}
	
	public boolean chaveExiste(int indiceTabela, String chave)
	{	
		//crio uma referencia da lista que esta na tabela
		List<Hash> lista = tabela.get(indiceTabela);
		
		for (int j = 0; j < lista.size(); j++) 
		{
			if(lista.get(j).getChave().compareTo(new BigInteger(chave)) == 0)
			{ 
				//chave já existe
				//System.out.println("Chave:  "+chave+" ja existe na tabela, nao vou inserir");
				return true;
			}
		}
		
		// chave nao existe 
		return false;
	}
		
	public void inserir(Hash hash)
	{
		BigInteger chave = hash.getChave();
		int indice = divisao(chave.toString());

		
		if(!chaveExiste(indice,chave.toString()))
		{
			//crio uma referencia da uma listas que estão na tabela 
			List<Hash> lista = tabela.get(indice);
			lista.add(hash);
			
		}
		
	}
	
	public void remover(String chave)
	{
		int indice = divisao(chave);
		//crio uma referencia da lista que esta na tabela
		
		//crio uma referencia da uma listas que estão na tabela 
		List<Hash> lista = tabela.get(indice);
		
		for (int i = 0; i < lista.size(); i++) 
		{
			if(lista.get(i).getChave().compareTo(new BigInteger(chave)) == 0)
			{	
				Hash h1 = lista.get(i);
				lista.remove(i);
				//exclusoes++;
				
				//if(exclusoes > 1)
				//{
					Arquivo.excluirRegistro("clientes.bin", h1.getdeslocamento());
				
					//Arquivo.atualizarArquivo("clientes.bin");
				/*}
				else
				{
					Arquivo.excluirRegistro("clientes.bin", h1.getdeslocamento());
				
				}*/
			}
			
		}
		
	}

	public void imprimirTabela()
	{
		System.out.println();
		System.out.println("Imprimindo tabela hash final:");
		
		for (int i = 0; i < tabela.size(); i++) 
		{
			System.out.println("elementos da posicao "+i+" da tabela:");
			for (int j = 0; j < tabela.get(i).size(); j++) 
			{
				if(!tabela.get(i).isEmpty())
				{	
					System.out.println("Chave: "+tabela.get(i).get(j).getChave());
					System.out.println("Deslocamento: "+tabela.get(i).get(j).getdeslocamento());
				}
				
			}
			
			
		}
		
	}
}
