/**
* White daisy object cools the planet
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class WhiteDaisy extends Daisy{

	/**
	* @param localTemp temperature of daisy
	* @param i row of daisy in grid
	* @param j column of daisy in grid
	*/
	public WhiteDaisy(double localTemp, int i, int j) {
		super(255,255,255,localTemp,i,j); // white
		this.albedo = 0.75;//reflect 75% of solar radiation
	}

	

}
