package arvore;
import java.io.Serializable;
import java.math.BigInteger;
public class Arvore implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int d;
	private Pagina raiz;
	private Ordenador ordenador = new Ordenador();
	
	public Arvore(int ordem)
	{
		d = ordem;
		raiz = new Pagina(ordem);
	}
	
	public int getOrdem()
	{
		return d;
	}
	
	public Pagina getRaiz()
	{
		return raiz;
	}
	public void setRaiz(Pagina raiz)
	{
		this.raiz = raiz;
	}
	
	public boolean ehFolha(Pagina pag)
	{
		boolean folha = false;
		
		for(int i = 0; i < pag.tamanho; i++)
		{
			if(pag.elementos[i].esq != null || pag.elementos[i].dir != null)
				folha = false;
			else
				folha = true;
		}
		
		return folha;
	}
	
	/* BUSCA QUE APARECE NA MAIN */
	public Pagina busca(String chave)
	{
		BigInteger key = new BigInteger(chave);
		return buscaAuxiliar(this.raiz, key, false);
	}
	
	private Pagina buscaAuxiliar(Pagina raiz, BigInteger chave, boolean insercao)
	{
		int i;
		
		for(i = 0; i < raiz.tamanho; i++)
		{
			if(raiz.elementos[i] != null)
			{
				if(raiz.elementos[i].chave.compareTo(chave) == 1)
				{
					if(raiz.elementos[i].esq != null)
					{
						raiz = buscaAuxiliar(raiz.elementos[i].esq, chave,insercao);
						break;
					}
					
					else
					return raiz;
				}
			
				else if(raiz.elementos[i].chave.compareTo(chave) == -1)
				continue;
				
				else if(raiz.elementos[i].chave.compareTo(chave) == 0  && insercao == true)
				return null;
				
				else if(raiz.elementos[i].chave.compareTo(chave) == 0 && insercao == false)
				return raiz;
			}
		}
		
		if(i > 0)
		{
			// verifica se n�o est� acessando �ndice indevido
			if(i <= raiz.tamanho)
			{
				if(raiz.elementos[i-1].dir != null)
					raiz = buscaAuxiliar(raiz.elementos[i-1].dir, chave,insercao);
			}
			
			else
				return raiz;
		}
		
		return raiz;
	}
	
	public void inserir(String chave, long deslocamento)
	{
		Elemento elemento = new Elemento(chave, deslocamento);
		//verifica se o elemento ja existe, caso nao exista retorna a
		// pagina que o elemento deve ser inserida
		Pagina pagina = buscaAuxiliar(this.raiz,elemento.chave,true);
		
		try
		{
			// insere elemento possivel null pointer exception
			pagina.elementos[pagina.tamanho] = elemento;
			pagina.tamanho++;
			
			// ordena
			ordenador.mergesort(pagina.elementos, 0, pagina.tamanho);
			
			if(pagina.tamanho == (2*getOrdem() + 1))
			{
				//cisao
				cisao(pagina);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Elemento já existe");
		}
		
	}
	
	public void remover(String chave)
	{
		Elemento elemento = new Elemento(chave,0); /* ATENÇÃO, PODE GERAR ERROS */
		Pagina pagina = buscaAuxiliar(this.raiz,elemento.chave,false);
		
		/* VERIFICA SE A PAGINA TEM MAIS QUE O VALOR MINIMO DE ELEMENTOS
		 * CASO MAIS SIMPLES DE REMOÇÃO */
		
		if(pagina != null)
		{
			if(pagina.tamanho > this.d && ehFolha(pagina))
			{
				// INDICE DO ELEMENTO A SER REMOVIDO
				int posicao = this.ordenador.busca(pagina.elementos, elemento.chave, 0, pagina.tamanho);
				
				if(pagina.elementos[posicao].esq == null && pagina.elementos[posicao].dir == null)
				{
					if(pagina.tamanho > this.d)
					{
						pagina.elementos[posicao] = null;
						pagina.tamanho = pagina.tamanho - 1;
					}
				}
			}
			else if(pagina.tamanho == this.d && ehFolha(pagina))
			{
				// alteracoes
				// pega o indice do primeiro elemento seguinte ao pai do elemento que sera excluido
				int prox = ordenador.buscaSeq(pagina.getPai().elementos, new BigInteger(chave), pagina.getPai().tamanho);
				
				// pega o indice do elemento a ser removido
				int indice = ordenador.busca(pagina.elementos, new BigInteger(chave), 0, pagina.tamanho);
				
				if(redistribuir2(pagina,prox,indice) == false)
				{
					concatenar(pagina,prox,indice);
				}
				
			}
			
			else
			{
				/* REMOÇÃO NA RAIZ OU NO MEIO DA ÁRVORE */
				
				System.out.println("REMOÇÃO NA MEIÚCA");
				// indice do elemento que se quer remover
				int indice = ordenador.busca(pagina.elementos, new BigInteger(chave), 0, pagina.tamanho);
				
				//pagina filha a esquerda do elemento que se quer remover
				Pagina aux = pagina.elementos[indice].esq;
				
				//busca pagina folha do elemento maior da esquerda
				while(aux.elementos[aux.tamanho-1].dir != null)
				{
					
					aux = aux.elementos[aux.tamanho-1].dir;
					System.out.println("raiz:"+this.raiz.elementos[this.raiz.tamanho-1].chave);
					
				}
				
				// COPIA DO MAIOR ELEMENTO DA ESQUERDA
				Elemento copia = new Elemento(aux.elementos[aux.tamanho-1].chave.toString(), aux.elementos[aux.tamanho-1].deslocamento);
				copia.dir = pagina.elementos[indice].dir;
				copia.esq = pagina.elementos[indice].esq;
				
				pagina.elementos[indice] = copia;
				
				/* BUSCA ÍNDICE DO PRIMEIRO CARA MAIOR AO ELEMENTO
				 * A SER REMOVIDO PARA POSSÍVEL REDISTRIBUIÇÃO
				 */
				int prox = ordenador.buscaSeq(aux.getPai().elementos, copia.chave, aux.getPai().tamanho);
				indice = ordenador.busca(aux.getPai().elementos, copia.chave, 0, aux.getPai().tamanho);
				
				aux.elementos[aux.tamanho-1] = null;
				//aux.tamanho = aux.tamanho - 1;
				
				/* VERIFICA SE PAG TEM MENOS DO QUE d ELEMENTOS
			     * ALTERAÇÃO FEITA EM 29/01/2017 ÀS 19h33
			     * OBS.: FUNCIONANDO
			     */
				if(aux.tamanho == d)
				{
					//System.out.println("aux é menor que d");
					if(redistribuir2(aux, prox, indice) == false)
					{
						concatenar(aux, prox, indice);
					}
				}
				
				else if(aux.tamanho > d)
				{
					aux.tamanho = aux.tamanho - 1;
				}
				
			}
			
		}
		
	}
	
	private boolean redistribuir2(Pagina pagina,int prox,int indice)
	{
		if(pagina.getPai().elementos[prox].esq.equals(pagina))
		{
			if(pagina.getPai().elementos[prox].dir.tamanho > this.d)
			{
				redistribuirDireita(pagina, prox, indice);
				return true;
			}	
			
		}
		else if(pagina.getPai().elementos[prox].esq.tamanho > this.d)
		{
			/* REDISTRIBUIÇÃO NORMAL
			 * ps.: É PRECISO TRATAR O CASO DE EXCLUSÃO DO ÚLTIMO
			 * ELEMENTO DA DIREITA */
		
			/* COPIA A CHAVE DO PRIMEIRO ELEMENTO DO FILHO
			 * ESQUERDO DA PAGINA PAI E ATUALIZA AS REFERÊNCIAS
			 */
			redistribuirEsquerda(pagina, prox, indice, false);
			return true;
		}
		
		else if((prox > 1) && pagina.getPai().elementos[prox-2].esq.tamanho > this.d)
		{
			redistribuirEsquerda(pagina, prox-2, indice, true);
			return true;
		}
		
		return false;
	}
	
	public void cisao(Pagina pagina)
	{
		int meio = (2*d+1)/2;
		int j = 0;
		
		// se pagina tem pai cria uma pagina nova
		if(pagina.getPai() != null)
		{
			Pagina filhaDir = new Pagina(getOrdem());
			
			// copia os elementos para a nova pagina 
			for (int i = meio + 1; i < pagina.tamanho ; i++) 
			{
				filhaDir.elementos[j] = pagina.elementos[i];
				filhaDir.tamanho++;
				
				/* ALTERAÇÃO FEITA AQUI NO DIA 28/01/2017
				 * ÀS 00H40
				*/
				
				if(pagina.elementos[i].esq != null)
				{
					pagina.elementos[i].esq.setPai(filhaDir);
				}
				
				if(pagina.elementos[i].dir != null)
				{
					pagina.elementos[i].dir.setPai(filhaDir);
				}
				pagina.elementos[i] = null;
				
				j++;
				
			}
			
			//antes de subir para o pai passa o ponteiro da esquerda para a direita do cara ao lado
			if(pagina.elementos[meio].esq != null)
			{
				pagina.elementos[meio - 1].dir = pagina.elementos[meio].esq;
			}
			
			// faz as liga��es do elemento do meio
			pagina.elementos[meio].esq = pagina;
					
			// sobe o elemento do meio p/ o pai
			pagina.getPai().elementos[pagina.getPai().tamanho] = pagina.elementos[meio];
			pagina.getPai().tamanho++;
			int fim = pagina.getPai().tamanho;
			
			// ordena pai
			ordenador.mergesort(pagina.getPai().elementos, 0, fim);
			
			// busca o indice do elemento que acabou de subir
			int indice = ordenador.busca(pagina.getPai().elementos, pagina.elementos[meio].chave, 0, fim);
			
			if(indice + 1 < pagina.getPai().tamanho){
				
				if(pagina.getPai().elementos[indice + 1] != null)
				{
					pagina.getPai().elementos[indice+1].esq = filhaDir;

				}
				
			}
			/* VERIFICAR SE HÁ FILHO À ESQUERDA E SETAR O PAI CORRETAMENTE */
			else
			{
				pagina.getPai().elementos[indice].dir = filhaDir;
				// s� o ultimo elemento pode ter filhos a direita (o ultimo elemento do pai n�o vai ser mais o ultimo)
				pagina.getPai().elementos[indice-1].dir = null;
				
			}
			
			// ajusta o tamanho da pagina atual
			pagina.elementos[meio] = null;
			pagina.tamanho = meio;
			
			filhaDir.setPai(pagina.getPai());			
			
			if(pagina.getPai().tamanho == 2*getOrdem()+1)
			{
				// se estorou o pai faz a cis�o do mesmo
				cisao(pagina.getPai());
			}
			
		}
		
		else
		{	// se nao tem pai cria duas paginas novas
		
			Pagina novaRaiz = new Pagina(getOrdem());
			Pagina filhaDir = new Pagina(getOrdem());
			
			// joga elemento do meio da pagina antiga p/ pagina nova
			novaRaiz.elementos[0] = pagina.elementos[meio];
			novaRaiz.tamanho++;
			
			// caso em que o elemento do meio tem filho a esquerda e sobe para a raiz
			if(pagina.elementos[meio].esq != null)
			{
				pagina.elementos[meio - 1].dir = pagina.elementos[meio].esq;				
			}
			
			// faz liga��o das paginas
			novaRaiz.elementos[0].esq = pagina;
			novaRaiz.elementos[0].dir = filhaDir;
			
			for (int i = meio + 1; i < pagina.tamanho ; i++) 
			{
				filhaDir.elementos[j] = pagina.elementos[i];
				filhaDir.tamanho++;
				
				//checando se os pais dos elementos estam corretos
				if(pagina.elementos[i].esq != null)
				{
					pagina.elementos[i].esq.setPai(filhaDir);
				}
				//checando se os pais dos elementos estam corretos
				if(pagina.elementos[i].dir != null)
				{
					pagina.elementos[i].dir.setPai(filhaDir);
				}
				pagina.elementos[i] = null;
				j++;
				
				
			}
			
			pagina.tamanho = meio;
			pagina.elementos[meio] = null;
			
			// seta pai das paginas
			pagina.setPai(novaRaiz);
			filhaDir.setPai(novaRaiz);
			raiz = novaRaiz;

		}
		
	}	
	
	private void redistribuirDireita(Pagina pagina,int prox,int indice)
	{
		// copia a chave do PRIMEIRO elemento do filho direito da pagina pai
		// e atualiza as referencias
		
		Elemento aux = new Elemento(pagina.getPai().elementos[prox].dir.elementos[0].chave.toString(), 
		pagina.getPai().elementos[prox].dir.elementos[0].deslocamento);
		
		aux.esq = pagina.getPai().elementos[prox].esq;
		aux.dir = pagina.getPai().elementos[prox].dir;
		pagina.getPai().elementos[prox].esq = null;
		pagina.getPai().elementos[prox].dir = null;
		pagina.elementos[indice] = pagina.getPai().elementos[prox];		
		pagina.getPai().elementos[prox] = aux;
		pagina.getPai().elementos[prox].dir.elementos[0] = null;
		
		/* AO EXCLUIR DA PRIMEIRA POSIÇÃO, DÁ NULLPOINTEREXCEPTION.
		 * É PRECISO ARRASTAR TODOS OS OUTROS ELEMENTOS
		 */
		ordenador.arrasta(pagina.getPai().elementos[prox].dir.elementos, 0, pagina.getPai().elementos[prox].dir.tamanho);
		ordenador.mergesort(pagina.getPai().elementos[prox].dir.elementos, 0, pagina.getPai().elementos[prox].dir.tamanho);
		pagina.getPai().elementos[prox].dir.tamanho = pagina.getPai().elementos[prox].dir.tamanho-1;

	}
	
	private void redistribuirEsquerda(Pagina pagina,int prox,int indice, boolean pegaUltimoEsq)
	{	
		Elemento aux = null;
		int maiorEsq = pagina.getPai().elementos[prox].esq.tamanho-1;
		
		/* CASO O ELEMENTO REMOVIDO NÃO SEJA O ÚLTIMO DA DIREITA */
		
		if(prox == pagina.getPai().tamanho-1)
		{
			if(pagina.elementos[indice].chave.compareTo(pagina.getPai().elementos[prox].chave) == -1)
			{

				aux = new Elemento(pagina.getPai().elementos[prox].esq.elementos[0].chave.toString(),
						pagina.getPai().elementos[prox].esq.elementos[0].deslocamento);
				
				//System.out.println("\nCHAVE DO ELEMENTO QUE VOU SUBIR:"+aux.chave);

				aux.esq = pagina.getPai().elementos[prox-1].esq;
				aux.dir = pagina.getPai().elementos[prox-1].dir;
				pagina.getPai().elementos[prox-1].esq = null;
				pagina.getPai().elementos[prox-1].dir = null;
				pagina.elementos[indice] = pagina.getPai().elementos[prox-1];

				pagina.getPai().elementos[prox-1] = aux;
				pagina.getPai().elementos[prox].esq.elementos[0] = null;
				
				/* AO REMOVER O ELEMENTO DA PRIMEIRA POSIÇÃO, É PRECISO ARRASTAR OS OUTROS NO VETOR.
				 * ESSA REMOÇÃO É PARA O CASO DA EXCLUSÃO SER NA POSIÇÃO TAMANHO-1 E PRECISAR SUBIR
				 * O CARA DA POSIÇÃO 0
				 */
				
				ordenador.arrasta(pagina.getPai().elementos[prox].esq.elementos, 0, pagina.getPai().elementos[prox].esq.tamanho);
				pagina.getPai().elementos[prox].esq.tamanho = pagina.getPai().elementos[prox].esq.tamanho-1;
				
			}
			else
			{

			 aux = new Elemento(pagina.getPai().elementos[prox].esq.elementos[maiorEsq].chave.toString(),
					pagina.getPai().elementos[prox].esq.elementos[maiorEsq].deslocamento);

			 //System.out.println("\nCHAVE DO ELEMENTO QUE VOU SUBIR:"+aux.chave);
			 
			 aux.esq = pagina.getPai().elementos[prox].esq;
			 aux.dir = pagina.getPai().elementos[prox].dir;
			 pagina.getPai().elementos[prox].esq = null;
			 pagina.getPai().elementos[prox].dir = null;
			 pagina.elementos[indice] = pagina.getPai().elementos[prox];
			 pagina.getPai().elementos[prox] = aux;
			 
			 /* APÓS ACERTAR AS REFERÊNCIAS, EXCLUI O ELEMENTO DA PÁGINA */
			 
			 pagina.getPai().elementos[prox].esq.elementos[maiorEsq] = null;
			 pagina.getPai().elementos[prox].esq.tamanho = pagina.getPai().elementos[prox].esq.tamanho-1;
			}		
		}
		
		else
		{
			if(!pegaUltimoEsq)
			{
				aux = new Elemento(pagina.getPai().elementos[prox].esq.elementos[0].chave.toString(),
						pagina.getPai().elementos[prox].esq.elementos[0].deslocamento);
				
				
				//System.out.println("\nCHAVE DO ELEMENTO QUE VOU SUBIR:"+aux.chave);

				aux.esq = pagina.getPai().elementos[prox-1].esq;
				aux.dir = pagina.getPai().elementos[prox-1].dir;
				pagina.getPai().elementos[prox-1].esq = null;
				pagina.getPai().elementos[prox-1].dir = null;
				pagina.elementos[indice] = pagina.getPai().elementos[prox-1];
				pagina.getPai().elementos[prox-1] = aux;
				pagina.getPai().elementos[prox-1].esq.elementos[maiorEsq] = null; /*ACHO QUE PRECISA TROCAR PRA 0 */
				pagina.getPai().elementos[prox-1].esq.tamanho = pagina.getPai().elementos[prox-1].esq.tamanho-1;
			}

			
			else
			{
				 aux = new Elemento(pagina.getPai().elementos[prox].esq.elementos[maiorEsq].chave.toString(),
						 pagina.getPai().elementos[prox].esq.elementos[maiorEsq].deslocamento);
				 
				 aux.esq = pagina.getPai().elementos[prox].esq;
				 aux.dir = pagina.getPai().elementos[prox].dir;
				 pagina.getPai().elementos[prox].esq = null;
				 pagina.getPai().elementos[prox].dir = null;
				 System.out.println("indice: "+indice);
				 pagina.elementos[indice] = pagina.getPai().elementos[prox];
				 
				 System.out.println("ELEMENTO QUE EU DESÇO: "+pagina.elementos[0].valorChave);
				 System.out.println("ELEMENTO AO LADO DO QUE EU DESCI: "+pagina.elementos[1].valorChave);
				 System.out.println("Tamanho da pagina: "+pagina.tamanho);
				 
				 pagina.getPai().elementos[prox] = aux;
				 
				 /* APÓS ACERTAR AS REFERÊNCIAS, EXCLUI O ELEMENTO DA PÁGINA */
				 
				 pagina.getPai().elementos[prox].esq.elementos[maiorEsq] = null;
				 pagina.getPai().elementos[prox].esq.tamanho = pagina.getPai().elementos[prox].esq.tamanho-1;
			}
		}

		
	}
	
	public void concatenar(Pagina pagina,int prox,int indice)
	{
		// REMOVE ELEMENTO NA PAGINA E ARRASTA OS ELEMENTOS SEGUINTES A ESTE ELEMENTO PARA A ESQUERDA  
		pagina.elementos[indice] = null;
		ordenador.arrasta(pagina.elementos,indice, pagina.tamanho);
		pagina.tamanho = pagina.tamanho -1;
		
		
		//CASO QUE NAO MEXE COM A ALTURA DA ARVORE
		if(pagina.equals(pagina.getPai().elementos[prox].esq))
		{
			/*SE SIM CONCATENAR COM O PROX-1 E PROX-1 ESQ */
		
			if(pagina.getPai().tamanho > 1)
			{	
				
				for (int i = pagina.tamanho; i < pagina.getPai().elementos[prox-1].esq.tamanho; i++)
				{
					pagina.elementos[i] = pagina.getPai().elementos[prox-1].esq.elementos[i];
					pagina.tamanho = pagina.tamanho + 1;
				}
			
			
				pagina.getPai().elementos[prox-1].esq = null;
				pagina.elementos[pagina.tamanho] = pagina.getPai().elementos[prox-1];
				pagina.tamanho = pagina.tamanho + 1;

				pagina.getPai().elementos[prox-1] = null;
				ordenador.arrasta(pagina.getPai().elementos,prox-1,pagina.getPai().tamanho);
				pagina.getPai().tamanho = pagina.getPai().tamanho - 1;
			
				ordenador.mergesort(pagina.elementos,0,pagina.tamanho);
			
			
				// HORA DA CISAO
			
				if(pagina.tamanho == 2*this.d + 1) 
				{
				
				}
				
			}
			
			else
			{
				System.out.println("Mexe na altura");
				
				for (int i = pagina.tamanho; i < pagina.getPai().elementos[prox].dir.tamanho; i++)
				{
					pagina.elementos[i] = pagina.getPai().elementos[prox].dir.elementos[i];
					pagina.tamanho = pagina.tamanho + 1;
				}
			
			
				pagina.getPai().elementos[prox].dir = null;
				pagina.elementos[pagina.tamanho] = pagina.getPai().elementos[prox];
				pagina.tamanho = pagina.tamanho + 1;

				pagina.getPai().elementos[prox] = null;
				ordenador.arrasta(pagina.getPai().elementos,prox,pagina.getPai().tamanho);
				pagina.getPai().tamanho = pagina.getPai().tamanho - 1;
			
				ordenador.mergesort(pagina.elementos,0,pagina.tamanho);
			
			
				// HORA DA CISAO
			
				if(pagina.tamanho == 2*this.d + 1) 
				{
				
				}
				
				concatenar(pagina.getPai(),0,0);
			}
				
			
		}
		
		if(prox == pagina.getPai().tamanho-1)
		{
			pagina.getPai().elementos[prox].esq.elementos[pagina.getPai().elementos[prox].esq.tamanho] = pagina.getPai().elementos[prox]; // desce o 140
			pagina.getPai().elementos[prox].esq.tamanho = pagina.getPai().elementos[prox].esq.tamanho+1; // aumenta o tamanho da pagina
			pagina.getPai().elementos[prox-1].dir = pagina.getPai().elementos[prox].esq; // faz o vizinho de 140 apontar pra esquerda dele
			pagina.getPai().elementos[prox].esq = null;
			pagina.getPai().elementos[prox].dir = null; // exclui a referência para a página vazia
			pagina.getPai().elementos[prox] = null; // dá null na posição do 140 na página pai
			pagina.getPai().tamanho = pagina.getPai().tamanho-1; // decrementa o tamanho do pai
			//ordenador.mergesort(pagina.getPai().elementos[prox-1].dir.elementos, 0, pagina.getPai().elementos[prox-1].dir.tamanho);
			
		}
		
	}
	
	private void preOrdem(Pagina raiz)
	{		
		if(raiz != null)
		{
			
			for(int i = 0; i < raiz.tamanho; i++)
			{
				System.out.print(raiz.elementos[i].chave+" ");
				preOrdem(raiz.elementos[i].esq);
				preOrdem(raiz.elementos[i].dir);
			}

		}
	}
	
	public void imprimir()
	{
		preOrdem(this.raiz);
		System.out.println();
	}
	
}

