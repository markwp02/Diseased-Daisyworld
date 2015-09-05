
public class BlackDaisy extends Daisy{


	public BlackDaisy(double globalTemp, int i, int j){
		super("images\\black.jpg", globalTemp, i, j);
		this.albedo = 0.25;//reflect 25% of solar radiation
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	void update() {
		// TODO Auto-generated method stub
		
	}

}
