import java.util.concurrent.BrokenBarrierException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ca1DSimulator  implements ca1DSim,Runnable {
	public  static int[][] resultado;
	private String[][] regla = {{"000", "0", "0"}, {"001", "0", "2"}, {"010", "0", "4"},
							{"011", "0", "8"}, {"100", "0", "16"}, {"101", "0", "32"},
							{"110", "0", "64"}, {"111", "0", "128"}};
	private static int size,ngen,rule;
	private int inicio, fin;
	public static int[] celulas;
	private static int[] calculo;
	public static double[] hamming;
	public static double[] entropia_res;
	private static AtomicInteger p0,p1;
	private static double  entropia_par;	
	private static int cores = Runtime.getRuntime().availableProcessors();
	private static Runnable[] tasks = new Runnable[cores];
	private static ExecutorService threadPool = Executors.newFixedThreadPool(cores);
	private static CyclicBarrier barrier = new CyclicBarrier(cores+1);

	public ca1DSimulator(int size, int rule){
		Scanner sc = new Scanner(System.in);
		this.size = size;
		this.rule = rule;
		celulas =new int[size];
		calculo =new int[size];
        Random rnd = new Random();
        for(int j=0;j<size;++j)
			celulas[j]= Math.abs(rnd.nextInt())%2;
		
        //celulas[size/2] = 1;
        regla(rule);
		
		int partes = size/cores;
		
		for (int i = 0; i < cores; ++i)
		{
			int inicio = i*partes;
			int fin;
			
			if (i+1 == cores)
				fin = size;
			else
				fin = (i+1)*partes;
			tasks[i] = new ca1DSimulator(inicio, fin,rule);
		}
			
			
	}
	private ca1DSimulator(int inic, int fin,int rule)
	{
		this.inicio = inic;
		this.fin   = fin;
		this.rule = rule;
		regla(rule);
	}
	public void nextGen(){
			
		int z;
		String cadena ;
		/*String[][] regla = {{"000", "0", "0"}, {"001", "1", "2"}, {"010", "0", "4"},
							{"011", "1", "8"}, {"100", "1", "16"}, {"101", "0", "32"},
							{"110", "1", "64"}, {"111", "0", "128"}};*/
	
		for(int i =inicio ; i< fin; ++i){
			z = mod(i-1,size);
			cadena = Integer.toString(celulas[z]) + Integer.toString(celulas[i])+Integer.toString(celulas[(i+1)%size]);
			for(int j=0;j<8;++j)
				if(cadena.equals(regla[j][0])){
					calculo[i]= Integer.parseInt(regla[j][1]);					
				}
		}

	}
	
	public void entropia()
	{		
		for(int i=inicio;i<fin;++i)	{
			if(calculo[i]==0)
				p0.getAndIncrement();
			else
				p1.getAndIncrement();
	
			
		}
	
		
	}
	
	public double entropia_temp(int c)
	{	
		double p0=0,p1=0;
		double g= ngen;
		for(int i=0;i<ngen;++i)	{
			if(resultado[i][c]==0)
				p0++;
			else
				p1++;
		}
		if(p0==0 || p1==0)
			return 0;
		else
			return Math.abs(p0/g* logbase(p0/g,2) + p1/g* logbase(p1/g,2));
		
	}
	
	public  void Hamming()	{
		
		for(int i=0;i<ngen-1;++i)
			for(int j=0;j<size;++j)	{
				if(resultado[i][j] != resultado[i+1][j])
					hamming[i]++;
			}											
	}
	
	
	public static double logbase(double a,  int b)
	{
		return Math.abs(Math.log(a) / Math.log(b));
	}
	
	private int mod(int a, int b)
	{
		int r = a % b;
		if (r < 0)
			r += b;
		
		return r;
	}
	public void caComputation(int nGen){
		ngen=nGen;
		hamming = new double[nGen];
		entropia_res = new double[nGen];
		resultado = new int[nGen][size];
		
		for(int k=0;k<nGen;++k)
		{	
			p1= new AtomicInteger(0);
			p0= new AtomicInteger(0);
			
			resultado[k]= celulas.clone();

			
			for(int i=0; i<cores; ++i) {
				threadPool.execute(tasks[i]);	
		}
			try
			{
				barrier.await();
				barrier.reset();
				sustituir();
			}
			catch (InterruptedException e)
			{
				System.out.println("Error: " + e.getMessage());
			}
			catch (BrokenBarrierException e)
			{
				System.out.println("Error: " + e.getMessage());
			}
			if(p0.get()==0 || p1.get()==0)
				entropia_res[k] =0;
			else
				entropia_res[k]=(double)(p0.get())/size* logbase((double)(p0.get())/size,2) + (double)(p1.get())/size* logbase((double)(p1.get())/size,2);
						
			
			


		}
	/*	threadPool(); 
	 while(!threadPool.isTerminated()){}
*/
			
	}
	public static void sustituir()
	{
		System.arraycopy( calculo, 0, celulas, 0, calculo.length );
	}
	@Override
	public void run()
	{
		this.nextGen();
		this.entropia();
		try
			{
				barrier.await();
			}
			catch (InterruptedException e)
			{
				System.out.println("Error: " + e.getMessage());
			}
			catch (BrokenBarrierException e)
			{
				System.out.println("Error: " + e.getMessage());
			}
	}
	
	 public void regla(int n)
    {
        String bin = toBinary(n);
        for(int i=0; i<bin.length();++i)
			regla[i][1] =  String.valueOf(bin.charAt(7-i));
	
    }
    
     public static String toBinary(int n)
    {
        String b = ""; // binary representation as a string
        while (n != 0) {
            int r = (int)(n % 2); // remainder
            b = r + b; // concatenate remainder
            n /= 2; // reduce n
        }
        while(b.length()<8)
			b = '0'+b;
        return b;
    }
    
    public void test()	{
		int tam = 1000;
		int gen = 4000;
		double H_media=0.0, E_media= 0.0;
		Random rnd = new Random();
		int[] celulas = new int[tam];
		for(int j=0;j<tam;++j)
			celulas[j]= rnd.nextInt()%2;
		ca1DSimulator x;
		for(int i=0;i<256;++i)
		{
			x = new ca1DSimulator(tam,i);
			
			x.caComputation(gen);
			x.entropia_temp(tam/2);
			for(int k=0;k<gen;++k)
			{
				E_media += x.entropia_res[k];
				H_media += x.hamming[k];
			}
			System.out.println(E_media/(double)(gen));
			System.out.println(H_media/(double)(gen));

				
		}

	}
	
	public static void main(String[] args) throws IOException
	{
		Hashtable<Integer, Integer> rules = new Hashtable<Integer, Integer>();
		int tam = 4;
		int gen = 28;
		double H_media, E_media;
		Random rnd = new Random();
		int[] celulas_test = new int[tam];
		PrintWriter fichero = new PrintWriter("marcas_2.txt", "UTF-8");
		for(int h=0;h<10;++h)	{
						System.out.println(h);

		for(int j=0;j<tam;++j)
			celulas_test[j]= Math.abs(rnd.nextInt())%2;
		
		//fichero.println("Regla  Hamming_media  Entropia_media  Entropia_temporal");
		for(int i=0;i<256;++i)
		{
			H_media=0.0;
			E_media= 0.0;
			ca1DSimulator x = new ca1DSimulator(tam,i);
			System.arraycopy( celulas_test, 0, x.celulas, 0, tam );
		/*	for(int p=0;p<tam;++p)
				System.out.print(x.celulas[p]);*/
			x.caComputation(gen);
			x.Hamming();
			x.entropia_temp(tam/2);
			for(int k=0;k<gen;++k)
			{
				E_media += x.entropia_res[k];
				H_media += x.hamming[k];
			}
			if(x.entropia_temp(tam/2)>0.9999999)
			{
			//	fichero.println(i+"\t\t"+H_media/(gen)+"\t\t\t\t"+ E_media/(gen)+"\t\t\t\t"+x.entropia_temp(tam/2));
				if(rules.get(i)!=null)
					rules.put(i,rules.get(i)+1);
				else
					rules.put(i,1);
			}
				
		}
	
	  
		
		}
		
		sortByValue(rules);
		Enumeration<Integer> enumKey = rules.keys();

	while(enumKey.hasMoreElements()) {
			Integer key = enumKey.nextElement();
			Integer val = rules.get(key);
			fichero.println("Regla "+key+", value: "+val);
	}

	fichero.close();
	System.out.println("fin");
	
		
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
    return map.entrySet()
              .stream()
              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
              .collect(Collectors.toMap(
                Map.Entry::getKey, 
                Map.Entry::getValue, 
                (e1, e2) -> e1, 
                LinkedHashMap::new
              ));
}
}
		

	
		
		




	


