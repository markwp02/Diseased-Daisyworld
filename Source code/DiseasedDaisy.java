/**
* Diseased daisy object slightly warms the planet as scorching causes daisy
* go a dark brown color. Can infect other daisies
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class DiseasedDaisy extends Daisy{

	/**
	* @param localTemp temperature of daisy
	* @param i row of daisy in grid
	* @param j column of daisy in grid
	*/
	public DiseasedDaisy(double localTemp, int i, int j){
		super(204,204,0,localTemp,i,j); // gold
		this.albedo = 0.4;//reflect 50% of solar radiation
        this.infected = true;
	}
}
