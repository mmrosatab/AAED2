package arvore;

import java.io.Serializable;
import java.math.BigInteger;

public class Ordenador implements Serializable
{	

	public void mergesort(Elemento [] vetor, int inicio, int fim)
	{

		if(inicio < fim)
		{

			int meio = (inicio+fim)/2;
			mergesort(vetor, inicio, meio);
			mergesort(vetor, meio+1, fim);
			intercala(vetor, inicio, fim);
		}
	}	

	public static int busca(Elemento [] v, BigInteger x, int inicio, int fim)
	{
		int meio, i = -1;

		if(inicio < fim)
		{
			 meio = (inicio+fim)/2;
			if(v[meio].chave.compareTo(x) == 0)
				return meio;
			else if(v[meio].chave.compareTo(x) == 1)
				i = busca(v, x,inicio, meio);
			else
				i = busca(v, x,meio+1, fim);
		}

		return i;
	}
	
	public int buscaSeq(Elemento[] v,BigInteger chave,int tamPag) 
	{
		int i = -1;
		
		for (i = 0; i < tamPag; i++) 
		{
			if((v[i].chave.compareTo(chave) == 1) && (i < tamPag - 1))
			{
				return i+1;
			}
			
			if(i == tamPag - 1)
			{
				return i;
			}
			
		}
		
		return i;
	}
	
	private void intercala(Elemento [] vetor, int inicio, int fim)
	{
		int i, j;
		Elemento aux;
		
		for(i = inicio+1; i < fim; i++)
		{
			aux = vetor[i];
			j = i;

			while(j>0 && aux.chave.compareTo(vetor[j-1].chave) == -1){

				vetor[j] = vetor[j-1];
				j--;
			}

			vetor[j] = aux;
		}
	}
	
	public int compara(String pri, String sec){
		char[] priInChar = pri.toCharArray();
		char[] secInChar = sec.toCharArray();
		int cont = 0;		
		
		if(priInChar.length < secInChar.length){
			
			for(cont = 0; cont < priInChar.length;cont++){
				
				if((int)priInChar[cont] < (int)secInChar[cont]){
					return -1;
				}
				else if((int)priInChar[cont] > (int)secInChar[cont]){
					return 1;
				}		
				
			}
		}
		else{
			
			for(cont = 0; cont < secInChar.length;cont++){
				if((int)priInChar[cont] < (int)secInChar[cont]){
					return -1;
				}
				else if((int)priInChar[cont] > (int)secInChar[cont]){
					return 1;
				}				
			}
		}
		
		return 0;
	}
	
	
	public void arrasta(Elemento [] vetor, int inicio, int fim){
		
		int i = 0;
		for(int j = inicio+1; j < fim; j++)
		{
			vetor[i] = vetor[j];
		}
		
	}
	
}

	
