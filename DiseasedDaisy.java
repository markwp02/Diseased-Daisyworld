
public class DiseasedDaisy extends Daisy{

    double infectRate;
	public DiseasedDaisy(double globalTemp, int i, int j, double infectRate){
		super("images\\bacteria.jpg", globalTemp, i, j);
		this.albedo = 0.3;//reflect 50% of solar radiation
        this.infected = true;
        this.infectRate = infectRate;
	}
    
    public double getInfectRate(){
        return infectRate;
    }
	

}
