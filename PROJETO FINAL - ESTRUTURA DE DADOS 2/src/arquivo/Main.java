package arquivo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import util.Conversao;

public class Main 
{
	public static void main(String[] args) throws NumberFormatException, IOException
	{
		/*
			
		
			
		RandomAccessFile arquivo = new RandomAccessFile("clientes.bin", "rw");
		byte [] n = new byte[4];
		arquivo.seek(1661);
		arquivo.read(n);
		n = new byte[1];
		arquivo.read(n);
		System.out.println(Conversao.byteToBoolean(n));
		n = new byte[22];
		arquivo.read(n);
		System.out.println(Conversao.byteString2(n));
		  
		*/
		
		//Arquivo.converterArquivo("clientes.txt");
		//System.out.println("Executou");
		Arquivo.criarIndiceHash("clientes.bin");
		//Arquivo arq = new Arquivo();
		//arq.imprimir("clientes2.bin");
		


	}
}
