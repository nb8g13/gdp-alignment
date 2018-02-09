import java.util.Map;
/**
 * Class for storing a single change to caption
 * @author LukeStacey
 *
 */
public class Mutation {
	
	Edit o;
	String a;
	long t; 
	
	
	private Mutation() {}
	/**
	 * Constructor
	 * @param o edit type
	 * @param a string edited
	 * @param t time of edit
	 */
	public Mutation(Edit o, String a, long t) {
		this.o = o;
		this.a = a;
		this.t= t;
	}
	/**
	 * getter for edit
	 * @return edit
	 */
	public Edit getO() {
		return o;
	}
	/**
	 * getter for a
	 * @return a
	 */
	public String getA() {
		return a;
	}
	/**
	 * getter for time
	 * @return time
	 */
	public long getT() {
		return t;
	}
	/**
	 * setter for edit
	 * @param o new value for edit
	 */
	public void setO(Edit o) {
		this.o = o;
	}
	/**
	 * setter for a
	 * @param a new value for a
	 */
	public void setA(String a) {
		this.a = a;
	}
	/**
	 * setter for t
	 * @param t new value for t
	 */
	public void setT(long t) {
		this.t= t;
	}
	/**
	 * Converts this object to a string
	 */
	public String toString() {
		String str = "";
		str = str + "author: " + this.a + "\n";
		str = str + "timestamp: " + this.t + "\n";
		
		return str;
	}
	
}
