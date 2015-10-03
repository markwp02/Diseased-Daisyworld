import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import static java.lang.Math.pow;


/**
* World object, contains all of Diseased Daisyworld. Diseased Daisyworld is an agent based
* extension to the Lovelock and Watson mathematical model, with the added perturbation of a 
* bacterial disease added to see how it will affect the global environment
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class World {

    Sun sun;
    JComboBox<String> infectComboBox;
	JFrame frame = new JFrame(), heatFrame = new JFrame();
    JPanel mainPanel = new JPanel(), bpanel = new JPanel();
    
    JMenuBar menuBar = new JMenuBar();
    JMenu menuInfection;
    JRadioButtonMenuItem rbMenuItem; 
    ButtonGroup group = new ButtonGroup();
    
	int x,y, count;
	JButton[][] grid, heatGrid;
    JButton temp, step, runButton, stop, reset;
	Thing[][] gridThings, updateGridThings;
	List<Ground> bground;
	List<WhiteDaisy> wdaisy; 
    List<BlackDaisy> bdaisy; 
    List<DiseasedDaisy> ddaisy;
    String text;
	
	double deathrate = 0.05 , diseasedDeathrate = 2*deathrate, perWhite;
    final double INIT_TEMP = -10, STEFAN_BOLTZMAN = 5.6696*Math.pow(10,-8), DIFFUSION = 0.025, INTRODUCE_DISEASE = 0.0001;
    final int DEGREE_TO_KELVIN = 273, C = 50, SOLAR_FLUX = 917;  
    
    double globalTemp = INIT_TEMP;
    static int stepNum = 0;
    static boolean halt;
    
	/**
	 * @param x rows of grid
	 * @param y columns of grid
	 */
	public World(int x, int y){
		initGraphics(x,y);
		initDaisyWorld();
		
	}
	
	/**
	* @param buttonGroup radio button group of infection rate
	* @return String infection rate chosen
	*/
	public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
	
	/**
	* Sets the initial conditions for Daisyworld. Luminosity is reset to 0.6. Global temp is set back to
	* the initial temp and the world is populated with bare ground.
	*/
	private void initDaisyWorld(){
        text = "";
		stepNum = 1;
        sun = new Sun();
		bground = new ArrayList<Ground>();
        wdaisy = new ArrayList<WhiteDaisy>();
        bdaisy = new ArrayList<BlackDaisy>();
        ddaisy = new ArrayList<DiseasedDaisy>();
        globalTemp = INIT_TEMP;
        for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				Ground ground = new Ground(globalTemp + DEGREE_TO_KELVIN,i,j);
				grid[i][j] = ground.jb;
				bground.add(ground);
				gridThings[i][j] = ground;
                updateGridThings[i][j] = ground;
				temp = new JButton();
                temp.setBackground(new Color(0,0,255));
                heatGrid[i][j] = temp;
							
			}
		}
        
        updateGrid();
        updateHeatMap();
    }
	
	/**
	* Sets up 2 frames, one for Daisyworld and one for the heatmap.
	* @param x num rows
	* @param y num columns
	*/
	private void initGraphics(int x, int y){
		
		// get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 
		// get 2/3 of the height, and 2/3 of the width
		int height = screenSize.height * 1 / 2;
		int width = screenSize.width * 1 / 2;
		 
		// set the jframe height and width
		frame.setPreferredSize(new Dimension(width, height));
        heatFrame.setPreferredSize(new Dimension(width, height));
		
        step = new JButton("Step");
        step.setBackground(new Color(235,235,235));
        step.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                step();
            }
        });
        
        runButton = new JButton("Run");
        runButton.setBackground(new Color(235,235,235));
        
        runButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run(){
                        try{
                            runWorld();
                        }catch (InterruptedException ie){
                            System.out.println(ie);
                        }
                    }
                }.start();
                
            }
        });
       
        reset = new JButton("Reset");
        
        reset.setBackground(new Color(235,235,235));
        
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                initDaisyWorld();
            }
        });        
        stop = new JButton("Stop");
        stop.setBackground(new Color(235,235,235));  
        menuBar.add(runButton);
        menuBar.add(step);
        menuBar.add(reset);
        menuBar.add(stop);
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(235,235,235)); 
        menuInfection = new JMenu("Infection Rate");    
        menuBar.add(menuInfection);
        JButton writeToFile = new JButton("Write to file");      
        writeToFile.setBackground(new Color(235,235,235));      
        writeToFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    write();
                }catch(IOException ioe){
                    System.out.println(ioe);
                }catch(InterruptedException ie){
                    System.out.println(ie);
                }
                
            }
        });
        
        menuBar.add(writeToFile);
        
        rbMenuItem = new JRadioButtonMenuItem("0");
        rbMenuItem.setSelected(true);
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("0.1");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.2");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.3");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.4");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.5");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.6");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.7");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.8");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("0.9");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        rbMenuItem = new JRadioButtonMenuItem("1.0");
        group.add(rbMenuItem);
        menuInfection.add(rbMenuItem);
        
        frame.setJMenuBar(menuBar);
        
        frame.add(mainPanel);
        mainPanel.add(bpanel);
        
		this.x=x;
		this.y=y;
        mainPanel.setLayout(new GridLayout());
		bpanel.setLayout(new GridLayout(x, y));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setTitle("Diseased Daisyworld");
		frame.setVisible(true);       
        heatFrame.setLayout(new GridLayout(x, y));
		heatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		heatFrame.pack();
		heatFrame.setTitle("Heatmap of Daisyworld");
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int xWidth = (int) rect.getMaxX() - heatFrame.getWidth();
        int yHeight = 0;
        heatFrame.setLocation(xWidth, yHeight);
        heatFrame.setVisible(true);
      
		heatFrame.setVisible(true);
		grid=new JButton[x][y];
        heatGrid = new JButton[x][y];
		gridThings = new Thing[x][y];
        updateGridThings = new Thing[x][y];
	}
	
    
    /**
	* Determines if a black, or white daisy is infected from one of its' four neighbours
	* @param idx index of daisy in linked list
	* @param i row of daisy
	* @param j column of daisy
	* @param white true if white daisy, false if black
	* @return true if infected, false otherwise
	*/
    public boolean infectedByNeighbours(int idx, int i, int j, boolean white){
        
        boolean up,down,left,right,above,below;
        Daisy tmp;
        
        //Cell above
        if(i == x-1){
            above = gridThings[0][j].isInfected();
        }else{
            above = gridThings[i+1][j].isInfected();
        }
        //Cell below
        if(i == 0){
            below = gridThings[x-1][j].isInfected();
        }
        else{
            below = gridThings[i-1][j].isInfected();
        }
        //Cell to left
        if(j == 0){
            left = gridThings[i][y-1].isInfected();
        }
        else{
            left = gridThings[i][j-1].isInfected();
        }
        //Cell to right
        if(j == y-1){
            right = gridThings[i][0].isInfected();
        }
        else{
            right = gridThings[i][j+1].isInfected();
        }
        if(above||below||left||right){
            double infectRate = Double.parseDouble(getSelectedButtonText(group));
            return Math.random() < infectRate;
        }
        return false;
    }
    
	/**
	* Updates white daisy, can die, remain, or become diseased
	**/
	public void updateWhiteDaisies(){
       
		for(int idx=0;idx<wdaisy.size();idx++){
			WhiteDaisy tmp = wdaisy.get(idx);
			if(Math.random() <= deathrate || tmp.localTemp - DEGREE_TO_KELVIN < 5 || tmp.localTemp - DEGREE_TO_KELVIN > 40){
				tmp = wdaisy.remove(idx);
				
				//Daisy becomes bare ground
				Ground ground = new Ground(tmp.localTemp,tmp.i,tmp.j);
				bground.add(ground);
				grid[ground.i][ground.j] = ground.jb;	
				
				updateGridThings[ground.i][ground.j] = ground;
                                
			}
            else if(Math.random() < INTRODUCE_DISEASE || infectedByNeighbours(idx,tmp.i,tmp.j,true)){
                tmp = wdaisy.remove(idx);
                DiseasedDaisy daisy = new DiseasedDaisy(tmp.localTemp,tmp.i,tmp.j);
                ddaisy.add(daisy);
                grid[daisy.i][daisy.j] = daisy.jb;	
				updateGridThings[daisy.i][daisy.j] = daisy;
            }
		}	
	}
	
	/**
	* Updates white daisy, can die, remain, or become diseased
	**/
	public void updateBlackDaisies(){
		for(int idx=0;idx<bdaisy.size();idx++){
			BlackDaisy tmp = bdaisy.get(idx);
			if(Math.random() <= deathrate || tmp.localTemp - DEGREE_TO_KELVIN < 5 || tmp.localTemp - DEGREE_TO_KELVIN > 40){
				tmp = bdaisy.remove(idx);
				Ground ground = new Ground(tmp.localTemp,tmp.i,tmp.j);
				bground.add(ground);
				grid[ground.i][ground.j] = ground.jb;
				updateGridThings[ground.i][ground.j]=ground;  
			}
            else if(Math.random() < INTRODUCE_DISEASE || infectedByNeighbours(idx,tmp.i,tmp.j,false)){
                tmp = bdaisy.remove(idx);
                DiseasedDaisy daisy = new DiseasedDaisy(tmp.localTemp,tmp.i,tmp.j);
                ddaisy.add(daisy);
                grid[daisy.i][daisy.j] = daisy.jb;	
				updateGridThings[daisy.i][daisy.j] = daisy;
            }			
		}		
	}
	
	/**
	* Updates diseased daisy, can die, or remain unchanged
	**/
    public void updateDiseasedDaisies(){
        for(int idx=0;idx<ddaisy.size();idx++){
			DiseasedDaisy tmp = ddaisy.get(idx);
			if(Math.random() <= diseasedDeathrate || tmp.localTemp - DEGREE_TO_KELVIN < 5 || tmp.localTemp - DEGREE_TO_KELVIN > 40){
				tmp = ddaisy.remove(idx);				
				//Daisy becomes bare ground
				Ground ground = new Ground(tmp.localTemp,tmp.i,tmp.j);
				bground.add(ground);
				grid[ground.i][ground.j] = ground.jb;
				updateGridThings[ground.i][ground.j]=ground;
			}        
		}
    }

	/**
	* Ground can grow a black, or white daisy, or remain unchanged
	**/
	public void updateGround(){
		for(int idx=0;idx<bground.size();idx++){
			Ground tmp = bground.get(idx);
			if(Math.random() <= growthRateDaisy(tmp.localTemp-DEGREE_TO_KELVIN)){
				tmp = bground.remove(idx);
				
				perWhite = percentWhite(tmp.localTemp - DEGREE_TO_KELVIN);
				if(Math.random() <= perWhite){
					WhiteDaisy daisy = new WhiteDaisy(tmp.localTemp,tmp.i,tmp.j);
					wdaisy.add(daisy);
					grid[tmp.i][tmp.j] = daisy.jb;
					updateGridThings[daisy.i][daisy.j]=daisy;
				}
				else{
					BlackDaisy daisy = new BlackDaisy(tmp.localTemp,tmp.i,tmp.j);
					bdaisy.add(daisy);
					grid[tmp.i][tmp.j] = daisy.jb;
					updateGridThings[daisy.i][daisy.j]=daisy;
				}												
			}
		}		
	}
	
	/**
	* Updates the grid of daisies and bare ground
	**/
	public void updateGrid(){
		bpanel.removeAll();
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				gridThings[i][j] = updateGridThings[i][j];
                bpanel.add(grid[i][j]);               
			}
		}		
		bpanel.revalidate();		
		bpanel.repaint();		 
	}	
    
	/**
	* Updates the heat map
	**/
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
    
    /**
	* Updates temperature based on Von Bloh equation
	**/
	public void updateTemp(){
		double totalTemp = 0;
		double totalNum = 0;
		double localTemp, albedo;
		Thing tmp;
		double luminosity = sun.getLuminosity();
		double albedoIncrease;
		double absorbed;
        double energyEmmitted, energyAbsorbed, energyReceived, energyReflected, energy;
        int surfaceArea = 1;
        double tPow4;
        JButton heatColor;
		double diff;
        double qPrime = 0.2;

		for(int j=0;j<y;j++){
			for(int i=0;i<x;i++){
				tmp = updateGridThings[i][j];
                albedo = tmp.getAlbedo();
 
               double diffusion = (DIFFUSION*difference(gridThings,i,j,x-1,y-1))*tmp.localTemp/C;
               double sunEnergy  = (SOLAR_FLUX*luminosity*(1-albedo)-STEFAN_BOLTZMAN*Math.pow(tmp.localTemp,4))/C;
               double change = diffusion + sunEnergy;

               tmp.newTemp += change;
           
                if(tmp.localTemp - DEGREE_TO_KELVIN < 5){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,0,255)); // navy blue
                        heatGrid[i][j] = heatColor;
                }    
                else if(tmp.localTemp - DEGREE_TO_KELVIN < 15){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,255,255)); // light blue
                        heatGrid[i][j] = heatColor;
                }
                else if(tmp.localTemp - DEGREE_TO_KELVIN < 25){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,255,0)); // green
                        heatGrid[i][j] = heatColor;
                }
                else if(tmp.localTemp - DEGREE_TO_KELVIN < 35){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(255,179,0)); // orange
                        heatGrid[i][j] = heatColor;
                }                 
                else if(tmp.localTemp - DEGREE_TO_KELVIN < 40){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(255,0,0)); // red
                        heatGrid[i][j] = heatColor;
                }                  
                else{
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,0,0)); // black
                        heatGrid[i][j] = heatColor;
                }                    
			}
		}
		
		for(int idx=0;idx<wdaisy.size();idx++){
			wdaisy.get(idx).localTemp = wdaisy.get(idx).newTemp;
            localTemp = wdaisy.get(idx).localTemp;
			totalTemp += localTemp;
			totalNum++;
		}
		for(int idx=0;idx<bdaisy.size();idx++){
            bdaisy.get(idx).localTemp = bdaisy.get(idx).newTemp;

			localTemp = bdaisy.get(idx).localTemp;
			totalTemp += localTemp;
			totalNum++;
		}
		for(int idx=0;idx<bground.size();idx++){
			bground.get(idx).localTemp = bground.get(idx).newTemp;

            localTemp = bground.get(idx).localTemp;
			totalTemp += localTemp;
			totalNum++;
		}
		for(int idx=0;idx<ddaisy.size();idx++){
			ddaisy.get(idx).localTemp = ddaisy.get(idx).newTemp;

            localTemp = ddaisy.get(idx).localTemp;
			totalTemp += localTemp;
			totalNum++;
		}
		this.globalTemp = totalTemp / totalNum - DEGREE_TO_KELVIN;
	}

	/**
	* Updates each individual type of daisy
	**/
	public void updateDaisies(){
		updateBlackDaisies();
		updateWhiteDaisies();
		updateDiseasedDaisies();
	}
	
	/**
	* Takes one step, allows for convergence if daisies exist
	**/
	public void step(){
	
		if(bdaisy.size()+wdaisy.size()+ddaisy.size() == 0)
			stepNoDaisies();
		else
			stepConverge();
    }
    
	/**
	* Do not need to update daisies and do not need to allow for convergence
	* as there is nothing to regulate the global temperature
	**/
	public void stepNoDaisies(){
		updateGround();
		sun.updateSun();
		updateTemp();
		updateHeatMap();
		updateGrid();
		System.out.println("step: "+stepNum++);
        System.out.println("white "+wdaisy.size());
        System.out.println("black " +bdaisy.size());
        System.out.println("diseased " + ddaisy.size());
        System.out.println(globalTemp+ " " +sun.getLuminosity() + "\n");
		text+=globalTemp+",";  
	}
    
	/**
	* Loops through 10 times to allow the global temperature to converge by
	* balancing the number of black and white daisies
	**/
    public void stepConverge(){

        for(int i=0;i<10;i++){
 
			updateGround();
			updateDaisies();
            updateTemp();
            updateHeatMap();
            updateGrid();	
           
        }
        sun.updateSun();
		System.out.println("step: "+stepNum++);
		System.out.println("white "+wdaisy.size());
        System.out.println("black " +bdaisy.size());
        System.out.println("diseased " + ddaisy.size());
        System.out.println(globalTemp+ " " +sun.getLuminosity() + "\n");
        text+=globalTemp+",";
        
    }
    
    /**
	 * Two loops, first run until daisies, second run until no daisies
	 * @throws InterruptedException 
	 */
    public void runWorld() throws InterruptedException{
        halt = false;
		//while(stepNum < 150){
		while(bdaisy.size()+wdaisy.size()+ddaisy.size() == 0){
			if(halt)
                break;
            stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                World.halt = true;
            }
            });
            
            Thread.sleep(100);
            stepNoDaisies();
		}
		while(bdaisy.size()+wdaisy.size()+ddaisy.size()>0){
		if(halt)
                break;
            stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                World.halt = true;
            }
            });
            
            Thread.sleep(100);
            stepConverge();
		}	
		
        text += "\n";
    }
    
    
    /**
	* Write to file global temp at each step
	* Different file for each infection rate
	**/
    public void write() throws IOException, InterruptedException{
        try{
			File theDir = new File("data");
    		// if the directory does not exist, create it
        	if (!theDir.exists()) {
        	    theDir.mkdir();
        	}
			FileWriter out = new FileWriter("./data//"+getSelectedButtonText(group)+".txt",true);
            out.write(text);
			out.close();
            text = "";
        }catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(SecurityException se){
    		se.printStackTrace();
    	}
    }
    
	/**
	* Difference equation to calculate the Laplace equation
	* @param gridThings grid that contains all daisies and bare ground
	* @param i row of selected daisy, or ground
	* @param j column of selected daisy, or ground
	* @param xMax max number of rows in grid
	* @param YMax max number of columns in grid
	* @return double solution to difference equation
	**/
	public static double difference(Thing[][] gridThings, int i, int j, int xMax, int YMax){
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

        total -= 4*gridThings[i][j].localTemp;
        return total;
            
    }
	
	/**
	* Growth rate equation from Watson and Lovelock
	* @param temp temperature of daisy, or ground
	* @return double decimal probability of growing a daisy
	**/
	public static double growthRateDaisy(double temp){
		return 1-0.003265*Math.pow(22.5-temp, 2);
	}

	/**
	* Probability of growing a white daisy, as a linear equation 
	* @param temp temperature of daisy, or ground
	* @return double decimal probability of growing a white daisy
	**/
	public static double percentWhite(double localTemp){
		return localTemp/35 - 1/7;
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		World world = new World(35,35);
       
	}

}
