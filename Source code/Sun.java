/**
* Sun object that has an increasing luminosity
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public class Sun {

	double luminosity;
    final double increase = 0.005;
	
	public Sun(){
        luminosity = 0.65;
		//luminosity = 1;
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
