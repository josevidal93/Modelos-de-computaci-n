import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.JFrame ;
import java.lang.String;
import javax.swing.Timer;
import javax.swing.SwingWorker;
import javax.imageio.*;
import java.io.*;
import javax.swing.JFileChooser;
import java.io.File;       
import java.util.Scanner;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;
/*
 Version que muestra la alfombra que deja el automata
 @author José Manuel Vidal Jiménez
 @version 2.0
 @since 2/11/2016
 
 */ 

public class ca1DGraphicSimulation  {
	private static JFrame frame;
	private static JTextField cel,gen,celula,res,regla,original,cifrado;	
	private BufferedImage imagen; 
	private static JLabel picLabel;
	private static JButton ejecutar,h,entropia1,cifrar, entropia2,ejecutar2;
	private static JPanel panel;
	private static ca1DSimulator automata;
	private static SwingWorker worker;
	private static int size,g,rule;
	private static GraphPanel g_hamming;
	private String mensaje;

	public ca1DGraphicSimulation() {
		frame = new JFrame("Autómata celular");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		JPanel parametros = new JPanel(new GridLayout(20, 1));
		JPanel botones = new JPanel(new GridLayout(10, 1));
		
		
		
		JLabel celulas   = new JLabel("Celulas");
		JLabel generaciones  = new JLabel("Generaciones");
		JLabel reglas   = new JLabel("Regla");
		JLabel celula_temp  = new JLabel("Cel. entropía");
		JLabel resultado  = new JLabel("Entropia temp. ");
		JLabel ori  = new JLabel("Original");
		JLabel cif  = new JLabel("Cifrado");
		cel   = new JTextField("1150");
		gen   = new JTextField("640");
		celula = new JTextField("450");
		res 	= new JTextField("0");
		regla	= new JTextField("90");
		original = new JTextField("");
		cifrado = new JTextField("");
		

		parametros.add(celulas);
		parametros.add(cel);
		
		parametros.add(reglas);
		parametros.add(regla);
		
		parametros.add(generaciones);
		parametros.add(gen);
		
		parametros.add(celula_temp);
		parametros.add(celula);
		
		parametros.add(resultado);
		parametros.add(res);

		parametros.add(cif);
		parametros.add(cifrado);

		parametros.add(ori);
		parametros.add(original);
		
		ejecutar = new JButton("Ejecutar");
		cifrar = new JButton("Cifrar");		
		h = new JButton("Hamming");	
		entropia1 = new JButton("Entropía Espacial");	
		entropia2 = new JButton("Entropía temporal");		
		cifrar = new JButton("Cifrar");		
	
		parametros.add(ejecutar);
		parametros.add(h,BorderLayout.SOUTH);
		parametros.add(entropia1,BorderLayout.SOUTH);		
		parametros.add(entropia2,BorderLayout.SOUTH);		
		parametros.add(cifrar,BorderLayout.SOUTH);
		//panel.add(botones,BorderLayout.WEST);
		//parametros.add(botones,BorderLayout.WEST);
		panel.add(parametros,BorderLayout.WEST);

		frame.getContentPane().add(panel,BorderLayout.WEST);

		//full screen
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//frame.setVisible(true);
		
		//pulsar botón ejecutar
		
		ejecutar.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
					
				JButton boton = (JButton) e.getSource();
				size = Integer.parseInt(cel.getText());
				g = Integer.parseInt(gen.getText());
				rule= Integer.parseInt(regla.getText());
				automata = new ca1DSimulator(size,rule);
				automata.caComputation(g);
			
				worker = new SwingWorker<Void, Void>()
				{
					public Void doInBackground()
					{ 
										
						imagen = convertir(automata.resultado);
						picLabel = new JLabel(new ImageIcon(imagen),JLabel.CENTER);
						panel.add(picLabel);									
						panel.revalidate();
						panel.repaint();
						
												
						return null;
					}
				};
				
				worker.execute();							
			}
		});   
		
		
		h.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
				size = Integer.parseInt(cel.getText());
				g = Integer.parseInt(gen.getText());
				rule= Integer.parseInt(regla.getText());
				automata = new ca1DSimulator(size,rule);
				automata.caComputation(g);
				automata.Hamming();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						g_hamming.createAndShowGui(automata.hamming,g);
					}
				});
				
					
				
			}
			
		}); 
		
		entropia1.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
				
				size = Integer.parseInt(cel.getText());
				g = Integer.parseInt(gen.getText());
				rule= Integer.parseInt(regla.getText());
				automata = new ca1DSimulator(size,rule);
				automata.caComputation(g);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						g_hamming.createAndShowGui(automata.entropia_res,g);
					}
				});
				
			}
			
		}); 	
		
		entropia2.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
				int celula_temp = Integer.parseInt(celula.getText()); 
				size = Integer.parseInt(cel.getText());
				g = Integer.parseInt(gen.getText());
				rule= Integer.parseInt(regla.getText());
				automata = new ca1DSimulator(size,rule);
				automata.caComputation(g);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						res.setText(Double.toString(automata.entropia_temp(celula_temp)));
						
					}
				});
				
			}		
				
			
		}); 
		
		cifrar.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
				boolean file = true;
				String fichero="";
				File selectedFile= null;
			    JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setCurrentDirectory(new File("user.dir"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				jFileChooser.setFileFilter(filter);
				Cypher c;
				System.out.println("-"+original.getText()+"-");
				if(original.getText().equals(""))
				{	
					int result = jFileChooser.showOpenDialog(new JFrame());
					if (result == JFileChooser.APPROVE_OPTION) {
						selectedFile = jFileChooser.getSelectedFile();
					}
					try
					{
						fichero = new Scanner(selectedFile).useDelimiter("\\A").next();
					}
					catch (FileNotFoundException i){}
				    c = new Cypher(fichero);
				    mensaje= fichero;
				 }
				 else
				 {
					 mensaje = original.getText();
					 c = new Cypher(original.getText());
				 }
					
				   SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						cifrado.setText(c.cifrar());
						original.setText(mensaje);
						
					}
				});
				
			}		
				
			
		}); 		
	}
	
	
	public static JPanel panel()
	{
		return panel;
	}
				
	public static BufferedImage convertir(int[][] matriz)
	{
        
        BufferedImage imagen = new BufferedImage(size, g, BufferedImage.TYPE_BYTE_BINARY);
		for(int j=0;j<g;++j)
			for(int i=0;i<size;++i) {
			  
					if(matriz[j][i]==1)
					{	
							imagen.setRGB(i, j, Color.WHITE.getRGB());
					}
		}    
        return imagen;
    }
    
	
	public static void main(String[] args) {
		ca1DGraphicSimulation v = new ca1DGraphicSimulation();
		
		
	}
}
