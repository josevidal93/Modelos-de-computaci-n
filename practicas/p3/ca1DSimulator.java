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

public class ca1DSimulator  implements ca1DSim,Runnable {
	public  static int[][] resultado;
	private static int size,ngen;
	private int inicio, fin;
	private static int[] celulas;
	private static int[] calculo;
	public static double[] hamming;
	public static double[] entropia_res;
	private static double p0,p1, entropia_par;	
	private static int cores = Runtime.getRuntime().availableProcessors();
	private static Runnable[] tasks = new Runnable[cores];
	private static ExecutorService threadPool = Executors.newFixedThreadPool(cores);
	private static CyclicBarrier barrier = new CyclicBarrier(cores+1);
    private static final Object lock= new Object();    

	public ca1DSimulator(int size){
		Scanner sc = new Scanner(System.in);
		this.size = size;
		celulas =new int[size];
		calculo =new int[size];
        Random rnd = new Random();
        celulas[size/2] = 1;
       /* for(int i =0 ; i< size; ++i)
			 celulas[i]= Math.abs(rnd.nextInt())%2;
		/*
        for(int i =0 ; i< size; ++i)
			System.out.print(celulas[i]);
		System.out.println();
*/
		
		
		int partes = size/cores;
		
		for (int i = 0; i < cores; ++i)
		{
			int inicio = i*partes;
			int fin;
			
			if (i+1 == cores)
				fin = size;
			else
				fin = (i+1)*partes;
			tasks[i] = new ca1DSimulator(inicio, fin);
		}
			
			
	}
	private ca1DSimulator(int inic, int fin)
	{
		this.inicio = inic;
		this.fin   = fin;
	}
	public void nextGen(){
			
		int z;
		String cadena ;
		String[][] regla = {{"000", "0", "0"}, {"001", "1", "2"}, {"010", "0", "4"},
							{"011", "1", "8"}, {"100", "1", "16"}, {"101", "0", "32"},
							{"110", "1", "64"}, {"111", "0", "128"}};
		for(int i =inicio ; i< fin; ++i){
		 	//z = mod(i-1,size);
			//calculo[i]= ((celulas[z]*celulas[(i+1)%size])+celulas[i])%2;  //función de transición
						//01010110 2+ 8+32+64 --106
			
		}
		for(int i =inicio ; i< fin; ++i){
			z = mod(i-1,size);
			cadena = Integer.toString(celulas[z]) + Integer.toString(celulas[i])+Integer.toString(celulas[(i+1)%size]);
			
			for(int j=0;j<8;++j)
				if(cadena.equals(regla[j][0])){
					calculo[i]= Integer.parseInt(regla[j][1]);
				//	System.out.println(cadena +"-----"+calculo[i]);
					//System.out.println(i);
					
				}
		}

	}
	public  synchronized void aumentar1() {
		 synchronized(lock)
		{  
			p1++;  
		}  
	}
	
		public  synchronized void aumentar0() {
			 synchronized(lock)
			{  
				p0++;  
			}  
	}
	public void entropia()
	{		
		for(int i=inicio;i<fin;++i)	{
			if(calculo[i]==0)
				aumentar0();
			else
				aumentar1();
			
		}
	
		
	}
	
	public double entropia_temp(int c)
	{	
		int p0=0,p1=0;
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
			p1=0;
			p0=0;
			
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
						entropia_res[k]= p0/size* logbase(p0/size,2) + p1/size* logbase(p1/size,2);

			
			
			


		}
		threadPool.shutdown(); 
	 while(!threadPool.isTerminated()){}

			
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
	
}



	


