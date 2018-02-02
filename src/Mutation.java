import java.util.Map;

public class Mutation {
	
	Edit o;
	String a;
	long t; 
	
	
	private Mutation() {}
	
	public Mutation(Edit o, String a, long t) {
		this.o = o;
		this.a = a;
		this.t= t;
	}
	
	public Edit getO() {
		return o;
	}
	
	public String getA() {
		return a;
	}
	
	public long getT() {
		return t;
	}
	
	public void setO(Edit o) {
		this.o = o;
	}
	
	public void setA(String a) {
		this.a = a;
	}
	
	public void setT(long t) {
		this.t= t;
	}
	
	public String toString() {
		String str = "";
		str = str + "author: " + this.a + "\n";
		str = str + "timestamp: " + this.t + "\n";
		
		return str;
	}
	
}
