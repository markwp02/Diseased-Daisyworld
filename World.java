import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.JSeparator;


import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.awt.SystemColor.*;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import static java.lang.Math.pow;



public class World {

     static final String infectList[] = {"0", "0.1", "0.15", "0.2"};
             
    JComboBox<String> infectComboBox;
    //Color color = new Color();
	JFrame frame = new JFrame();
    JFrame heatFrame = new JFrame();
    JPanel mainPanel = new JPanel();
    JPanel bpanel = new JPanel();
    JPanel rightpanel = new JPanel();
    
    JMenuBar menuBar = new JMenuBar();
    JMenu menuRun, menuStep, menuReset, menuInfection;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;
   // JCheckBoxMenuItem cbMenuItem;
   
   ButtonGroup group = new ButtonGroup();
    
	Sun sun = new Sun();
	int x,y;
	JButton[][] grid;
    JButton[][] heatGrid;
    JButton temp;
	Thing[][] gridThings;
	List<FertileSoil> fsoil = new ArrayList<FertileSoil>();
	List<InfertileSoil> isoil = new ArrayList<InfertileSoil>();
	List<WhiteDaisy> wdaisy = new ArrayList<WhiteDaisy>();

	List<BlackDaisy> bdaisy = new ArrayList<BlackDaisy>();

	double steadyState = 22.5;
	double globalTemp = -10;
	double birthrate, deathrate = 0.1, perWhite, ran, chance = 0;
    
    final double STEFAN_BOLTZMAN = 5.67*Math.pow(10,-8); //Joules/sec m^2 k^4
    final int SOLAR_FLUX = 917; // W/m^2
    final double EMISSIVITY = 0.96;
    
