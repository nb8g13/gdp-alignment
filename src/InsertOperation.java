
public class InsertOperation implements Operation {
	
	String insertion;
	
	public InsertOperation(String insertion) {
		this.insertion = insertion;
	}
	
	public Caption performOperation(String text, Edit edit, double reputation) {
		StringBuilder sb = new StringBuilder(text);
		sb.insert(edit.getPos(), insertion);
		Caption cap = new Caption(sb.toString(), reputation);
		return cap;
	}
	
	public String toString() {
		return "INSERTION: " + insertion;
	}
}
