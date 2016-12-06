import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Random;

public class paralellBelZab implements Runnable
{
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	
	private Random random;
	
	private static double[][][] reaccion;
	private static double[][][] siguiente_reaccion;
	
	private static double alfa;
	private static double beta;
	private static double gamma;
	
	private static int size;
	
	
	private static int nucleos;
	private static ExecutorService threadPool;
	private static Runnable[] tareas;
	private static CyclicBarrier barrera;
	
	private int inicio;
	private int fin;
	
	private paralellBelZab(int inicio, int fin)
	{
		this.inicio = inicio;
		this.fin    = fin;
	}
	
	private static void acceptNextGeneration()	{
		for(int i=0;i<3;++i)
			for(int j=0;j<size;++j)
				for(int k=0;k<size;++k)
					reaccion[i][j][k] =siguiente_reaccion[i][j][k];
	}

		

	
	public paralellBelZab(int size, double alfa, double beta, double gamma)
	{
		this.size = size;
		reaccion   = new double[3][size][size];
		siguiente_reaccion   = new double[3][size][size];
		
		this.alfa  = alfa;
		this.beta  = beta;
		this.gamma = gamma;

		nucleos    = Runtime.getRuntime().availableProcessors();
		threadPool = Executors.newFixedThreadPool(nucleos);
		barrera    = new CyclicBarrier(nucleos + 1);
		tareas     = new Runnable[nucleos];
		
		for (int i = 0; i < nucleos; ++i)
		{
			int inicioIntervalo = i * (size / nucleos);
			int finIntervalo    = (i+1) * (size/nucleos);
			
			if ((i+1) == nucleos)
				finIntervalo = size;
			
			tareas[i] = new paralellBelZab(inicioIntervalo, finIntervalo);
		}
		
		random = new Random();
		aleatorio();
	}

	public void aleatorio()
	{
				
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				reaccion[A][i][j] = random.nextDouble();
				reaccion[B][i][j] = random.nextDouble();
				reaccion[C][i][j] = random.nextDouble();
			}
		}
		
	}
		

	public static void siguienteGeneracion()
	{		
		for (int i = 0; i < tareas.length; ++i)
			threadPool.execute(tareas[i]);
		
			try
			{
				barrera.await();
				barrera.reset();
				acceptNextGeneration();


			}
			catch (InterruptedException e)
			{
				System.out.println("InterruptedException: BelZhab.siguienteGeneracion()");
				System.out.println("Error: " + e.getMessage());
			}
			catch (BrokenBarrierException e)
			{
				System.out.println("BrokenBarrierException: BelZhab.siguienteGeneracion()");
				System.out.println("Error: " + e.getMessage());
			}
		
	}
	
	private void subGeneracion()
	{
		double[] concentracion;
		
		for (int i = inicio; i < fin; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				concentracion = concentraciones(j, i);
				
				siguiente_reaccion[A][i][j] = parteFlotante(concentracion[A] * (1 + (alfa  * concentracion[B] - gamma * concentracion[C])));
				siguiente_reaccion[B][i][j] = parteFlotante(concentracion[B] * (1 + (beta  * concentracion[C] - alfa  * concentracion[A])));
				siguiente_reaccion[C][i][j] = parteFlotante(concentracion[C] * (1 + (gamma * concentracion[A] - beta  * concentracion[B])));
			}
		}
	}
	
	private static double[] concentraciones(int x, int y)
	{
		double[] concentracion = new double[3];
		
		for (int i = -1; i <= 1; ++i)
		{
			for (int j = -1; j <= 1; ++j)
			{
				concentracion[A] += reaccion[A][mod(y+i, size)][mod(x+j, size)];
				concentracion[B] += reaccion[B][mod(y+i, size)][mod(x+j, size)];
				concentracion[C] += reaccion[C][mod(y+i, size)][mod(x+j, size)];
			}
		}
		
		concentracion[A] /= 9;
		concentracion[B] /= 9;
		concentracion[C] /= 9;
		
		return concentracion;
	}
	
	private static double parteFlotante(double f)
	{
		return f - Math.floor(f);
	}
	
	public static double[][][] mostrar()
	{
		return siguiente_reaccion;
		
	}
	
	public void run()
	{
			subGeneracion();
			
			try
			{
				barrera.await();
			}
			catch (InterruptedException e)
			{
				System.out.println("InterruptedException: BelZhab.run()");
				System.out.println("Error: " + e.getMessage());
			}
			catch (BrokenBarrierException e)
			{
				System.out.println("BrokenBarrierException: BelZhab.run()");
				System.out.println("Error: " + e.getMessage());
			}
	}
	
	private static int mod(int a, int b)
	{
		int r = a % b;
		if (r < 0)
			r += b;
		
		return r;
	}
	

	
}









