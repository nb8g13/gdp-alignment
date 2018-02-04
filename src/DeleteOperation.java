
public class DeleteOperation implements Operation {
	
	int size;
	
	public DeleteOperation(int size) {
		this.size = size;
	}
	
	public Caption performOperation(String text, Edit edit, double reputation) {
		StringBuilder sb = new StringBuilder(text);
		sb.delete(edit.getPos(), edit.getPos() + size);
		Caption cap = new Caption(sb.toString(), reputation);

		return cap;
	}
	
	public String toString() {
		return "DELETE: " + size*-1;
	}
}
