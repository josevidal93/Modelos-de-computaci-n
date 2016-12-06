import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class belZab 
{
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	
	private Random random;
	
	private static double[][][][] reaccion;
	private static double alfa;
	private static double beta;
	private static double gamma;
	
	private static int size;
	private static int p,q=1;


	
	
	
	public belZab(int size, double alfa, double beta, double gamma)
	{
		this.size = size;
		reaccion   = new double[2][3][size][size];		
		this.alfa  = alfa;
		this.beta  = beta;
		this.gamma = gamma;
		random = new Random();
		aleatorio();
	}
	
	
	public void aleatorio()
	{
				
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				reaccion[0][A][i][j] = random.nextDouble();
				reaccion[0][B][i][j] = random.nextDouble();
				reaccion[0][C][i][j] = random.nextDouble();
			}
		}
		
	}

	public void alfa(double alfa)
	{		
		this.alfa = alfa;
	}
	
	public void beta(double beta)
	{
		this.beta = beta;
	}
	
	public void gamma(double gamma)
	{
		this.gamma = gamma;
	}
		
	private static double[] concentraciones(int x, int y)
	{
		double[] concentracion = new double[3];
		
		for (int i = -1; i <= 1; ++i)
		{
			for (int j = -1; j <= 1; ++j)
			{
				concentracion[A] += reaccion[p][A][mod(y+i, size)][mod(x+j, size)];
				concentracion[B] += reaccion[p][B][mod(y+i, size)][mod(x+j, size)];
				concentracion[C] += reaccion[p][C][mod(y+i, size)][mod(x+j, size)];
			}
		}
		
		concentracion[A] /= 9;
		concentracion[B] /= 9;
		concentracion[C] /= 9;
		
		return concentracion;
	}

	private static int mod(int a, int b)
	{
		int r = a % b;
		if (r < 0)
			r += b;
		
		return r;
	}
	private static double parteFlotante(double f)
	{
		return f - Math.floor(f);
	}
	
	public void compute()
	{
		double[] c = new double[3];
		for(int i=0;i<size;++i)
			for(int j=0;j<size;++j)
			{
				c= concentraciones(i,j);
				reaccion[q][A][i][j]= parteFlotante(c[A]+c[A]*(alfa*c[B]-gamma*c[C]));
				reaccion[q][B][i][j]= parteFlotante(c[B]+c[B]*(beta*c[C]-alfa*c[A]));
				reaccion[q][C][i][j]= parteFlotante(c[C]+c[C]*(gamma*c[A]-beta*c[B]));
			}
			
		if(p==0) { p=1; q=0;}
		else {p=0; q=1;}
	}
	
	public double[][][] mostrar()
	{
		return reaccion[1];
	}
	

}
























