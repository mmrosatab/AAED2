package arquivo;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import arvore.*;
import hashtable.Hash;
import hashtable.TabelaHash;
import util.Conversao;
import util.Serializacao;

public class Arquivo
{
	
	public static int buscaSequencial(String nomeArquivo,String cpf)
	{
		String linha;
		
		try
		{
			RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw");

			while((linha = arquivo.readLine())!= null)
			{
				
				String[] splitTab = linha.split("\t");
				
				String[] splitPonto = splitTab[0].split("\\.");
				
				String[] splitTraco = splitPonto[2].split("-");
				
				String novoCpf = splitPonto[0]+ splitPonto[1]+splitTraco[0]+splitTraco[1];
				
				if(novoCpf.equals(cpf))
				{
					
					System.out.println("Registro encontrado: ");
					System.out.println("CPF: "+novoCpf);
					System.out.println("Nome: "+splitTab[1]);
					System.out.println("Endereco: "+splitTab[2]);
					System.out.println("Email: "+splitTab[3]);
					System.out.println("Data de nascimento: "+splitTab[4]);
					System.out.println("Telefone: "+splitTab[5]);
					System.out.println("N° de Compras: "+splitTab[6]);
					System.out.println();

					arquivo.close();
					return 0;
				}
				
			}
			System.out.println("Registro nao encontrado.");
			
		arquivo.close();
			
		} catch (Exception e)
		{
			// TODO: handle exception
			System.out.println("Problema abertura arquivo no metodo buscaSequencial");
		}
		
		System.out.println("Não existe nenhum registro com esse CPF");
		return -1;
	}
	
	public static void converterArquivo(String nomeArquivoTxt) throws NumberFormatException, IOException
	{	
		RandomAccessFile arquivoTxt = new RandomAccessFile(nomeArquivoTxt, "rw");
		String[] nomeArquivoBin = nomeArquivoTxt.split(".txt");
		//System.out.println("nomedoarquivo: "+nomeArquivoBin[0]);
		RandomAccessFile arquivoBin = new RandomAccessFile(nomeArquivoBin[0]+".bin", "rw");
		String linha;
		
		while((linha = arquivoTxt.readLine())!= null)
		{
			
			String[] splitTab = linha.split("\t");
			
			String[] splitPonto = splitTab[0].split("\\.");
			
			String[] splitTraco = splitPonto[2].split("-");
			
			String novoCpf = splitPonto[0]+ splitPonto[1]+splitTraco[0]+splitTraco[1];
			
			// imprimindo campo para teste
			/*System.out.println("CPF: "+novoCpf);
			System.out.println("Nome: "+splitTab[1]);
			System.out.println("Endereco: "+splitTab[2]);
			System.out.println("Email: "+splitTab[3]);
			System.out.println("Data de nascimento: "+splitTab[4]);
			System.out.println("Telefone: "+splitTab[5]);
			System.out.println("N° de Compras: "+Integer.parseInt(splitTab[6]));
			*/
		
			escrever(arquivoBin,true,novoCpf, splitTab[1], splitTab[2], splitTab[3], splitTab[4],splitTab[5],Integer.parseInt(splitTab[6]));
				
		}
		
		arquivoTxt.close();
		arquivoBin.close();
		
		
	}
	
	public static void escrever(RandomAccessFile arquivo,boolean ativo ,String cpf,String nome,String end,
			String email,String data,String tel,int nCompras) throws IOException 
	{
		int tam = 1 + 22 + nome.length()*2 + end.length()*2 + email.length()*2 + data.length()*2 + tel.length()*2 + 28;
		// fazendo tratamento da string
		byte[] arrayBoolean = Conversao.booleanToByte(ativo);
		byte[] arrayCPF = Conversao.stringByte(cpf);
		byte[] arrayTamNome = Conversao.intToByteArray(nome.length());
		byte[] arrayNome = Conversao.stringByte(nome);
		byte[] arrayTamEnd = Conversao.intToByteArray(end.length());
		byte[] arrayEnd = Conversao.stringByte(end);
		byte[] arrayTamEmail = Conversao.intToByteArray(email.length());
		byte[] arrayEmail = Conversao.stringByte(email);
		byte[] arrayTamData = Conversao.intToByteArray(data.length());
		byte[] arrayData = Conversao.stringByte(data);
		byte[] arrayTamTel = Conversao.intToByteArray(tel.length()); //talvez tam telefone seja fixo
		byte[] arrayTel = Conversao.stringByte(tel);
		byte[] arrayNCompras = Conversao.intToByteArray(nCompras);
		byte[] tamanhoTotal = Conversao.intToByteArray(tam);
		
		/* + 24 PORQUE TEM 5 STRINGS CUJO TAMANHO (DO TIPO INT) VEM ANTES DE CADA UMA
		 * + 4 BYTES DO INTEIRO nCompras. TOTALIZANDO 24 BYTES EXTRAS
		 * 5*4 = 20
		 * 20 + 4 bytes de nCompras = 24
		 */
		
		arquivo.write(tamanhoTotal);
		arquivo.write(arrayBoolean);
		arquivo.write(arrayCPF);
		arquivo.write(arrayTamNome);
		arquivo.write(arrayNome);
		arquivo.write(arrayTamEnd);
		arquivo.write(arrayEnd);
		arquivo.write(arrayTamEmail);
		arquivo.write(arrayEmail);
		arquivo.write(arrayTamData);
		arquivo.write(arrayData);
		arquivo.write(arrayTamTel);
		arquivo.write(arrayTel);
		arquivo.write(arrayNCompras);
	
	
	}
	
