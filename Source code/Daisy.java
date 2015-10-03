/**
* Daisy object can be White, Black, or Diseased
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public abstract class Daisy extends Thing{

	/**
	* @param r red
	* @param g green
	* @param b blue
	* @param localTemp temperature of daisy
	* @param i row of daisy in grid
	* @param j column of daisy in grid
	*/
	public Daisy(int r,int g, int b, double LocalTemp, int i, int j){
		super(r,g,b, LocalTemp,i,j);
	}
	
	

}
