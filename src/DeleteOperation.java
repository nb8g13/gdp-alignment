
public class DeleteOperation implements Operation {
	
	int size;
	
	public DeleteOperation(int size) {
		this.size = size;
	}
	
	public void performOperation(Caption cap) {
		StringBuilder sb = new StringBuilder(cap.getText());
		sb.delete(cap.pos, cap.getPos() + size);
		cap.setText(sb.toString());
	}
	
	public String toString() {
		return "DELETE: " + size*-1;
	}
}
