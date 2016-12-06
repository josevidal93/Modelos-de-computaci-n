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
 @author José Manuel Vidal Jiménez
 @version 2.0
 @since 27/11/2016
 
 */ 

public class Lifegraphic  {
	private static JFrame frame;
	private static JTextField iter;
	private static JTextField size_text;	
	private BufferedImage imagen; 
	private static JLabel picLabel;
	private static JButton execute_button, stop_button;
	private static JPanel panel;
	private static Life life_game;
	private static SwingWorker worker;
	private static int size,gen;
	private String mensaje;
	private JComboBox life_options;
	private int[] count;
	private static boolean stop = true;
	private GraphPanel2 graph;

	public Lifegraphic() {
		frame = new JFrame("Life Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		JPanel parametros = new JPanel(new GridLayout(9, 1));
		panel.add(parametros,BorderLayout.WEST);
		
		
		JLabel Size_label   = new JLabel("Size");
		
		size_text   = new JTextField("400");

		
		JLabel iteraciones = new JLabel("Iteraciones");

		JLabel iter = new JLabel("0");
		
		parametros.add(iteraciones);	
		parametros.add(iter);
		
		JLabel Generaciones = new JLabel("Generaciones");

		JTextField Gen = new JTextField("150");
		parametros.add(Generaciones);
			
		parametros.add(Gen);

		parametros.add(Size_label);
		parametros.add(size_text);
		
		execute_button = new JButton("Execute");

		parametros.add(execute_button);
		
		
		stop_button = new JButton("Stop");

		parametros.add(stop_button);

		String[] options = { "Random","Glider", "Tumbler", "10cellrow", "Explorer", "Blinker","lightweight spaceship","Gosper Glider Gun","SpaceShips" };


		life_options = new JComboBox(options);

		parametros.add(life_options);
		
		
		frame.getContentPane().add(panel);

	
		
		execute_button.addActionListener(new ActionListener( ) {
			public void actionPerformed( ActionEvent e )
			{
					
				
				String selected = (String) life_options.getSelectedItem();
				JButton boton = (JButton) e.getSource();
				size = Integer.parseInt(size_text.getText());
				gen = Integer.parseInt(Gen.getText());
				if(size<200) size =200;
				if(size>680) size =680;
				life_game = new Life(size,selected);
			
				worker = new SwingWorker<Void, Void>()
				{
					public Void doInBackground()
					{ 
						
						stop = false;
						int it=0,g=0;
						double[] graph_l = new double[gen];
						double[] graph_m = new double[gen];
						
						while(!stop && g <gen){

							
									life_game.nextGeneration();	
									imagen = convertir(life_game.getGrid());
									if (picLabel == null)
									{
										picLabel = new JLabel(new ImageIcon(imagen));
										panel.add(picLabel);
									}
									else{
										picLabel.setIcon(new ImageIcon(imagen));

									}
									panel.revalidate();
									panel.repaint();
									iter.setText(Integer.toString(it+1));
									count = life_game.counter();
									graph_l[it]=count[0];
									graph_m[it]=count[1];
									it++;
									g++;
											
						
						}
						
					graph.createAndShowGui(graph_l,graph_m,gen);
						
					return null;
				}
					protected void done()
					{
						stop = false;
					}
				};
				
				worker.execute();							
			}
		});   
			
		stop_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				stop = true;
			}
		});		
				
	}
	
	
	public static JPanel panel()
	{
		return panel;
	}
					
				
	public static BufferedImage convertir(boolean[][] matriz)
	{
        
        BufferedImage imagen = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_BINARY);
		for(int j=0;j<size;++j)
			for(int i=0;i<size;++i) {
			  
					if(matriz[j][i])
					{	
							imagen.setRGB(i, j, Color.WHITE.getRGB());
					}
		}    
        return imagen;
    }
    
	
	public static void main(String[] args) {
		Lifegraphic v = new Lifegraphic();
		
		
	}
}
