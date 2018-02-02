
public class CursorOperation implements Operation {
	
	int move;
	
	public CursorOperation(int move) {
		this.move = move;
	}
	
	public void performOperation(Caption cap) {
		cap.setPos(cap.getPos() + move);
	}
	
	public String toString() {
		return "MOVE CURSOR: " + move;
	}
}
