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
/*
 Version que muestra la alfombra que deja el automata
 @author José Manuel Vidal Jiménez
 @version 2.0
 @since 2/11/2016
 
 */ 

public class ca1DGraphicSimulation  {
	private static JFrame frame;
	private static JTextField cel,gen,celula,res;	
	private BufferedImage imagen; 
	private static JLabel picLabel;
	private static JButton ejecutar,h,entropia1, entropia2,ejecutar2;
	private static JPanel panel;
	private static ca1DSimulator automata;
	private static SwingWorker worker;
	private static int size,g;
	private static GraphPanel g_hamming;

	public ca1DGraphicSimulation() {
		frame = new JFrame("Autómata celular");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		JPanel parametros = new JPanel(new GridLayout(2, 3));
		panel.add(parametros);
		
		
		JLabel celulas   = new JLabel("Celulas ");
		JLabel generaciones  = new JLabel("Generaciones ");
		JLabel celula_temp  = new JLabel("Celula entropía ");
		JLabel resultado  = new JLabel("Resultado entropia temporal ");
		
		cel   = new JTextField("1350");
		gen   = new JTextField("650");
		celula = new JTextField("450");
		res 	= new JTextField("0");
		parametros.add(celulas);
		parametros.add(generaciones);
		parametros.add(celula_temp);
		parametros.add(resultado);
		parametros.add(cel);
		parametros.add(gen);
		parametros.add(celula);
		parametros.add(res);
		ejecutar = new JButton("Ejecutar");		
		panel.add(ejecutar);

		h = new JButton("Hamming");		
		panel.add(h);

		entropia1 = new JButton("Entropía Espacial");		
		panel.add(entropia1);
		
		entropia2 = new JButton("Entropía temporal");		
		panel.add(entropia2);
		
		
		frame.getContentPane().add(panel);

		//full screen
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		
		//pulsar botón ejecutar
		
		ejecutar.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
					
				JButton boton = (JButton) e.getSource();
				size = Integer.parseInt(cel.getText());
				g = Integer.parseInt(gen.getText());
				automata = new ca1DSimulator(size);
				automata.caComputation(g);
				
				/*for(int i=0;i<g;++i){
					for(int j=0;j<size;++j){
						System.out.print(automata.resultado[i][j]);}
				System.out.println();}*/
				
				//imagen = new BufferedImage(size, 15, BufferedImage.TYPE_BYTE_BINARY);
		
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
				automata = new ca1DSimulator(size);
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
				automata = new ca1DSimulator(size);
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
				automata = new ca1DSimulator(size);
				automata.caComputation(g);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						res.setText(Double.toString(automata.entropia_temp(celula_temp)));
						
					}
				});
				
			}		
				
			
		}); 		
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
