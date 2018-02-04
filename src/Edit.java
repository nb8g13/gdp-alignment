import java.util.Iterator;
import java.util.List;

public class Edit {
	
	List<Operation> operations;
	int pos = 0;

	public Edit(List<Operation> ops) {
		this.operations = ops;
	}
	
	public String toString() {
		String str = "";
		for (Operation op : operations) {
			str = str + op.toString() + "\n";
		}
		
		return str;
	}
	
	public Caption applyEdit(Caption caption, double reputation) {
		Iterator<Operation> iter = this.operations.iterator();
		while(iter.hasNext()) {
			Operation op = iter.next();
			caption = op.performOperation(caption.getText(), this, reputation);
		}
		caption.setPos(0);
		return caption;
		
	}
	
	public int getPos() {
		return this.pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
}
