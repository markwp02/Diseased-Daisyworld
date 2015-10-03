import java.awt.Color;

/**
* Ground object can grow White, or Black, Daisies
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class Ground extends Thing{

	/**
	* @param localTemp temperature of daisy
	* @param i row of ground in grid
	* @param j column of ground in grid
	*/
	public Ground(double localTemp,int i, int j) {
		//super("images\\gardensoil.jpg", localTemp, i, j);
		super(204,102,0,localTemp,i,j);
		this.albedo = 0.5; // reflects 50% of solar radiation
        this.infected = false;
	}

	
	

}
