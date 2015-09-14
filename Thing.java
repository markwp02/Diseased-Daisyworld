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
	int i,j;
	double albedo;
	
	public Thing(String image, double localTemp,int i,int j){
		jb = new JButton(new ImageIcon(image));
		this.localTemp = localTemp;
		this.i=i;
		this.j=j;
	}
	
	abstract void update();

    public double getAlbedo(){
        return albedo;
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
