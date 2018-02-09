import java.util.Iterator;
import java.util.List;
/**
 * SuperClass for storing different changes to captions and strigns
 * @author LukeStacey
 *
 */
public class Edit {
	
	List<Operation> operations;
	int pos = 0;
	/**
	 * Constructor
	 * @param ops operation this edit object will apply
	 */
	public Edit(List<Operation> ops) {
		this.operations = ops;
	}
	/**
	 * converts object to a string for outputting
	 */
	public String toString() {
		String str = "";
		for (Operation op : operations) {
			str = str + op.toString() + "\n";
		}
		
		return str;
	}
	/**
	 * Runs this objects operations on the given caption and then rebuilds with given reputation
	 * @param caption caption to be edited
	 * @param reputation reputation to be constructed with at the end
	 * @return edited caption with given reputation
	 */
	public Caption applyEdit(Caption caption, double reputation) {
		Iterator<Operation> iter = this.operations.iterator();
		while(iter.hasNext()) {
			Operation op = iter.next();
			caption = op.performOperation(caption.getText(), this, reputation);
		}
		caption.setPos(0);
		return caption;
		
	}
	/**
	 * getter for position
	 * @return position
	 */
	public int getPos() {
		return this.pos;
	}
	/**
	 * setter for position
	 * @param pos new value for position
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
}
