/**
 * Class for making captions with the mouse
 * @author LukeStacey
 *
 */
public class CursorOperation implements Operation {
	
	int move;
	/**
	 * Constructor
	 * @param move int that is stored in the object
	 */
	public CursorOperation(int move) {
		this.move = move;
	}
	
	public Caption performOperation(String text, Edit edit, double reputation) {
		Caption cap = new Caption(text, reputation);
		edit.setPos(edit.getPos()+move);
		return cap;
	}
	/**
	 * Tostring function for printing the object
	 * @return string that includes this' move variable
	 */
	public String toString() {
		return "MOVE CURSOR: " + move;
	}
}
