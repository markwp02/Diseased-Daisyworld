import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.awt.SystemColor.*;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;


public class World {

    //Color color = new Color();
	JFrame frame = new JFrame();
    JFrame heatFrame = new JFrame();
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
	double globalTemp = 0;
	double birthrate, deathrate = 0.1, perWhite, ran, chance = 0;
    
	/**
	 * @param x
	 * @param y
	 */
	public World(int x, int y) {
	
		// get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		 
		// get 2/3 of the height, and 2/3 of the width
		int height = screenSize.height * 1 / 2;
		int width = screenSize.width * 1 / 2;
		 
		// set the jframe height and width
		frame.setPreferredSize(new Dimension(width, height));
        heatFrame.setPreferredSize(new Dimension(width, height));
		
		this.x=x;
		this.y=y;
		frame.setLayout(new GridLayout(x, y));
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
				else if(ran <= chance*3){
					FertileSoil soil = new FertileSoil(globalTemp,i,j);
					grid[i][j] = soil.jb;
					fsoil.add(soil);
					gridThings[i][j] = soil;
                    
				}
				else{
					InfertileSoil soil = new InfertileSoil(globalTemp,i,j);
					grid[i][j] = soil.jb;
					isoil.add(soil);
					gridThings[i][j] = soil;
                    
				}
				temp = new JButton();
                temp.setBackground(new Color(0,0,255));
                heatGrid[i][j] = temp;
				frame.add(grid[i][j]);
                heatFrame.add(heatGrid[i][j]);
			
			}
		}
        
        Thing temp = gridThings[x/2][y/2];
        temp.localTemp = 1000;
        
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
				
				//problem is that I do not take into account surrounding temperatures
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
				//frame.revalidate();
				//frame.repaint();
				//frame.add(grid[tmp.i][tmp.j]);
				
				
			}
		}
		//frame.invalidate();
		//frame.validate();
		//frame.revalidate();
		//frame.repaint();
	}
	
	public void updateGrid(){
		
		frame.getContentPane().removeAll();
		
		for(int i=0;i<x;i++){
			for(int j=0;j<y;j++){
				frame.add(grid[i][j]);
			
			}
		}
		
		frame.revalidate();
		
		frame.repaint();
		 
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
		double localTemp;
		Thing tmp;
		double luminosity = sun.getLuminosity();
		double albedoIncrease;
		double absorbed;
        
        JButton heatColor;
		
		for(int j=0;j<y;j++){
			for(int i=0;i<x;i++){
				tmp = gridThings[j][i];
				// get average from neighbours
				tmp.localTemp = weightedLocalTemp(gridThings,i,j,x,y);
				// update temp according to sun and albedo
				absorbed = 1-tmp.albedo;
				albedoIncrease = 2*absorbed;//changes to number between 1 and 2 
				tmp.localTemp *= luminosity*albedoIncrease;
                if(tmp.localTemp < 10){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,0,255));
                        heatGrid[i][j] = heatColor;
                }    
                else if(tmp.localTemp < 20){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(0,255,0));
                        heatGrid[i][j] = heatColor;
                }
                else if(tmp.localTemp < 30){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(239,255,0));
                        heatGrid[i][j] = heatColor;
                }
                else if(tmp.localTemp < 40){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(255,179,0));
                        heatGrid[i][j] = heatColor;
                }                 
                else if(tmp.localTemp < 50){
                        heatColor = new JButton();
                        heatColor.setBackground(new Color(255,90,0));
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
	
	/**
	 * Finds weighted average from among surrounding cells
	 * 0.4 central cell, 0.1 up,down,left,right and 0.05 diagonals
	 * @param gridThings
	 * @param i x coordinate of central cell
	 * @param j y coordinate of central cell
	 * @param xMax number of x coordinates
	 * @param yMax number of y coordinates
	 * @return the weighted average
	 */
	public static double weightedLocalTemp(Thing[][] gridThings,int i,int j,int xMax,int yMax){
		double total = 0;
		//System.out.println(i + ", " + j);
		total += gridThings[i][j].localTemp*0.4;
		if(i == 0){
			if(j==0){
				total += gridThings[xMax-1][j].localTemp*0.1;
				total += gridThings[i+1][j].localTemp*0.1;
				total += gridThings[i][yMax-1].localTemp*0.1;
				total += gridThings[i][j+1].localTemp*0.1;
				total += gridThings[xMax-1][yMax-1].localTemp*0.05;
				total += gridThings[xMax-1][j+1].localTemp*0.05;
				total += gridThings[i+1][yMax-1].localTemp*0.05;
				total += gridThings[i+1][j+1].localTemp*0.05;
			}
			else if(j==yMax-1){
				total += gridThings[xMax-1][j].localTemp*0.1;
				total += gridThings[i+1][j].localTemp*0.1;
				total += gridThings[i][j-1].localTemp*0.1;
				total += gridThings[i][0].localTemp*0.1;
				total += gridThings[xMax-1][j-1].localTemp*0.05;
				total += gridThings[xMax-1][0].localTemp*0.05;
				total += gridThings[i+1][j-1].localTemp*0.05;
				total += gridThings[i+1][0].localTemp*0.05;
			}
			else{
				total += gridThings[xMax-1][j].localTemp*0.1;
				total += gridThings[i+1][j].localTemp*0.1;
				total += gridThings[i][j-1].localTemp*0.1;
				total += gridThings[i][j+1].localTemp*0.1;
				total += gridThings[xMax-1][j-1].localTemp*0.05;
				total += gridThings[xMax-1][j+1].localTemp*0.05;
				total += gridThings[i+1][j-1].localTemp*0.05;
				total += gridThings[i+1][j+1].localTemp*0.05;
			}
		}
		else if(i == xMax-1){
			if(j==0){
				total += gridThings[i-1][j].localTemp*0.1;
				total += gridThings[0][j].localTemp*0.1;
				total += gridThings[i][yMax-1].localTemp*0.1;
				total += gridThings[i][j+1].localTemp*0.1;
				total += gridThings[i-1][yMax-1].localTemp*0.05;
				total += gridThings[i-1][j+1].localTemp*0.05;
				total += gridThings[0][yMax-1].localTemp*0.05;
				total += gridThings[0][j+1].localTemp*0.05;
			}
			else if(j==yMax-1){
				total += gridThings[i-1][j].localTemp*0.1;
				total += gridThings[0][j].localTemp*0.1;
				total += gridThings[i][j-1].localTemp*0.1;
				total += gridThings[i][0].localTemp*0.1;
				total += gridThings[i-1][j-1].localTemp*0.05;
				total += gridThings[i-1][0].localTemp*0.05;
				total += gridThings[0][j-1].localTemp*0.05;
				total += gridThings[0][0].localTemp*0.05;
			}
			else{
				total += gridThings[i-1][j].localTemp*0.1;
				total += gridThings[0][j].localTemp*0.1;
				total += gridThings[i][j-1].localTemp*0.1;
				total += gridThings[i][j+1].localTemp*0.1;
				total += gridThings[i-1][j-1].localTemp*0.05;
				total += gridThings[i-1][j+1].localTemp*0.05;
				total += gridThings[0][j-1].localTemp*0.05;
				total += gridThings[0][j+1].localTemp*0.05;
			}
		}
		else{
			if(j==0){
				total += gridThings[i-1][j].localTemp*0.1;
				total += gridThings[i+1][j].localTemp*0.1;
				total += gridThings[i][yMax-1].localTemp*0.1;
				total += gridThings[i][j+1].localTemp*0.1;
				total += gridThings[i-1][yMax-1].localTemp*0.05;
				total += gridThings[i-1][j+1].localTemp*0.05;
				total += gridThings[i+1][yMax-1].localTemp*0.05;
				total += gridThings[i+1][j+1].localTemp*0.05;
			}
			else if(j==yMax-1){
				total += gridThings[i-1][j].localTemp*0.1;
				total += gridThings[i+1][j].localTemp*0.1;
				total += gridThings[i][j-1].localTemp*0.1;
				total += gridThings[i][0].localTemp*0.1;
				total += gridThings[i-1][j-1].localTemp*0.05;
				total += gridThings[i-1][0].localTemp*0.05;
				total += gridThings[i+1][j-1].localTemp*0.05;
				total += gridThings[i+1][0].localTemp*0.05;
			}
			else{
				total += gridThings[i-1][j].localTemp*0.1;
				total += gridThings[i+1][j].localTemp*0.1;
				total += gridThings[i][j-1].localTemp*0.1;
				total += gridThings[i][j+1].localTemp*0.1;
				total += gridThings[i-1][j-1].localTemp*0.05;
				total += gridThings[i-1][j+1].localTemp*0.05;
				total += gridThings[i+1][j-1].localTemp*0.05;
				total += gridThings[i+1][j+1].localTemp*0.05;
			}
		}
		
		return total;
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		World world = new World(25,25);
        //world.gridThings[5,5].localTemp = 1000;
       // grid.updateGrid();
        
		//grid.updateGrid();
			for(int i = 1; i<100;i++){
				Thread.sleep(100);
				
				// Scanner scan= new Scanner(System.in);
				// String text= scan.nextLine();
				
				world.updateFertileSoil();
				world.updateWhiteDaisies();
				world.updateBlackDaisies();
				world.updateGrid();
				world.sun.updateSun();
				world.updateTemp();
                world.updateHeatMap();
                
        
				System.out.println("Step: " + i);
				System.out.println("white "+world.wdaisy.size());
				System.out.println("black " +world.bdaisy.size());
				System.out.println(world.globalTemp+"\n");
			}
	}

}
