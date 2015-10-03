import javax.swing.JButton;
import java.awt.Color;

/**
* Thing abstract object can be a Daisy or Ground
*
* @author Mark Paterson 22643202
* @version 1.0
* @since 1/10/2015
*/
public abstract class Thing {

	JButton jb;
	double localTemp;
    double newTemp;
	int i,j;
	double albedo;
    boolean infected;
	
	/**
	* @param r red
	* @param g green
	* @param b blue
	* @param localTemp temperature of Thing
	* @param i row of ground in Thing
	* @param j column of ground in Thing
	*/
	public Thing(int r,int g,int b, double localTemp,int i,int j){
		jb = new JButton();
		jb.setBackground(new Color(r,g,b));
		this.localTemp = localTemp;
        this.newTemp = localTemp;
		this.i=i;
		this.j=j;
        this.infected = false;
	}
	
	/**
	* @return albedo
	*/
    public double getAlbedo(){
        return albedo;
    }
	
	/**
	* @return true if infected, false otherwise
	*/
	public boolean isInfected(){
        return infected;
    }
    
}
