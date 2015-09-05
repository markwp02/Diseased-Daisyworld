
public class FertileSoil extends Soil{

	public FertileSoil(double localTemp,int i, int j) {
		super("images\\gardensoil.jpg", localTemp, i, j);
		this.albedo = 0.5; // reflects 50% of solar radiation
		// TODO Auto-generated constructor stub
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
