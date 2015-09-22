import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import static java.lang.Math.pow;



public class World {

    Sun sun;
    JComboBox<String> infectComboBox;
	JFrame frame = new JFrame();
    JFrame heatFrame = new JFrame();
    JPanel mainPanel = new JPanel();
    JPanel bpanel = new JPanel();
    JPanel rightpanel = new JPanel();
    
    JMenuBar menuBar = new JMenuBar();
    JMenu menuInfection;
    JRadioButtonMenuItem rbMenuItem;
   
   ButtonGroup group = new ButtonGroup();
    
	
	int x,y;
	JButton[][] grid;
    JButton[][] heatGrid;
    JButton temp;
	Thing[][] gridThings, updateGridThings;
	List<FertileSoil> fsoil;
	//List<InfertileSoil> isoil; = new ArrayList<InfertileSoil>();
	List<WhiteDaisy> wdaisy; 
    List<BlackDaisy> bdaisy; 
    List<DiseasedDaisy> ddaisy;
    String text;
	
	double birthrate, perWhite, ran, chance = 0;
    double deathrate = 0.1;
    final double INIT_TEMP = 0;
    //final double INIT_TEMP = 22.5;
    final double STEADY_STATE = 22.5;
    final int DEGREE_TO_KELVIN = 273;
    //final double STEFAN_BOLTZMAN = 5.67*Math.pow(10,-8); //Joules/sec m^2 k^4
    final double STEFAN_BOLTZMAN = 5.6696*Math.pow(10,-8);
    final int DIFFUSION = 500;
    final int C = 2500;
    final int SOLAR_FLUX = 917; // W/m^2
    final double EMISSIVITY = 0.96;
    