	public static void criarIndiceAVB(String nomeArquivoBin)
	{
		
		//Anda no arquivo, calcula o tamanho dos registros e cria indices na arvore associados ao deslocamentos
		try
		{
			RandomAccessFile arquivo = new RandomAccessFile(nomeArquivoBin,"rw");
			Arvore arv = new Arvore(8);
			
			// rebobinando leitura arquivo
			arquivo.seek(0);
			//buscar registro
			
			byte[] b = new byte[1];
			byte[] arrayCpf = new byte[11*2]; // lê 22 bytes do cpf
			byte[] arrayInt = new byte[4]; // lê inteiro
			byte[] tamanhoTotal = new byte[4];
			
			byte[] arrayNome,end,email,data,tel;
			int tamNome,tamEnd,tamEmail,tamData,tamTel;
			
			long deslocamento = 0;
			arquivo.read(tamanhoTotal);
			arquivo.read(b);
			
			while(arquivo.read(arrayCpf)!= -1)
			{

				String cpf = Conversao.byteString2(arrayCpf);
				
				if(Conversao.byteToBoolean(b))
				{
					arv.inserir(cpf, deslocamento);
				}
				
				deslocamento = deslocamento + Conversao.byteArrayToInt(tamanhoTotal);
				arquivo.seek(deslocamento);
				
				arquivo.read(tamanhoTotal);
				arquivo.read(b);
			}
				
			arquivo.close();		
			Serializacao.serializar(arv,"arvore.bin");
		}
		catch(IOException e)
		{
			System.out.println("Erro na função criar índice na árvore B.");
		}
		
	}
	
	public static void criarIndiceHash(String nomeArquivoBin) throws IOException
	{
		//Anda no arquivo, calcula o tamanho dos registros e cria indices na arvore associados ao deslocamentos
		
		RandomAccessFile arquivo = new RandomAccessFile(nomeArquivoBin,"rw");
		TabelaHash tabela = new TabelaHash(10007);

		// rebobinando leitura arquivo
		arquivo.seek(0);
		//buscar registro
		
		byte[] b = new byte[1];
		byte[] arrayCpf = new byte[11*2]; // lê 22 bytes do cpf
		byte[] arrayInt = new byte[4]; // lê inteiro
		byte[] tamanhoTotal = new byte[4];
		
		byte[] arrayNome,end,email,data,tel;
		int tamNome,tamEnd,tamEmail,tamData,tamTel;
		
		long deslocamento = 0;
		arquivo.read(tamanhoTotal);
		arquivo.read(b);
		long aux = 0;
		
		while(arquivo.read(arrayCpf)!= -1)
		{	
			String cpf = Conversao.byteString2(arrayCpf);
			
			// CRIA UM NÓ E SETA O DESLOCAMENTO EM BYTES
			Hash hash = new Hash(cpf,deslocamento);
			tabela.inserir(hash);
			
			deslocamento = deslocamento + Conversao.byteArrayToInt(tamanhoTotal);
			arquivo.seek(deslocamento);
			
			arquivo.read(tamanhoTotal);
			arquivo.read(b);
		}
			
		arquivo.close();		
		Serializacao.serializar(tabela,"arquivoHastable.bin");
	}
	
	/* NÃO FUNCIONA */
	
