package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import arvore.Arvore;
import hashtable.TabelaHash;



public class Serializacao 
{
	
	public static void serializar(TabelaHash tabela, String nomeArquivo)
	{
		try 
		{
		
			FileOutputStream arquivo = new FileOutputStream(nomeArquivo); // criando arquivo
			ObjectOutputStream objeto = new ObjectOutputStream(arquivo);
			objeto.writeObject(tabela); // escrevendo no objeto
			objeto.flush();
			
			arquivo.close();
			objeto.close();
			
			System.out.println("Serializacao feita com sucesso!");
		} catch (Exception e) 
		{
			// TODO: handle exception
			System.out.println("Nao foi possivel serializar o objeto!");
		}
		
	}
	
	public static TabelaHash deserializar(String nomeArquivo)
	{
		try 
		{
			
			TabelaHash tabela;
			FileInputStream arquivo = new FileInputStream(nomeArquivo); 
			ObjectInputStream objeto = new ObjectInputStream(arquivo);
			tabela = (TabelaHash) objeto.readObject();
			
			arquivo.close();
			objeto.close();
			
			System.out.println("Deserializacao feita com sucesso!");
			
			return tabela;
		} catch (Exception e) 
		{
			// TODO: handle exception
			System.out.println("Nao foi possivel deserializar o objeto!");
			return null;
			
		}
		
	}
		
		public static void serializar(Arvore arv, String nomeArquivo)
		{
			try 
			{
			
				FileOutputStream arquivo = new FileOutputStream(nomeArquivo); // criando arquivo
				ObjectOutputStream objeto = new ObjectOutputStream(arquivo);
				objeto.writeObject(arv); // escrevendo no objeto
				objeto.flush();
				
				arquivo.close();
				objeto.close();
				
				System.out.println("Serializacao feita com sucesso!");
			} catch (Exception e) 
			{
				// TODO: handle exception
				System.out.println("Nao foi possivel serializar o objeto!");
			}
			
		}
		
		public static Arvore deserializarArvore(String nomeArquivo)
		{
			try 
			{
				
				Arvore arv;
				FileInputStream arquivo = new FileInputStream(nomeArquivo); 
				ObjectInputStream objeto = new ObjectInputStream(arquivo);
				arv = (Arvore) objeto.readObject();
				
				arquivo.close();
				objeto.close();
				
				System.out.println("Deserializacao feita com sucesso!");
				
				return arv;
			} catch (Exception e) 
			{
				// TODO: handle exception
				System.out.println("Nao foi possivel deserializar o objeto!");
				return null;
				
			}
		
	}
	

}
