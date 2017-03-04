package principal;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Scanner;
import hashtable.*;
import util.Serializacao;
import arquivo.Arquivo;
import arvore.*;

public class Principal 
{
	public static void main(String[] args) 
	{
		
		Scanner entrada = new Scanner(System.in);
		//Scanner entrada2 = new Scanner(System.in);
		System.out.println("* * * * * * * * * * * * * * * * * * * * * \n");
		System.out.println("Escolha uma das opções abaixo:\n \n"
				+ "\t1 - Busca Sequencial\n\t2 - Gerar Arquivo Binário\n"
				+ "\t3 - Gerar Árvore B\n\t4 - Gerar Tabela Hash\n"
				+ "\t5 - Incluir Registro\n\t6 - Consultar Registro\n"
				+ "\t7 - Excluir Registro\n\t8 - Desempenho\n\t"
				+ "9 - Imprimir Arquivo Binário\n\t10 - Encerrar\n");
		System.out.println("* * * * * * * * * * * * * * * * * * * * * ");
		int opcao = entrada.nextInt();
		String cpf;
		
		while(opcao!= 10)
		{
			switch(opcao)
			{
				case 1:
					
					System.out.println("Informe o CPF:");
					cpf = entrada.next();
					Arquivo.buscaSequencial("clientes.txt", cpf);
					break;
					
				case 2:
					
					try
					{
						Arquivo.converterArquivo("clientes.txt");
						System.out.println("Arquivo gerado com sucesso!");
					}
					catch(IOException e)
					{
						System.out.println("Erro ao gerar arquivo!");
					}
					break;
				
				case 3:
					
					Arquivo.criarIndiceAVB("clientes.bin");
					//Arvore avb = Serializacao.deserializarArvore("arvore.bin");
					//avb.imprimir();
					break;
				
				case 4:
					
					try
					{
						Arquivo.criarIndiceHash("clientes.bin");
					}
					catch(IOException e)
					{
						System.out.println("Erro ao gerar tabela!");
					}
					break;
				
				case 5:
					
					try
					{
						Arquivo.inserirRegistro("clientes.bin");
					}
					catch(IOException e)
					{
						System.out.println("Erro ao inserir registro!");
					}
					break;
				
				case 6:
					
					System.out.println("1 - Consultar na tabela\t2 - Consultar na árvore\t3 - Consultar em ambas estruturas");
					int escolha = entrada.nextInt();
					System.out.println("Informe o CPF:");
					cpf = entrada.next();
					
					if(escolha == 1)
					{
						Arquivo.recuperarHash(cpf);
					}
					else if(escolha == 2)
					{
						Arquivo.recuperarArv(cpf);
						
					}
					else if(escolha == 3)
					{
						System.out.println("Realizando consulta na Hash:");
						Arquivo.recuperarHash(cpf);
						System.out.println();
						System.out.println("Realizando consulta na Árvore:");
						Arquivo.recuperarArv(cpf);
					}
					break;
				
				case 7:
					
					System.out.println("Informe o CPF:");
					cpf = entrada.next();
					TabelaHash table = Serializacao.deserializar("arquivoHastable.bin");
					table.remover(cpf);
					Serializacao.serializar(table, "arquivoHastable.bin");
					break;
					
				case 8:
					
					System.out.println("Informe o CPF:");
					cpf = entrada.next();
					
					long inicio = System.currentTimeMillis();
					Arquivo.buscaSequencial("clientes.txt", cpf);
					long tempo = System.currentTimeMillis() - inicio;
					System.out.println("Sequencial: "+tempo+"ms");
					
					
					Arvore arv = Serializacao.deserializarArvore("arvore.bin");
					inicio = System.currentTimeMillis();
					Pagina p = arv.busca(cpf);
					
					if(Ordenador.busca(p.elementos, new BigInteger(cpf),0,p.tamanho) == -1)
					{	
						System.out.println("Chave não existe");
					}
					
					tempo = System.currentTimeMillis() - inicio;
					System.out.println("Árvore: "+tempo+"ms");
					
					
					table = Serializacao.deserializar("arquivoHastable.bin");
					inicio = System.currentTimeMillis();
					int indiceTabela = table.divisao(cpf);
					
					if(!table.chaveExiste(indiceTabela,cpf))
					{
						System.out.println("Chave nao existe.");
					}
					
					tempo = System.currentTimeMillis() - inicio;
					System.out.println("Tabela Hash: "+tempo+"ms");
					break;
					
				case 9:
					Arquivo.imprimir("clientes.bin");
					break;
					
				default:
					System.out.println("Opção inválida!");
					break;
					
				
			}
			
			System.out.println("* * * * * * * * * * * * * * * * * * * * * \n");
			System.out.println("Escolha uma das opções abaixo:\n \n"
					+ "\t1 - Busca Sequencial\n\t2 - Gerar Arquivo Binário\n"
					+ "\t3 - Gerar Árvore B\n\t4 - Gerar Tabela Hash\n"
					+ "\t5 - Incluir Registro\n\t6 - Consultar Registro\n"
					+ "\t7 - Excluir Registro\n\t8 - Desempenho\n"
					+ "\t9 - Imprimir Arquivo Binário\n\t10 - Encerrar\n");
			System.out.println("* * * * * * * * * * * * * * * * * * * * * ");
			opcao = entrada.nextInt();
		}
		System.out.println("Programa encerrado!");
		
	}

}
