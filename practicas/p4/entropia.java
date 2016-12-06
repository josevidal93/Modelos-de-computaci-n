import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Random;

public class entropia implements Runnable{
	private static int [] vector;
	private static int cores = Runtime.getRuntime().availableProcessors();
	private static Runnable[] tasks = new Runnable[cores];
	private static ExecutorService threadPool = Executors.newFixedThreadPool(cores);
	private int inicio,fin;
	private static double p0,p1;
	private static int tam;
	private static double res=0;
    private static final Object lock= new Object();    

	
	public  void mostrar() {
		System.out.println(p1/tam);
		System.out.println(p0/tam);
		System.out.println((p0+p1)/tam);
		System.out.println(tam);
		double entropia_res = p0/tam* logbase(p0/tam,2) + p1/tam* logbase(p1/tam,2);
		System.out.println(entropia_res);


	}
	
	public entropia(int size){
		
		this.tam=size;
		vector = new int[size];
		Random rnd = new Random();
        for(int i =0 ; i< size; ++i){
			vector[i] = Math.abs(rnd.nextInt())%2;
		}
	
		int partes = size/cores;
		
		for (int i = 0; i < cores; ++i)
		{
			int inicio = i*partes;
			int fin;
			
			if (i+1 == cores)
				fin = size;
			else
				fin = (i+1)*partes;
			tasks[i] = new entropia(inicio, fin);
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
	
	public entropia(int inicio, int fin){
		this.inicio = inicio;
		this.fin = fin;
	}
	
	
		@Override
	public void run()
	{		
		for(int i=inicio;i<fin;++i)	{
			if(vector[i]==0)
				aumentar0();
			else
				aumentar1();
			
		}
	
		
	}
	
	public static double logbase(double a,  int b)
	{
		return Math.log(a) / Math.log(b);
	}
	
	public static void main(String[] args) {
		
		int size= (int)Math.pow(10,3);
		entropia x = new entropia(size);
		for(int i=0; i<cores; ++i) 
				threadPool.execute(tasks[i]);
				
		threadPool.shutdown(); 
		while(!threadPool.isTerminated()){}	
		x.mostrar();
		double entropia_res = p0* logbase(p0,2) + p1* logbase(p1,2);
	}

}
