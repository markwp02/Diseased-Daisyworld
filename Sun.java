
public class Sun {

	double luminosity;
	final double increase = 0.000;
	public Sun(){
		luminosity = 1.0;
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
