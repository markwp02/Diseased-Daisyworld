
public class FertileSoil extends Soil{

	public FertileSoil(double localTemp,int i, int j) {
		//super("images\\gardensoil.jpg", localTemp, i, j);
        super("images\\ground.jpg", localTemp, i, j);
		this.albedo = 0.5; // reflects 50% of solar radiation
        this.infected = false;
	}

	
	

}