	/**
	 * @param x
	 * @param y
	 */
	public World(int x, int y){
	
    
    
    
		// get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 
		// get 2/3 of the height, and 2/3 of the width
		int height = screenSize.height * 1 / 2;
		int width = screenSize.width * 1 / 2;
		 
		// set the jframe height and width
		frame.setPreferredSize(new Dimension(width, height));
        heatFrame.setPreferredSize(new Dimension(width, height));
		
        JButton step = new JButton("Step");
        step.setBackground(new Color(235,235,235));
        step.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("step");
                step();
            }
        });
        
        JButton runButton = new JButton("Run");
        runButton.setBackground(new Color(235,235,235));
        
        runButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) 
            {
                //System.out.println("step");
                new Thread() {
                    public void run(){
                        try{
                        //  Thread.sleep(1000);
                            runWorld();
                        }catch (InterruptedException ie){
                            System.out.println(ie);
                        }
                    }
                }.start();
                
            }
        });
       
        JButton reset = new JButton("Reset");
        
        reset.setBackground(new Color(235,235,235));
        
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("reset");
            }
        });
        
        menuBar.add(runButton);
        menuBar.add(step);
        menuBar.add(reset);
        
        //menuBar.add(new JSeparator()); // SEPARATOR
        //menuBar.addSeparator();

        
       menuBar.setOpaque(true);
       menuBar.setBackground(new Color(235,235,235));
        menuBar.addSeparator();
        
        //Build the first menu.
        menuInfection = new JMenu("Infection Rate");
        
        
        
       // menuInfection.setOpaque(true);
       // menuInfection.setBackground(Color.CYAN);
        //menuInfection.setMnemonic(KeyEvent.VK_A);
        menuInfection.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
                
        menuBar.add(menuInfection);
        
        rbMenuItem = new JRadioButtonMenuItem("0");
        rbMenuItem.setSelected(true);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("0.1");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.2");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.3");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.4");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.5");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        
        
        frame.setJMenuBar(menuBar);
        
        frame.add(mainPanel);
        //frame.add(bpanel);
        mainPanel.add(bpanel);
       // mainPanel.add(rightpanel);
        
		this.x=x;
		this.y=y;
        //mainPanel.setLayout(new GridBagLayout());
        mainPanel.setLayout(new GridLayout());
        
        //GridBagConstraints c = new GridBagConstraints();
        
         //natural height, maximum width
        // c.fill = GridBagConstraints.HORIZONTAL;
         
         // c.weightx = 0.5;
        
       /// c.fill = GridBagConstraints.HORIZONTAL;
       // c.gridx = 0;
       // c.gridy = 0;
        
		bpanel.setLayout(new GridLayout(x, y));
       // rightpanel.setLayout(new GridLayout(4,1));
        
       //// rightpanel.add(step);
        //rightpanel.add(run);
        //rightpanel.add(new JLabel("Infect rate"));
       // infectComboBox = new JComboBox<>(infectList);
       // rightpanel.add(infectComboBox);
        
        /*step.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("step");
                step();
            }
        });
        */
        
        
 
        
        
        
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
//frame.setLocationRelativeTo(null);
        //frame.setLocationByPlatform(true);
		frame.setTitle("Diseased Daisyworld");
		frame.setVisible(true);
        
        heatFrame.setLayout(new GridLayout(x, y));
		heatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		heatFrame.pack();
        //heatFrame.setLocationByPlatform(true);
		heatFrame.setTitle("Heatmap of Daisyworld");
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int xWidth = (int) rect.getMaxX() - heatFrame.getWidth();
        //int yHeight = (int) rect.getMaxY() - heatFrame.getHeight();
        int yHeight = 0;
        heatFrame.setLocation(xWidth, yHeight);
        heatFrame.setVisible(true);
        
        
		heatFrame.setVisible(true);
		grid=new JButton[x][y];
        heatGrid = new JButton[x][y];
		gridThings = new Thing[x][y];
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				
				//JButton jb = new JButton("Black Daisy", new ImageIcon("images\\black.png"));
				//grid[i][j]=jb;
				ran = Math.random();
				//if(i%4==0){
				// equal chance of black/white daisies and fertile/infertile soil
				if(ran <= chance){
					BlackDaisy daisy = new BlackDaisy(globalTemp,i,j);
					grid[i][j] = daisy.jb;
					bdaisy.add(daisy);
					gridThings[i][j] = daisy;
					
				}
				//else if(i%4==1){
				else if(ran <= chance*2){	
					WhiteDaisy daisy = new WhiteDaisy(globalTemp,i,j);
					grid[i][j] = daisy.jb;
					wdaisy.add(daisy);
					gridThings[i][j] = daisy;
                   
				}
				//else if(i%4==2){
				//else if(ran <= chance*3)
                    else{
					FertileSoil soil = new FertileSoil(globalTemp,i,j);
					grid[i][j] = soil.jb;
					fsoil.add(soil);
					gridThings[i][j] = soil;
                    
				}
				//else{
				//	InfertileSoil soil = new InfertileSoil(globalTemp,i,j);
				//	grid[i][j] = soil.jb;
				//	isoil.add(soil);
				//	gridThings[i][j] = soil;
                    
				//}
				temp = new JButton();
                temp.setBackground(new Color(0,0,255));
                heatGrid[i][j] = temp;
				bpanel.add(grid[i][j]);
                heatFrame.add(heatGrid[i][j]);
			
			}
		}
        
       // Thing temp = gridThings[x/2][y/2];
        //temp.localTemp = 1000;
        
	}
	
	public void updateWhiteDaisies(){
		for(int idx=0;idx<wdaisy.size();idx++){
			WhiteDaisy tmp = wdaisy.get(idx);
			if(Math.random() <= deathrate || tmp.localTemp < 5 || tmp.localTemp > 40){
				tmp = wdaisy.remove(idx);
				
				//Daisy becomes fertile soil
				FertileSoil soil = new FertileSoil(tmp.localTemp,tmp.i,tmp.j);
				fsoil.add(soil);
				grid[soil.i][soil.j] = soil.jb;	
				///soil.jb.revalidate();
				//soil.jb.repaint();
				gridThings[soil.i][soil.j] = soil;
			}
			
		}
		//frame.revalidate();
		//frame.repaint();
	}
	
	public void updateBlackDaisies(){
		for(int idx=0;idx<bdaisy.size();idx++){
			BlackDaisy tmp = bdaisy.get(idx);
			if(Math.random() <= deathrate || tmp.localTemp < 5 || tmp.localTemp > 40){
				tmp = bdaisy.remove(idx);
				
				//Daisy becomes fertile soil
				FertileSoil soil = new FertileSoil(tmp.localTemp,tmp.i,tmp.j);
				fsoil.add(soil);
				grid[soil.i][soil.j] = soil.jb;
				
				gridThings[soil.i][soil.j]=soil;
			}
			
		}
		
		//frame.revalidate();
		//frame.repaint();
	}
	
	public void updateFertileSoil(){
		for(int idx=0;idx<fsoil.size();idx++){
			FertileSoil tmp = fsoil.get(idx);
			if(Math.random() <= growthRateDaisy(tmp.localTemp)){
				tmp = fsoil.remove(idx);
				
				perWhite = percentWhite(tmp.localTemp,steadyState);
				//perWhite = 1;
				//System.out.println(perWhite);
				if(Math.random() <= perWhite){
					//System.out.print("white ");
					WhiteDaisy daisy = new WhiteDaisy(tmp.localTemp,tmp.i,tmp.j);
					wdaisy.add(daisy);
					grid[tmp.i][tmp.j] = daisy.jb;
					gridThings[daisy.i][daisy.j]=daisy;
				}
				else{
					//System.out.println("black ");
					BlackDaisy daisy = new BlackDaisy(tmp.localTemp,tmp.i,tmp.j);
					bdaisy.add(daisy);
					grid[tmp.i][tmp.j] = daisy.jb;
					gridThings[daisy.i][daisy.j]=daisy;
				}
				
				
				
			}
		}
		
	}
	
	public void updateGrid(){
		
		//frame.getContentPane().removeAll();
		bpanel.removeAll();
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				bpanel.add(grid[i][j]);
			
			}
		}
		
		bpanel.revalidate();
		
		bpanel.repaint();
		 
	}
	
    public void updateHeatMap(){
		
		heatFrame.getContentPane().removeAll();
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				heatFrame.add(heatGrid[i][j]);
			
			}
		}
		
		heatFrame.revalidate();
		
		heatFrame.repaint();
		 
	}
    
    
    
	public void updateTemp(){
		double totalTemp = 0;
		double totalNum = 0;
		double localTemp, albedo;
		Thing tmp;
		double luminosity = sun.getLuminosity();
		double albedoIncrease;
		double absorbed;
        double energyEmmitted, energyAbsorbed, energyReceived, energyReflected;
        int surfaceArea = 1;
        double test;
        JButton heatColor;
		double diff;
		for(int j=0;j<y;j++){
			for(int i=0;i<x;i++){
				tmp = gridThings[i][j];
                albedo = tmp.getAlbedo();
                
                energyReceived = luminosity*SOLAR_FLUX;
                energyReflected = energyReceived*albedo;
                energyAbsorbed = energyReceived - energyReflected;
                energyEmmitted = energyAbsorbed;

                //rearrange Stefan-Boltzmann law
                test =energyEmmitted/(EMISSIVITY*STEFAN_BOLTZMAN*surfaceArea);
                tmp.localTemp = (Math.sqrt(Math.sqrt(test))) - 273;
               // System.out.println(tmp.localTemp);
                diff = averageNeighbours(gridThings,i,j,x-1,y-1) - tmp.localTemp;
                tmp.localTemp += 1/4*diff;

                
                
				// get average from neighbours
				//tmp.localTemp = weightedLocalTemp(gridThings,i,j,x,y);
				//tmp.localTemp = (averageNeighbours(gridThings,i,j,x-1,y-1) + 4*tmp.localTemp)/5;
                // update temp according to sun and albedo
				//absorbed = 1-tmp.albedo;
				//albedoIncrease = 2*absorbed;//changes to number between 1 and 2 
				//albedoIncrease = absorbed-0.5;
                //tmp.localTemp *= luminosity*albedoIncrease;
                //tmp.localTemp *=albedoIncrease;
                //tmp.localTemp += 1.45*luminosity;
                //System.out.println(diff);
              //  tmp.localTemp *= albedoIncrease;
                //tmp.localTemp += 1/4 * diff + 0.3*luminosity;
               // System.out.println(tmp.localTemp);
                if(tmp.localTemp < 5){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,0,255)); // navy blue
                        heatGrid[i][j] = heatColor;
                }    
                else if(tmp.localTemp < 15){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,255,255)); // light blue
                        heatGrid[i][j] = heatColor;
                }
                else if(tmp.localTemp < 25){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,255,0)); // green
                        heatGrid[i][j] = heatColor;
                }
                else if(tmp.localTemp < 35){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(255,179,0)); // orange
                        heatGrid[i][j] = heatColor;
                }                 
                else if(tmp.localTemp < 40){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(255,0,0)); // red
                        heatGrid[i][j] = heatColor;
                }                  
                else{
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,0,0));
                        heatGrid[i][j] = heatColor;
                }                    
				//System.out.print(tmp.localTemp+" ");
			}
			//System.out.println();
		}
		
		for(int idx=0;idx<wdaisy.size();idx++){
			localTemp = wdaisy.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
		for(int idx=0;idx<bdaisy.size();idx++){
			localTemp = bdaisy.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
		for(int idx=0;idx<fsoil.size();idx++){
			localTemp = fsoil.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
		for(int idx=0;idx<isoil.size();idx++){
			localTemp = isoil.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}

		this.globalTemp = totalTemp / totalNum;
}
	
    public void step(){
        //System.out.println("Step");
        updateFertileSoil();
        updateWhiteDaisies();
		updateBlackDaisies();
		updateGrid();
		sun.updateSun();
		updateTemp();
        updateHeatMap();
        
    }
    
    /**
	 * @param args
	 * @throws InterruptedException 
	 */
    public void runWorld() throws InterruptedException{
        
        for(int i = 1; i<200;i++){
			Thread.sleep(100);
            step();
            System.out.println("Step: " + i);
            System.out.println("white "+wdaisy.size());
            System.out.println("black " +bdaisy.size());
            System.out.println(globalTemp+"\n");
        }
    }
    
	public static double percentWhite(double localTemp, double steadyState){
		double diff = localTemp - steadyState;
		//System.out.println(diff);
		double percent = 2*diff/100;
		if(percent >= 0.5)
			return 1;
		else if(percent <= -0.5)
			return 0;
		else
			return 0.5 + percent;
	}
	
	public static double growthRateDaisy(double temp){
		return 1-0.003265*Math.pow(22.5-temp, 2);
	}
	
    public static double averageNeighbours(Thing[][] gridThings, int i, int j, int xMax, int YMax){
        double total = 0;
        //Cell above
        if(i == xMax){
            total = gridThings[0][j].localTemp;
        }else{
            total = gridThings[i+1][j].localTemp;
        }
        //Cell below
        if(i == 0){
            total +=  gridThings[xMax][j].localTemp;
        }
        else{
            total += gridThings[i-1][j].localTemp;
        }
        //Cell to left
        if(j == 0){
            total += gridThings[i][YMax].localTemp;
        }
        else{
            total += gridThings[i][j-1].localTemp;
        }
        //Cell to right
        if(j == YMax){
            total += gridThings[i][0].localTemp;
        }
        else{
            total += gridThings[i][j+1].localTemp;
        }
        return total / 4;
            
    }
    
	
    
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		World world = new World(25,25);
        world.updateGrid();
        world.updateHeatMap();
        //world.gridThings[5,5].localTemp = 1000;
       // grid.updateGrid();
        
		//grid.updateGrid();
			for(int i = 1; i<2;i++){
				//Thread.sleep(100);
				
				// Scanner scan= new Scanner(System.in);
				// String text= scan.nextLine();
				
				//world.updateFertileSoil();
				//world.updateWhiteDaisies();
				//world.updateBlackDaisies();
				//world.updateGrid();
				//world.sun.updateSun();
				//world.updateTemp();
              //  world.updateHeatMap();
                
        
				System.out.println("Step: " + i);
				System.out.println("white "+world.wdaisy.size());
				System.out.println("black " +world.bdaisy.size());
				System.out.println(world.globalTemp+"\n");
			}
	}

}
