package hashtable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Scanner;

import util.Conversao;
import util.Serializacao;

public class Recuperador 
{
	public static void main(String[] args) throws IOException 
	{
		TabelaHash tabela = Serializacao.deserializar("arquivoHastable.bin");
		//tabela.imprimirTabela();
		RandomAccessFile arquivo = new RandomAccessFile("clientes.bin","rw");
		Scanner teclado1 = new Scanner(System.in);
		String cpf;
		int indiceTabela, indiceNaLista;
		List<Hash> lista;
		
		System.out.println("Escolha a opçao desejada"+
				" 1 - Buscar registro,"+
				" 2 - Excluir Registro"+
				" 3 - Encerrar programa");

		int opcao = teclado1.nextInt();

		while(opcao != 3)
		{
			switch (opcao) 
			{			
				case 1:
					
					arquivo.seek(0);
					System.out.println("Entre com o CPF do Cliente:");
					cpf = teclado1.next();
					indiceTabela = tabela.divisao(cpf);
					// cria referencia de uma lista que esta na tabela a partir do indice
					lista = tabela.getLista(indiceTabela);
					indiceNaLista = tabela.indice(lista, cpf);
					
					if(indiceNaLista != -1)
					{
						// desloco no arquivo uma determinada qtd de bytes
						arquivo.seek(lista.get(indiceNaLista).getdeslocamento());
						
						//01234569833
					
						byte[] b = new byte[1];
						byte[] arrayCpf = new byte[11*2]; // lê 22 bytes do cpf
						byte[] arrayInt = new byte[4]; // lê inteiro
						byte[] tamanhoTotal = new byte[4];
						
						byte[] arrayNome,end,email,data,tel;
						int tamNome,tamEnd,tamEmail,tamData,tamTel;
						
						// desloco no arquivo uma determinada qtd de bytes
						arquivo.seek(lista.get(indiceNaLista).getdeslocamento());
						
						System.out.println("Imprimindo registro");
						arquivo.read(tamanhoTotal);
						
						arquivo.read(b);
						System.out.println("Situacao do registro: "+Conversao.byteToBoolean(b));
						
						arquivo.read(arrayCpf);// pega cpf
						System.out.println(Conversao.byteString2(arrayCpf));
				
						arquivo.read(arrayInt); // pega tamanho do nome 
						tamNome = Conversao.byteArrayToInt(arrayInt);
						
						arrayNome = new byte[tamNome*2];
						arquivo.read(arrayNome); // pega nome 
						System.out.println(Conversao.byteString2(arrayNome));
						
						arquivo.read(arrayInt);
						tamEnd = Conversao.byteArrayToInt(arrayInt);
						
						end = new byte[tamEnd*2];
						arquivo.read(end);
						System.out.println("Endereco: "+Conversao.byteString2(end));
						
						arquivo.read(arrayInt);
						tamEmail = Conversao.byteArrayToInt(arrayInt);
						
						email = new byte[tamEmail*2];
						arquivo.read(email);
						System.out.println("E-mail: "+Conversao.byteString2(email));
						
						arquivo.read(arrayInt);
						tamData = Conversao.byteArrayToInt(arrayInt);
						
						data = new byte[tamData*2];
						arquivo.read(data);
						System.out.println("Data de Nascimento: "+Conversao.byteString2(data));
						
						arquivo.read(arrayInt);
						tamTel = Conversao.byteArrayToInt(arrayInt);
						
						tel = new byte[tamTel*2];
						arquivo.read(tel);
						System.out.println("Tel: "+Conversao.byteString2(tel));
						
						arquivo.read(arrayInt);// pega int numero de compras
						System.out.println("N° de compras: "+Conversao.byteArrayToInt(arrayInt));
							
					}else
					{
						System.out.println("Não existe registro com essa matricula.");
					}
					
					break;
					
				case 2:
					
					arquivo.seek(0);
					System.out.println("Entre com o CPF do Cliente:");
					cpf = teclado1.next();
					indiceTabela = tabela.divisao(cpf);
					// cria referencia de uma lista que esta na tabela a partir do indice
					lista = tabela.getLista(indiceTabela);
					indiceNaLista = tabela.indice(lista, cpf);
					
					if(indiceNaLista != -1)
					{
						tabela.remover(cpf);
					}
					else
					{
						System.out.println("Registro não existe.");
					}
					
					break;
					
				default:
					System.out.println("Opção inválida");
					break;
			}
			
			System.out.println("Escolha a opçao desejada"+
				" 1 - Buscar registro,"+
				" 2 - Excluir Registro"+
				" 3 - Encerrar programa");

			opcao = teclado1.nextInt();
			
		}
		
		arquivo.close();
		System.out.println("Programa encerrado!");
	}
	
	
}
