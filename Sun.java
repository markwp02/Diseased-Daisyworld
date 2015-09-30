
public class Sun {

	double luminosity;
	//final double increase = 0.006;
    //final double increase = 0.003;
    //final double increase = 0.004;
    //final double increase = 5.8;
    final double increase = 0.01;
	public Sun(){
		//luminosity = 1.0;
        luminosity = 0.6;
        
        //luminosity = 295.5;
       //luminosity = 415;
       //luminosity = 830;
      //luminosity = 1660;
      //luminosity = 604;
	}
	
	public void updateSun(){
		luminosity += increase;
	}
	
	public double getLuminosity(){
		return luminosity;
	}
	
	

}
