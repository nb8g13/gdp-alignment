import java.util.Map;

public class Mutation {
	Map<String, String> o;
	String a;
	long t; 
	
	private Mutation() {}
	
	public Mutation(Map<String, String> o, String a, long t) {
		this.o = o;
		this.a = a;
		this.t= t;
	}
	
	public Map<String, String> getO() {
		return o;
	}
	
	public String getA() {
		return a;
	}
	
	public long getT() {
		return t;
	}
	
	public void setO(Map<String, String> o) {
		this.o = o;
	}
	
	public void setA(String a) {
		this.a = a;
	}
	
	public void setT(long t) {
		this.t= t;
	}
}
