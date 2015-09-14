
public class Sun {

	double luminosity;
	//final double increase = 0.006;
    //final double increase = 0.003;
    final double increase = 0.004;
	public Sun(){
		//luminosity = 1.0;
        luminosity = 0.6;
	}
	
	public void updateSun(){
		luminosity += increase;
	}
	
	public double getLuminosity(){
		return luminosity;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
