/**
* Sun object that has an increasing luminosity
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class Sun {

	double luminosity;
    final double increase = 0.01;
	
	public Sun(){
        luminosity = 0.6;
	}
	
	/**
	* Increases luminosity
	*/
	public void updateSun(){
		luminosity += increase;
	}
	
	/**
	* @return double luminosity
	*/
	public double getLuminosity(){
		return luminosity;
	}
	
	

}
