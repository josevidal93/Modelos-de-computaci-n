import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
public class Cypher implements Runnable {
 
	private static int cores = Runtime.getRuntime().availableProcessors(),size;
	private int inicio,fin;
    private static String mensaje, bin_aux;
    private static int[] m_bin, mensaje_cifrado;
    private static ca1DSimulator automata;
    private static int[] clave;				
    private static Runnable[] tasks = new Runnable[cores];
	private static ExecutorService threadPool = Executors.newFixedThreadPool(cores);			
    public Cypher(String mensaje) 
    {
		int ascii=0;
		this.mensaje= mensaje;
		size = mensaje.length();
		clave = new int[size*7];
		mensaje_cifrado =new int[size*7];
		automata = new ca1DSimulator(size,51);
		automata.caComputation(size*7);
		for(int j=0;j<size*7;++j){
			clave[j] = automata.resultado[j][size/2];
		}
		m_bin = new int[size*7];
		for(int i=0; i< mensaje.length();++i)	{
			ascii = mensaje.charAt(i);
			bin_aux = toBinary(ascii);
			for(int g=0;g<bin_aux.length();++g)
			{
				m_bin[i*7 +g]= Character.getNumericValue(bin_aux.charAt(g));
			}
		}
		
	
		/*
		 paralelizar el XOR 
		 */

		int partes = size*7/cores;
		for (int i = 0; i < cores; ++i)
		{
			int inicio = i*partes;
			int fin;
			
			if (i+1 == cores)
				fin = size*7;
			else
				fin = (i+1)*partes;
			tasks[i] = new Cypher(inicio, fin);
		}
    }
    
    public Cypher(int inicio, int fin)
    {
		this.inicio = inicio;
		this.fin = fin;
		
	}
	
	public static String mcifrado()
	{
		String m ="" ;
		int size =mensaje.length()*7;
		for(int i=0;i<size;++i)
			m+= mensaje_cifrado[i];
		return m;
	}
	
	public void run()
	{
		for(int i = inicio ; i<fin ; ++i)	{
			mensaje_cifrado[i] = m_bin[i] ^ clave[i];
		}
	}
 
	public static void xor()
	{
		for(int i=0; i<cores; ++i)
				threadPool.execute(tasks[i]);
				
		threadPool.shutdown(); 
		while(!threadPool.isTerminated()){}
		
	}
	
	public void mostrar(String m,String o)	{
		
		System.out.println("Cifrado");

		for(int i =0;i<size*7;++i)
			System.out.print(mensaje_cifrado[i]);
		System.out.println();
		System.out.println("Clave");
		for(int j =0;j<size*7;++j)
			System.out.print(clave[j]);
		System.out.println();
		System.out.println("mensaje binario");

		for(int j =0;j<size*7;++j)
			System.out.print(m_bin[j]);
		System.out.println();
		
		System.out.println("mensaje en clave");
		System.out.println(m);
		System.out.println("mensaje original");
		System.out.println(o);
	    
		
	}
		
    /**
     * Hace la conversión de base 10 a base 2 (binario)
     *
     * Debemos dividir n por 2 sucesivamente hasta que el resultado es 0, y
     * la representación binaria resultante estará compuesto por los restos de
     * todas las divisiones, desde el último al primero.
     */
    public static String toBinary(int n)
    {
        String b = ""; // binary representation as a string
        while (n != 0) {
            int r = (int)(n % 2); // remainder
            b = r + b; // concatenate remainder
            n /= 2; // reduce n
        }
        while(b.length()<7)
			b = '0'+b;
        return b;
    }
    
    public static String cifrar()
    {
		int ascii_=0,x;
		
		char letra;
		String m="";
		String s;
		char c;
		
		for(int i=0; i<cores; ++i)
				threadPool.execute(tasks[i]);
				
		threadPool.shutdown(); 
		while(!threadPool.isTerminated()){}
		
		String mensaje_ = mcifrado();
		
		for(int i=0;i<mensaje_.length()-6;i++)
		{
			ascii_=0;
			for(int j=6;j>=0;--j)
			{
				c = mensaje_.charAt(i);
				s = ""+mensaje_.charAt(i);
				x = Integer.parseInt(s);
				ascii_+= x*Math.pow(2,j);
				letra = (char)ascii_;
				++i;
				
				
			}
			--i;

			letra = (char)ascii_;
			m= m+letra;

		}
		return m;
	}
	
	public String descifrar(String cifrado)	{
		
		String m="",res="";
		int ascii,ascii_,x;
		char letra,c;
		String s="";
		m_bin = new int[cifrado.length()*7];
		for(int i=0; i< cifrado.length();++i)	{
			ascii = cifrado.charAt(i);
			bin_aux = toBinary(ascii);
			for(int g=0;g<bin_aux.length();++g)
			{
				m_bin[i*7 +g]= Character.getNumericValue(bin_aux.charAt(g));
			}
		}
		
		for(int i = 0 ; i<cifrado.length()*7 ; ++i)	{
			m = m+(m_bin[i] ^ clave[i]);
		}
		
			for(int i=0;i<m.length()-6;i++)
		{
			ascii_=0;
			for(int j=6;j>=0;--j)
			{
				c = m.charAt(i);
				s = ""+m.charAt(i);
				x = Integer.parseInt(s);
				ascii_+= x*Math.pow(2,j);
				letra = (char)ascii_;
				++i;
				
				
			}
			--i;

			letra = (char)ascii_;
			res= res+letra;

		}
		return res;
	}
    
    public static void main(String[] args)
    {
		String cifrado, original="Hola mundo";
		Cypher c = new Cypher(original);
		cifrado = c.cifrar();
		original= c.descifrar(cifrado);
		c.mostrar(cifrado,original);

		
		
	}
}

