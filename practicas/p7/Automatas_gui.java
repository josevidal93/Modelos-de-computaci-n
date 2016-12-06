import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

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

public class Automatas_gui extends JPanel {
	
	private static JTextField size_text;	
	private BufferedImage imagen; 
	private static JLabel picLabel;
	private static JButton execute_button,stop_button;
	private static JPanel panel;
	private static Life life_game;
	private static SwingWorker worker;
	private static int size;
	private String mensaje;
	private JComboBox life_options;
	private boolean stop = true;
	private static JTextField iter;

    public Automatas_gui() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
		ca1DGraphicSimulation automatacelular = new ca1DGraphicSimulation();
        JComponent panel1 = automatacelular.panel();
        tabbedPane.addTab("1-D", null, panel1,"One dimension automata");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JTabbedPane tabbedPane2D = new JTabbedPane();
        tabbedPane.addTab("2-D", null, tabbedPane2D,"Two dimension automata");
        
        
   		Lifegraphic lifegame = new Lifegraphic();
        JComponent panel2 = lifegame.panel();
		belZabGraphic belzab_s = new belZabGraphic();
		JComponent panel3 = belzab_s.panel();
		JTabbedPane tabbedPaneBZ = new JTabbedPane();
		
		tabbedPane2D.addTab("Life", null, panel2,"Life Game");        
		tabbedPane2D.addTab("B-Z", null, tabbedPaneBZ,"Belousov-Zhabotinsky");
		
		parallelBelZabGraphic belzab_p = new parallelBelZabGraphic();
        JComponent panel4 = belzab_p.panel();

		
		tabbedPaneBZ.addTab("Secuencial",null,panel3,"Secuencial");
		tabbedPaneBZ.addTab("Paralelo",null,panel4,"Paralelo");
        
     
        add(tabbedPane);
        
      
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
    
	

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Automatas_gui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

   
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
         JFrame frame = new JFrame("Automatas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Automatas_gui();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.getContentPane().add(new Automatas_gui(),BorderLayout.WEST);

		//full screen
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
