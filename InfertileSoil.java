
public class InfertileSoil extends Soil{

	public InfertileSoil(double localTemp, int i, int j){
		super("images\\badsoil.jpg", localTemp, i, j);
		this.albedo = 0.5;//reflects 50% of solar radiation
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
