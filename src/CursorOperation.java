
public class CursorOperation implements Operation {
	
	int move;
	
	public CursorOperation(int move) {
		this.move = move;
	}
	
	public Caption performOperation(String text, Edit edit, double reputation) {
		Caption cap = new Caption(text, reputation);
		edit.setPos(edit.getPos()+move);
		return cap;
	}
	
	public String toString() {
		return "MOVE CURSOR: " + move;
	}
}
