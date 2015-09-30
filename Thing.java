import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 
 */

/**
 * @author Mark
 *
 */
public abstract class Thing {

	JButton jb;
	double localTemp;
    double newTemp;
	int i,j;
	double albedo;
    boolean infected;
	
	public Thing(String image, double localTemp,int i,int j){
		jb = new JButton(new ImageIcon(image));
		this.localTemp = localTemp;
        this.newTemp = localTemp;
		this.i=i;
		this.j=j;
        this.infected = false;
	}
	

    public double getAlbedo(){
        return albedo;
    }
	
	public boolean isInfected(){
        return infected;
    }
    
    public double getInfectRate(){
        return 0;
    }
}
