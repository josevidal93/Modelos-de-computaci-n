import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Juego de la vida
 * @author José Manuel Vidal Jiménez
 * @version 1 27/11/2016
 */
public class Life implements Runnable
{
	/** Cell's grid, represents states of each cell, being 'true' alive and 'false' dead. */
	private static boolean[][] grid;
	
	/** Auxiliary grid, necessary to save the next generation without collisions */
	private static boolean[][] nextGrid;
	
	/** Size of our grid. It has to be square at the moment. */
	private static int size;
	
	/** Number of available cores */
	private static int cores = Runtime.getRuntime().availableProcessors();
	
	/** Thread's pool */
	private static ExecutorService threadPool = Executors.newFixedThreadPool(cores);
	
	/** Array of Runnable tasks. Necessary to avoid creating tasks for each new generation */
	private static Runnable[] tasks = new Runnable[cores];
	
	/** Barrier to keep threads synchronised. */
	private static CyclicBarrier barrier = new CyclicBarrier(cores + 1);
	
	/** Integer representing where this task has to start computing the next generation */
	private int beginX;
	
	/** Integer representing where this task has to stop computing the next generation */
	private int endX;
	/** Integer array representing where is stored the live cells and dead cells */
	private static int count[];
	/**
	 * Create a new random state for the automata, with a 0.1 probability of a cell being alive
	 */
	public void randomGrid(String opt)
	{
		grid = new boolean[size][size];
		//		String[] options = { "Random","Glider", "Tumbler", "10cellrow", "Explorer", "Blinker" };

		switch(opt)
		{
			case "Glider":
				grid[155][155] = true;
				grid[156][155] = true;
				grid[157][155] = true;
				grid[155][156] = true;
				grid[156][157] = true;
				break;
				
			case "Tumbler":
				grid[150][151] = true;
				grid[150][152] = true;		 
				grid[150][154] = true;
				grid[150][155] = true;
				
				grid[151][151] = true;
				grid[151][152] = true;
				grid[151][154] = true;
				grid[151][155] = true;
						
				grid[152][152] = true;
				grid[152][154] = true;
				
				grid[153][150] = true;
				grid[153][152] = true;
				grid[153][154] = true;
				grid[153][156] = true;
						
				grid[154][150] = true;
				grid[154][152] = true;
				grid[154][154] = true;
				grid[154][156] = true;
				
				grid[155][150] = true;
				grid[155][151] = true;
				grid[155][155] = true;
				grid[155][156] = true;				
				break;
			case "Gosper Glider Gun":
				grid[1][5]=true;
				grid[1][6]=true;
				grid[2][5]=true;
				grid[2][6]=true;
				grid[11][5]=true;
				grid[11][6]=true;
				grid[11][7]=true;
				grid[12][4]=true;
				grid[12][8]=true;
				grid[13][3]=true;
				grid[13][9]=true;
				grid[14][3]=true;
				grid[14][9]=true;
				grid[15][6]=true;
				grid[16][4]=true;
				grid[16][8]=true;
				grid[17][5]=true;
				grid[17][6]=true;
				grid[17][7]=true;
				grid[18][6]=true;
				grid[21][3]=true;
				grid[21][4]=true;
				grid[21][5]=true;
				grid[22][3]=true;
				grid[22][4]=true;
				grid[22][5]=true;
				grid[23][2]=true;
				grid[23][6]=true;
				grid[25][1]=true;
				grid[25][2]=true;
				grid[25][6]=true;
				grid[25][7]=true;
				
				grid[35][3]=true;
				grid[35][4]=true;
				grid[36][3]=true;
				grid[36][4]=true;
				grid[35][22]=true;
				grid[35][23]=true;
				grid[35][25]=true;
				grid[36][22]=true;
				grid[36][23]=true;
				grid[36][25]=true;
				grid[36][26]=true;
				grid[36][27]=true;
				grid[37][28]=true;
				grid[38][22]=true;
				grid[38][23]=true;
				grid[38][25]=true;
				grid[38][26]=true;
				grid[38][27]=true;
				grid[39][23]=true;
				grid[39][25]=true;
				grid[40][23]=true;
				grid[40][25]=true;
				grid[41][24]=true;	
			break;
			case "SpaceShips":
					 
				grid[4][5] = true;
				grid[4][7] = true;
				grid[5][8] = true;
				grid[6][8] = true;
				grid[7][8] = true;
				grid[7][5] = true;
				grid[8][6] = true;
				grid[8][7] = true;
				grid[8][8] = true;

				grid[20][4] = true;
				grid[22][5] = true;
				grid[18][5] = true;
				grid[23][6] = true;
				grid[23][7] = true;
				grid[18][7] = true;
				grid[23][8] = true;
				grid[22][8] = true;
				grid[21][8] = true;
				grid[20][8] = true;
				grid[19][8] = true;

				grid[35][4] = true;
				grid[36][4] = true;
				grid[33][5] = true;
				grid[38][5] = true;
				grid[39][6] = true;
				grid[33][7] = true;
				grid[39][7] = true;
				grid[34][8] = true;
				grid[35][8] = true;
				grid[36][8] = true;
				grid[37][8] = true;
				grid[38][8] = true;
				grid[39][8] = true;



				/** This is the preset for the Weekender spaceship, discovered by David Eppstein
				* */

				grid[44][16] = true;
				grid[45][16] = true;
				grid[47][16] = true;
				grid[48][16] = true;
				grid[46][17] = true;
				grid[46][15] = true;
				grid[49][17] = true;
				grid[51][17] = true;
				grid[51][18] = true;
				grid[51][19] = true;
				grid[51][20] = true;
				grid[51][25] = true;
				grid[51][26] = true;
				grid[51][27] = true;
				grid[51][28] = true;
				grid[50][21] = true;
				grid[50][22] = true;
				grid[50][23] = true;
				grid[50][24] = true;
				grid[49][21] = true;
				grid[49][22] = true;
				grid[49][23] = true;
				grid[49][24] = true;
				grid[53][19] = true;
				grid[54][20] = true;
				grid[54][21] = true;
				grid[54][24] = true;
				grid[54][25] = true;
				grid[53][26] = true;
				grid[49][28] = true;
				grid[48][29] = true;
				grid[47][29] = true;
				grid[46][28] = true;
				grid[46][30] = true;
				grid[45][29] = true;
				grid[44][29] = true;


				/** This is the preset for the 44P5H2V0 spaceship, discovered by Dean Hickerson
				* */

				grid[6][44] = true;
				grid[6][50] = true;
				grid[7][45] = true;
				grid[7][49] = true;
				grid[7][42] = true;
				grid[7][52] = true;
				grid[8][45] = true;
				grid[8][49] = true;
				grid[8][40] = true;
				grid[8][41] = true;
				grid[8][53] = true;
				grid[8][54] = true;
				grid[9][45] = true;
				grid[9][49] = true;
				grid[10][45] = true;
				grid[10][49] = true;
				grid[10][40] = true;
				grid[10][54] = true;
				grid[11][45] = true;
				grid[11][49] = true;
				grid[11][44] = true;
				grid[11][50] = true;
				grid[12][44] = true;
				grid[12][50] = true;
				grid[12][42] = true;
				grid[12][52] = true;
				grid[13][41] = true;
				grid[13][42] = true;
				grid[13][43] = true;
				grid[13][51] = true;
				grid[13][52] = true;
				grid[13][53] = true;
				grid[14][42] = true;
				grid[14][45] = true;
				grid[14][49] = true;
				grid[14][52] = true;
				grid[15][43] = true;
				grid[15][44] = true;
				grid[15][45] = true;
				grid[15][49] = true;
				grid[15][50] = true;
				grid[15][51] = true;
				grid[16][44] = true;
				grid[16][50] = true;

				break;
			case "10cellrow":
				grid[155][150] = true;
				grid[155][151] = true;
				grid[155][152] = true;
				grid[155][153] = true;
				grid[155][154] = true;
				grid[155][155] = true;
				grid[155][156] = true;
				grid[155][157] = true;
				grid[155][158] = true;
				grid[155][159] = true;
				break;
				
			case "Explorer":
				grid[150][150] = true;
				grid[150][151] = true;
				grid[150][152] = true;
				grid[150][153] = true;
				grid[150][154] = true;
				grid[152][150] = true;
				grid[152][154] = true;
				grid[154][150] = true;
				grid[154][151] = true;
				grid[154][152] = true;
				grid[154][153] = true;
				grid[154][154] = true;
				break;
				
			case "Blinker":
				grid[100][100] = true;
				grid[100][101] = true;
				grid[100][102] = true;
				break;
			case "lightweight spaceship":
				grid[150][151] = true;
				grid[150][152] = true;
				grid[150][153] = true;
				grid[150][154] = true;
				grid[151][150] = true;
				grid[151][154] = true;
				grid[152][154] = true;
				grid[153][150] = true;
				grid[153][153] = true;
				break;
			default:
				for (int i = 0; i < size; ++i)
				for (int j = 0; j < size; ++j)
					grid[j][i] = Math.random() < 0.5;
			
		}
			
		 
		
		
		
	}
	