	public static void atualizarArquivo(String nomeArquivo)
	{
		try
		{
			String[] nomeArquivoBin = nomeArquivo.split(".bin");
			RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw");
			RandomAccessFile novoArquivo = new RandomAccessFile(nomeArquivoBin[0]+"2.bin","rw");
			byte[] b = new byte[1];
			byte[] tamanhoTotal = new byte[4];
			byte[] campos = new byte[4];
			byte[] cpf = new byte[22];
			int tamanho;
			
			while(arquivo.read(tamanhoTotal) != -1)
			{	tamanho = Conversao.byteArrayToInt(tamanhoTotal);
				arquivo.read(b);
				if(Conversao.byteToBoolean(b))
				{
					System.out.println("Entrei no if");
					novoArquivo.write(tamanhoTotal);
					novoArquivo.write(b);
					
					arquivo.read(cpf);
					novoArquivo.write(cpf);
					
					// TAMANHO NOME
					campos = new byte[4];
					arquivo.read(campos);
					tamanho = Conversao.byteArrayToInt(campos);
					novoArquivo.write(campos);
					
					// NOME
					campos = new byte[tamanho*2]; // USA O TAMANHO DA STRING PRA PEGÁ-LA
					arquivo.read(campos);
					novoArquivo.write(campos);
					
					// TAMANHO DO ENDEREÇO
					campos = new byte[4];
					arquivo.read(campos);
					tamanho = Conversao.byteArrayToInt(campos);
					novoArquivo.write(campos);
					
					// ENDEREÇO
					campos = new byte[tamanho*2];
					arquivo.read(campos);
					novoArquivo.write(campos);
					
					// TAMANHO DO CEP
					campos = new byte[4];
					arquivo.read(campos);
					tamanho = Conversao.byteArrayToInt(campos);
					novoArquivo.write(campos);
					
					// CEP
					campos = new byte[tamanho*2];
					arquivo.read(campos);
					novoArquivo.write(campos);
					
					// TAMANHO EMAIL
					campos = new byte[4];
					arquivo.read(campos);
					tamanho = Conversao.byteArrayToInt(campos);
					novoArquivo.write(campos);
					
					// EMAIL
					campos = new byte[tamanho*2];
					arquivo.read(campos);
					novoArquivo.write(campos);
					
					// TAMANHO DATA
					campos = new byte[4];
					arquivo.read(campos);
					tamanho = Conversao.byteArrayToInt(campos);
					novoArquivo.write(campos);
					
					// DATA
					campos = new byte[tamanho*2];
					arquivo.read(campos);
					novoArquivo.write(campos);
					
					// TAMANHO TELEFONE
					campos = new byte[4];
					arquivo.read(campos);
					tamanho = Conversao.byteArrayToInt(campos);
					novoArquivo.write(campos);
					
					// TELEFONE
					campos = new byte[tamanho*2];
					arquivo.read(campos);
					novoArquivo.write(campos);
					
					// NÚMERO DE COMPRAS
					campos = new byte[4];
					arquivo.read(campos);
					novoArquivo.write(campos);
					
				}
				
				else
				{
					int t = Conversao.byteArrayToInt(tamanhoTotal);
					arquivo.seek((arquivo.getFilePointer()- arquivo.getFilePointer()) + (long)t);
				}
				
			}
			
			arquivo.close();
			novoArquivo.close();
			
		}
		catch(IOException e)
		{
			System.out.println("Erro na função atualizar arquivo.");
		}
		
	}
	
	public static void excluirRegistro(String nomeArquivo, long deslocamento)
	{
		try
		{
			RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo,"rw");
			byte [] tamanho = new byte[4];
			
			System.out.println("ENTREI NO EXCLUIR REGISTRO");
			arquivo.seek(deslocamento);
			arquivo.read(tamanho);
			arquivo.write((byte)0);
			arquivo.close();

		}
		
