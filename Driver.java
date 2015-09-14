import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Driver {

	
	public static void run(String filename) throws IOException, InterruptedException{
		try {
			String text = "";
			
			
			FileWriter out = new FileWriter(filename);
			
			
			World world = new World(25,25);
			//grid.updateGrid();
				for(int i = 1; i<201;i++){
					Thread.sleep(100);
					
					
					
					world.updateFertileSoil();
                    world.updateWhiteDaisies();
                    world.updateBlackDaisies();
                    world.updateGrid();
                    world.sun.updateSun();
                    world.updateTemp();
                    world.updateHeatMap();
					text+=world.globalTemp+",";
					
					System.out.println("Step: " + i);
					System.out.println("white "+world.wdaisy.size());
					System.out.println("black " +world.bdaisy.size());
					System.out.println(world.globalTemp+"\n");
				}
			text+="\n";
			out.write(text);
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		run("filename.txt");
		

		
	}

}