	/**
	 * Constructor of the 2D automata, with a given size.
	 * @param size Size of the matrix (square)
	 */
	public Life(int size, String option)
	{
		//Set size
		Life.size = size;
		
		//Generate new random initial state
		randomGrid(option);
		
		//In case we have only 1 core
		this.beginX = 0;
		this.endX   = size;
		
		//Create new tasks.
		int parts = size/cores;
		
		for (int i = 0; i < cores; ++i)
		{
			int beginX = i*parts;
			int endX;
			
			//Is this our last iteration? Last task have to take the remaining rows,
			//just in case size%cores != 0
			if (i+1 == cores)
				endX = size;
			else
				endX = (i+1)*parts;
			
			tasks[i] = new Life(beginX, endX);
		}
	}
	
	/**
	 * Constructor para crear las tasks
	 * @param beginX Primera fila a computar
	 * @param endX Última fila a computar
	 */
	private Life(int beginX, int endX)
	{
		this.beginX = beginX;
		this.endX   = endX;
	}
	
	/**
	 * Modulo .
	 * @param a Dividendo
	 * @param b Divisor
	 * @return Resto
	 */
	private int mod(int a, int b)
	{
		int r = a % b;
		if (r < 0)
			r += b;
		
		return r;
	}
	
	/**
	 * Vecindad viva
	 * @param x coordenada x
	 * @param y coordenada y
	 * @return numero de células vivas en la vecindad
	 */
	public int aliveNeighbours(int x, int y)
	{
		int alives = -1;
		
		if (x < size && y < size)
		{
			alives = 0;
			
			for (int i = -1; i <= 1; ++i)
			{
				for (int j = -1; j <= 1; ++j)
				{
					int col = mod(y+j, size);
					int row = mod(x+i, size);
					
					//Avoid considering (x,y) cell
					if (grid[col][row] && (col != y || row != x))
						++alives;
				}
			}
		}

		return alives;
	}
	