		catch(IOException e)
		{
			System.out.println("Erro na função excluir registro.");
		}
		
	}
	public static void inserirRegistro(String nomeArquivo) throws IOException
	{
		RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo, "rw");
		String cpf,nome,end,email, data, tel;
		int nCompras;

		//vai para o fim do arquivo 
		arquivo.seek(arquivo.length());

		Scanner teclado = new Scanner(System.in);

		System.out.println("Entre com os campos do registro separados por TAB (sem pontos ou traços): ");
		String linha = teclado.nextLine();

		String[] split = linha.split("\t");
		cpf = split[0];
		nome = split[1];
		end = split[2];
		email = split[3];
		data = split[4];
		tel = split[5];
		nCompras = Integer.parseInt(split[6]);

		escrever(arquivo, true, cpf, nome, end, email, data, tel, nCompras);

		criarIndiceHash(nomeArquivo);
		criarIndiceAVB(nomeArquivo);

		arquivo.close();

	}
	
	public static void recuperarArv(String cpf)
	{
		Arvore arv = Serializacao.deserializarArvore("arvore.bin");
		Pagina auxiliar = arv.busca(cpf);
		int indice = Ordenador.busca(auxiliar.elementos, new BigInteger(cpf), 0, auxiliar.tamanho);
		
		try
		{
			RandomAccessFile arquivo = new RandomAccessFile("clientes.bin","rw");
			
			if(indice != -1)
			{
				arquivo.seek(auxiliar.elementos[indice].deslocamento);
				
				byte[] b = new byte[1];
				byte[] arrayCpf = new byte[11*2]; // lê 22 bytes do cpf
				byte[] arrayInt = new byte[4]; // lê inteiro
				byte[] tamanhoTotal = new byte[4];
				
				byte[] arrayNome,end,email,data,tel;
				int tamNome,tamEnd,tamEmail,tamData,tamTel;
				
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
			}
			else
			{
				System.out.println("Registro não encontrado.");
			}
			
			arquivo.close();
		}
		catch(IOException e)
		{
			System.out.println("Erro na função recuperar árvore.");
		}
	}
	public static void recuperarHash(String cpf)
	{
		TabelaHash tabela = Serializacao.deserializar("arquivoHastable.bin");
		//Scanner teclado1 = new Scanner(System.in);
		int indiceTabela, indiceNaLista;
		List<Hash> lista;
		
		try
		{
			RandomAccessFile arquivo = new RandomAccessFile("clientes.bin","rw");
			
			//arquivo.seek(0);
			indiceTabela = tabela.divisao(cpf);
			// cria referencia de uma lista que esta na tabela a partir do indice
			lista = tabela.getLista(indiceTabela);
			indiceNaLista = tabela.indice(lista, cpf);
			
			if(indiceNaLista != -1)
			{
				// desloco no arquivo uma determinada qtd de bytes
				arquivo.seek(lista.get(indiceNaLista).getdeslocamento());
				
			
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
				System.out.println("Registro não encontrado.");
			}
			
			arquivo.close();
		}
		catch(IOException e)
		{
			System.out.println("Erro ao abrir arquivo na função recuperar hash");
		}
		
		
	}
	public static void imprimir(String nomeArquivo)
	{
		try
		{
			
		RandomAccessFile arquivo = new RandomAccessFile(nomeArquivo,"rw");

		// rebobinando leitura arquivo
		arquivo.seek(0);
		//buscar registro
		
		byte[] b = new byte[1];
		byte[] arrayCpf = new byte[11*2]; // lê 22 bytes do cpf
		byte[] arrayInt = new byte[4]; // lê inteiro
		byte[] tamanhoTotal = new byte[4];
		
		byte[] arrayNome,end,email,data,tel;
		int tamNome,tamEnd,tamEmail,tamData,tamTel;

		arquivo.read(tamanhoTotal);
		arquivo.read(b);
		
		while(arquivo.read(arrayCpf)!= -1)
		{
			// pega cpf
			System.out.println("Registro: "+Conversao.byteToBoolean(b));
			
			String cpf = Conversao.byteString2(arrayCpf);
			System.out.println("CPF:"+cpf);

			arquivo.read(arrayInt); // pega tamanho do nome 
			tamNome = Conversao.byteArrayToInt(arrayInt);
			//System.out.println(tamNome);
			
			arrayNome = new byte[tamNome*2];
			arquivo.read(arrayNome); // pega nome 
			System.out.println(new String(arrayNome,"UTF-16"));
			
			arquivo.read(arrayInt);
			tamEnd = Conversao.byteArrayToInt(arrayInt);
			//System.out.println(tamEnd);
			
			end = new byte[tamEnd*2];
			arquivo.read(end);
			System.out.println("END: "+Conversao.byteString2(end));
			
			arquivo.read(arrayInt);
			tamEmail = Conversao.byteArrayToInt(arrayInt);
			//System.out.println(tamEmail);
			
			email = new byte[tamEmail*2];
			arquivo.read(email);
			System.out.println("E-mail: "+Conversao.byteString2(email));
			
			arquivo.read(arrayInt);
			tamData = Conversao.byteArrayToInt(arrayInt);
			//System.out.println(tamData);
			
			data = new byte[tamData*2];
			arquivo.read(data);
			System.out.println("Data: "+Conversao.byteString2(data));
			
			arquivo.read(arrayInt);
			tamTel = Conversao.byteArrayToInt(arrayInt);
			//System.out.println(tamTel);
			
			tel = new byte[tamTel*2];
			arquivo.read(tel);
			System.out.println("Tel: "+Conversao.byteString2(tel));
			
			arquivo.read(arrayInt);// pega int numero de compras
			System.out.println("N° compras: "+Conversao.byteArrayToInt(arrayInt));
			System.out.println();
			
			arquivo.read(tamanhoTotal);
			arquivo.read(b);
		}
		
			arquivo.close();
		}
		catch(IOException e)
		{
			System.out.println("Erro na leitura do arquivo!");
		}
			
		
	}
	
}
