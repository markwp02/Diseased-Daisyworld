/**
* Black daisy object warms the planet
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class BlackDaisy extends Daisy{

	/**
	* @param localTemp temperature of daisy
	* @param i row of daisy in grid
	* @param j column of daisy in grid
	*/
	public BlackDaisy(double localTemp, int i, int j){
		super(0,0,0,localTemp,i,j); // black
		this.albedo = 0.25;//reflect 25% of solar radiation
	}
	

}