    double globalTemp = INIT_TEMP;
    
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
            public void actionPerformed(ActionEvent e){
                step();
            }
        });
        
        JButton runButton = new JButton("Run");
        runButton.setBackground(new Color(235,235,235));
        
        runButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
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
            public void actionPerformed(ActionEvent e){
                initDaisyWorld();
                //System.out.println(gridThings[1][1].localTemp);
            }
        });
        
        JButton hot = new JButton("hot");
        
        reset.setBackground(new Color(235,235,235));
        
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                updateGridThings[0][0].localTemp = 1000;
            }
        });
        
        menuBar.add(runButton);
        menuBar.add(step);
        menuBar.add(reset);
        menuBar.add(hot);
        
       

        
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
        //frame.add(bpanel);
        mainPanel.add(bpanel);
       // mainPanel.add(rightpanel);
        
		this.x=x;
		this.y=y;
        //mainPanel.setLayout(new GridBagLayout());
        mainPanel.setLayout(new GridLayout());
        
        
        
       
        
		bpanel.setLayout(new GridLayout(x, y));
       
        
 
        
        
        
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
        updateGridThings = new Thing[x][y];
		initDaisyWorld();
		
	}
	
    private void initDaisyWorld(){
        text = "";
        sun = new Sun();
        fsoil = new ArrayList<FertileSoil>();
        wdaisy = new ArrayList<WhiteDaisy>();
        bdaisy = new ArrayList<BlackDaisy>();
        ddaisy = new ArrayList<DiseasedDaisy>();
        globalTemp = INIT_TEMP;
        for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				
				FertileSoil soil = new FertileSoil(globalTemp + DEGREE_TO_KELVIN,i,j);
				grid[i][j] = soil.jb;
				fsoil.add(soil);
				gridThings[i][j] = soil;
                updateGridThings[i][j] = soil;
                
                //WhiteDaisy daisy = new WhiteDaisy(globalTemp,i,j);
               // grid[i][j] = daisy.jb;
               // wdaisy.add(daisy);
               // gridThings[i][j] = daisy;
               // updateGridThings[i][j] = daisy;
                 //DiseasedDaisy daisy = new DiseasedDaisy(globalTemp,i,j,0);
                // grid[i][j]=daisy.jb;
				
				temp = new JButton();
                temp.setBackground(new Color(0,0,255));
                heatGrid[i][j] = temp;
							
			}
		}
        //gridThings[x/2][x/2].localTemp = 1000;
       // updateGridThings[x/2][x/2].localTemp = 1000;
        
        //DiseasedDaisy daisy = new DiseasedDaisy(globalTemp,x-1,y-1,1);
       // grid[x-1][y-1]=daisy.jb;
       // ddaisy.add(daisy);
       // gridThings[x-1][y-1] = daisy;
       // updateGridThings[x-1][y-1] = daisy;
        
        updateGrid();
        updateHeatMap();
    }
    
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
        //System.out.println(i +","+j+" "+above+" "+below+" "+left +" " + right);
        if(above||below||left||right){
            double infectRate = Double.parseDouble(getSelectedButtonText(group));
           // System.out.println(infectRate + " " + i +","+j);
            return Math.random() < infectRate;
           /* if(Math.random() < infectRate){
                System.out.println("Here");
                if(white)
                    tmp = wdaisy.remove(idx);
                else
                    tmp = bdaisy.remove(idx);
                DiseasedDaisy daisy = new DiseasedDaisy(tmp.localTemp,i,j,infectRate);
                ddaisy.add(daisy);
                grid[daisy.i][daisy.j] = daisy.jb;	
				gridThings[daisy.i][daisy.j] = daisy;
            }*/
        }
        return false;
    }
    
	public void updateWhiteDaisies(){
       
		for(int idx=0;idx<wdaisy.size();idx++){
			WhiteDaisy tmp = wdaisy.get(idx);
			if(Math.random() <= deathrate || tmp.localTemp - DEGREE_TO_KELVIN < 5 || tmp.localTemp - DEGREE_TO_KELVIN > 40){
				tmp = wdaisy.remove(idx);
				
				//Daisy becomes fertile soil
				FertileSoil soil = new FertileSoil(tmp.localTemp,tmp.i,tmp.j);
				fsoil.add(soil);
				grid[soil.i][soil.j] = soil.jb;	
				
				updateGridThings[soil.i][soil.j] = soil;
                                
			}
            else if(Math.random() < 0.0 || infectedByNeighbours(idx,tmp.i,tmp.j,true)){
                tmp = wdaisy.remove(idx);
                DiseasedDaisy daisy = new DiseasedDaisy(tmp.localTemp,tmp.i,tmp.j,Double.parseDouble(getSelectedButtonText(group)));
                ddaisy.add(daisy);
                grid[daisy.i][daisy.j] = daisy.jb;	
				updateGridThings[daisy.i][daisy.j] = daisy;
            }
           

			
		}
		
	}
	
	public void updateBlackDaisies(){
		for(int idx=0;idx<bdaisy.size();idx++){
			BlackDaisy tmp = bdaisy.get(idx);
			if(Math.random() <= deathrate || tmp.localTemp - DEGREE_TO_KELVIN < 5 || tmp.localTemp - DEGREE_TO_KELVIN > 40){
				tmp = bdaisy.remove(idx);
				
				//Daisy becomes fertile soil
				FertileSoil soil = new FertileSoil(tmp.localTemp,tmp.i,tmp.j);
				fsoil.add(soil);
				grid[soil.i][soil.j] = soil.jb;
				
				updateGridThings[soil.i][soil.j]=soil;  
			}
            else if(Math.random() < 0.0 || infectedByNeighbours(idx,tmp.i,tmp.j,false)){
                tmp = bdaisy.remove(idx);
                DiseasedDaisy daisy = new DiseasedDaisy(tmp.localTemp,tmp.i,tmp.j,Double.parseDouble(getSelectedButtonText(group)));
                ddaisy.add(daisy);
                grid[daisy.i][daisy.j] = daisy.jb;	
				updateGridThings[daisy.i][daisy.j] = daisy;
            }
			

		}
		
		
	}
	
    public void updateDiseasedDaisies(){
        for(int idx=0;idx<ddaisy.size();idx++){
			DiseasedDaisy tmp = ddaisy.get(idx);
			if(Math.random() <= deathrate/2 || tmp.localTemp - DEGREE_TO_KELVIN < 5 || tmp.localTemp - DEGREE_TO_KELVIN > 40){
				tmp = ddaisy.remove(idx);
				
				//Daisy becomes fertile soil
				FertileSoil soil = new FertileSoil(tmp.localTemp,tmp.i,tmp.j);
				fsoil.add(soil);
				grid[soil.i][soil.j] = soil.jb;
				
				updateGridThings[soil.i][soil.j]=soil;
			}
            
		}
    }
    
	public void updateFertileSoil(){
		for(int idx=0;idx<fsoil.size();idx++){
			FertileSoil tmp = fsoil.get(idx);
			if(Math.random() <= growthRateDaisy(tmp.localTemp-DEGREE_TO_KELVIN)){
				tmp = fsoil.remove(idx);
				
				perWhite = percentWhite(tmp.localTemp - DEGREE_TO_KELVIN,STEADY_STATE);
				//perWhite = 1;
				//System.out.println(perWhite);
				if(Math.random() <= perWhite){
					//System.out.print("white ");
					WhiteDaisy daisy = new WhiteDaisy(tmp.localTemp,tmp.i,tmp.j);
					wdaisy.add(daisy);
					grid[tmp.i][tmp.j] = daisy.jb;
					updateGridThings[daisy.i][daisy.j]=daisy;
				}
				else{
					//System.out.println("black ");
					BlackDaisy daisy = new BlackDaisy(tmp.localTemp,tmp.i,tmp.j);
					bdaisy.add(daisy);
					grid[tmp.i][tmp.j] = daisy.jb;
					updateGridThings[daisy.i][daisy.j]=daisy;
				}
				
				
				
			}
		}
		
	}
	
	public void updateGrid(){
		
		//frame.getContentPane().removeAll();
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
        double energyEmmitted, energyAbsorbed, energyReceived, energyReflected, energy;
        int surfaceArea = 1;
        double tPow4;
        JButton heatColor;
		double diff;
        double qPrime = 0.2;
        //double albedoWorld = (0.25*wdaisy.size() + 0.75*bdaisy.size() + 0.5*ddaisy.size() + 0.5*fsoil.size() ) / (wdaisy.size() + bdaisy.size() + fsoil.size());
       // System.out.println(albedoWorld);
       // energy = luminosity*SOLAR_FLUX*(1-albedoWorld);
       // tPow4 =energy/STEFAN_BOLTZMAN;
       // globalTemp = (Math.sqrt(Math.sqrt(tPow4))) - 273;
        
		for(int j=0;j<y;j++){
			for(int i=0;i<x;i++){
				tmp = updateGridThings[i][j];
                albedo = tmp.getAlbedo();
                
                //energyReceived = luminosity*SOLAR_FLUX;
                //energyReflected = energyReceived*albedo;
               // energyAbsorbed = energyReceived - energyReflected;
               // energyEmmitted = energyAbsorbed;
               // energy = luminosity*SOLAR_FLUX*(1-albedo);
                
                //rearrange Stefan-Boltzmann law to get temp to the power of 4
               // tPow4 =energy/STEFAN_BOLTZMAN;
                // convert to degrees Celsius
               // tmp.localTemp = (Math.sqrt(Math.sqrt(tPow4))) - 273;
              //  tmp.localTemp = qPrime*(albedoWorld-albedo) + globalTemp;
                //System.out.println(tmp.localTemp);
               // diff = averageNeighbours(updateGridThings,i,j,x-1,y-1) - tmp.localTemp;
               // tmp.localTemp += 1/4*diff;

               //System.out.println(tmp.localTemp);
               double diffusion = (DIFFUSION*difference(gridThings,i,j,x-1,y-1))/C;
               double sunEnergy  = (SOLAR_FLUX*luminosity*(1-albedo)-STEFAN_BOLTZMAN*Math.pow(tmp.localTemp,4))/C;
               double change = diffusion + sunEnergy;

               tmp.newTemp += change;
               // System.out.println(diffusion + " " + sunEnergy + " " +change + " " + tmp.newTemp);
                //System.out.println(change + " " + tmp.newTemp);
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
                        heatColor.setBackground(new Color(0,0,0));
                        heatGrid[i][j] = heatColor;
                }                    
				//System.out.print(tmp.localTemp+" ");
			}
			//System.out.println();
		}
		
		for(int idx=0;idx<wdaisy.size();idx++){
			wdaisy.get(idx).localTemp = wdaisy.get(idx).newTemp;
            localTemp = wdaisy.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
		for(int idx=0;idx<bdaisy.size();idx++){
            bdaisy.get(idx).localTemp = bdaisy.get(idx).newTemp;

			localTemp = bdaisy.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
		for(int idx=0;idx<fsoil.size();idx++){
			fsoil.get(idx).localTemp = fsoil.get(idx).newTemp;

            localTemp = fsoil.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
		for(int idx=0;idx<ddaisy.size();idx++){
			ddaisy.get(idx).localTemp = ddaisy.get(idx).newTemp;

            localTemp = ddaisy.get(idx).localTemp;
			totalTemp += localTemp;
			//System.out.println(localTemp);
			totalNum++;
		}
        //System.out.println(totalTemp/totalNum);
		this.globalTemp = totalTemp / totalNum - DEGREE_TO_KELVIN;
}
	
    public void step(){

    
        updateFertileSoil();
        updateWhiteDaisies();
		updateBlackDaisies();
        updateDiseasedDaisies();
		
		sun.updateSun();
		updateTemp();
        updateHeatMap();
        updateGrid();
        System.out.println("white "+wdaisy.size());
        System.out.println("black " +bdaisy.size());
        System.out.println("diseased " + ddaisy.size());
        System.out.println(globalTemp+ " " +sun.getLuminosity() + "\n");
        text+=globalTemp+",";
        
    }
    
    /**
	 * @param args
	 * @throws InterruptedException 
	 */
    public void runWorld() throws InterruptedException{
        
        for(int i = 1; i<251;i++){
			Thread.sleep(10);
            System.out.println("Step: " + i);
            step();
            
            ///System.out.println("white "+wdaisy.size());
            //System.out.println("black " +bdaisy.size());
           // System.out.println(globalTemp+"\n");
        }
        text += "\n";
    }
    
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
    
    public void write() throws IOException, InterruptedException{
        try{
            FileWriter out = new FileWriter(getSelectedButtonText(group)+".txt",true);
            out.write(text);
			out.close();
            text = "";
        }catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public static double percentWhite(double localTemp, double STEADY_STATE){
		double diff = localTemp - STEADY_STATE;
		//System.out.println(diff);
		double percent = diff/100;
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
    
	public static double difference(Thing[][] gridThings, int i, int j, int xMax, int YMax){
        double total = 0;
        //Cell above
        if(i == xMax){
            total = gridThings[0][j].localTemp;
        }else{
            total = gridThings[i+1][j].localTemp;
        }
        //System.out.println(total);
        //Cell below
        if(i == 0){
            total +=  gridThings[xMax][j].localTemp;
        }
        else{
            total += gridThings[i-1][j].localTemp;
        }
              //  System.out.println(total);

        //Cell to left
        if(j == 0){
            total += gridThings[i][YMax].localTemp;
        }
        else{
            total += gridThings[i][j-1].localTemp;
        }
          //      System.out.println(total);

        //Cell to right
        if(j == YMax){
            total += gridThings[i][0].localTemp;
        }
        else{
            total += gridThings[i][j+1].localTemp;
        }
        //        System.out.println(total);

        total -= 4*gridThings[i][j].localTemp;
       // System.out.println(total);
        return total;
            
    }
    
    
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		World world = new World(30,30);
       
	}

}