	/**
	 * Estado en la siguiente generación
	 * @param x coordenada x
	 * @param y coordenada y
	 * @return Siguiente estado
	 */
	public boolean nextState(int x, int y)
	{
		boolean newState = false;
		int neighbours = aliveNeighbours(x, y);
		
		if (grid[y][x])
		{
			if (neighbours == 2 || neighbours == 3)
				newState = true;
			else
				newState = false;
		}
		else
			if (neighbours == 3)
				newState = true;
			
		return newState;
	}
	
	
	private void nextSubGeneration()
	{
		for (int i = beginX; i < endX; ++i)
			for (int j = 0; j < size; ++j)
				nextGrid[j][i] = nextState(i, j);
	}
	
	/**
	 * Computa la siguiente generación
	 */
	public static void nextGeneration()
	{
		nextGrid = new boolean[size][size];
		
		for (int i = 0; i < cores; ++i)
			threadPool.execute(tasks[i]);
		
		try
		{
			barrier.await();
			barrier.reset();
			acceptNextGeneration();
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
	
	
	@Override
	public void run()
	{
		nextSubGeneration();
		
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
	
	/**
	 
	 * @return Devuelve la cuadrícula de estados
	 */
	public boolean[][] getGrid()
	{
		return grid;
	}
	/**
	 * @return devuelve el número de células vivas y muertas
	 */
	public int[] counter()
	{
		count= new int[2];	
		for(int i=0;i<size;++i)
			for(int j=0;j<size;++j)
		{
			if(grid[i][j]) 
				count[0]++;
			else
				count[1]++;
		}
		return count;
	}
	/**
	 * Convierte la siguiente generación en actual.
	 */
	public static void acceptNextGeneration()
	{
		grid = nextGrid;
	}
}
