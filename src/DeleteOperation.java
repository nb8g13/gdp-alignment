/**
 * An operation class that can delete a section from a character
 * @author LukeStacey
 *
 */
public class DeleteOperation implements Operation {
	
	int size;
	/**
	 * Constructor that sets the size of the setion to be deleted
	 * @param size the size of the section that this will delete
	 */
	public DeleteOperation(int size) {
		this.size = size;
	}
	/**
	 * Deletes a section of a string
	 * @param text the string to be edited
	 * @param edit the area that will be deleted
	 * @param reputation the reputation to be assigned to the constructed caption
	 * @return the caption with the edited string and given reputation
	 */
	public Caption performOperation(String text, Edit edit, double reputation) {
		StringBuilder sb = new StringBuilder(text);
		sb.delete(edit.getPos(), edit.getPos() + size);
		Caption cap = new Caption(sb.toString(), reputation);

		return cap;
	}
	/**
	 * Converts the object into a printable string
	 */
	public String toString() {
		return "DELETE: " + size*-1;
	}
}
