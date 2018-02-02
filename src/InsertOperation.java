
public class InsertOperation implements Operation {
	
	String insertion;
	
	public InsertOperation(String insertion) {
		this.insertion = insertion;
	}
	
	public void performOperation(Caption cap) {
		StringBuilder sb = new StringBuilder(cap.getText());
		sb.insert(cap.getPos(), insertion);
		cap.setText(sb.toString());
	}
	
	public String toString() {
		return "INSERTION: " + insertion;
	}
}
