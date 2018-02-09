/**
 * Operation type for inserting into string
 * @author LukeStacey
 *
 */
public class InsertOperation implements Operation {
	
	String insertion;
	/**
	 * Constructor
	 * @param insertion specifies the String to be inserted
	 */
	public InsertOperation(String insertion) {
		this.insertion = insertion;
	}
	/**
	 * Applies this objects operation to the text
	 * @param text the string that shall be edited
	 * @param edit the edit object containing more information on where to insert
	 * @param reputation the reputation to be put in the caption returned
	 * @return edited string in a caption with given reputation
	 */
	public Caption performOperation(String text, Edit edit, double reputation) {
		StringBuilder sb = new StringBuilder(text);
		sb.insert(edit.getPos(), insertion);
		Caption cap = new Caption(sb.toString(), reputation);
		return cap;
	}
	/**
	 * Turns the object into a string so it can be printed
	 */
	public String toString() {
		return "INSERTION: " + insertion;
	}
}
